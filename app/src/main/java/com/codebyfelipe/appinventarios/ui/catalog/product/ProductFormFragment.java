package com.codebyfelipe.appinventarios.ui.catalog.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.codebyfelipe.appinventarios.R;
import com.codebyfelipe.appinventarios.data.remote.dto.Categoria;
import com.codebyfelipe.appinventarios.data.remote.dto.CreateProductoRequest;
import com.codebyfelipe.appinventarios.data.remote.dto.Producto;
import com.codebyfelipe.appinventarios.databinding.FragmentProductFormBinding;
import com.codebyfelipe.appinventarios.util.Resource;
import java.util.ArrayList;
import java.util.List;

public class ProductFormFragment extends Fragment {  //Formulario de crear/editar. Usa productoId (argumento de navegación) para decidir si es edición.

    public static final String ARG_PRODUCTO_ID = "productoId";
    private static final String REQUEST_KEY_BARCODE = "barcode_result";

    private FragmentProductFormBinding binding;
    private ProductViewModel viewModel;

    private String productoId; //Si esta en Null, sin Id es para crear un producto nuevo
    private final List<Categoria> categoriasList = new ArrayList<>();
    private ArrayAdapter<String> categoriaAdapter;

    private String codigoEscaneadoPendiente;
    private boolean datosCargados = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        if (getArguments() != null) {
            productoId = getArguments().getString(ARG_PRODUCTO_ID);
        }

        setupCategoriaSpinner();
        setupScannerListener();

        binding.btnScan.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_productFormFragment_to_barcodeScannerFragment)
        );

        binding.btnGuardar.setOnClickListener(v -> guardarProducto());

        viewModel.getCategorias().observe(getViewLifecycleOwner(), this::handleCategorias);
        viewModel.getProductoDetalle().observe(getViewLifecycleOwner(), this::handleProductoDetalle);
        viewModel.getGuardarResult().observe(getViewLifecycleOwner(), this::handleGuardarResult);

        viewModel.cargarCategorias();

        if (productoId != null) {
            viewModel.cargarProducto(productoId);
        }
    }

    private void setupCategoriaSpinner() {
        categoriaAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        binding.spinnerCategoria.setAdapter(categoriaAdapter);
    }

    private void setupScannerListener() {
        getParentFragmentManager().setFragmentResultListener(
                REQUEST_KEY_BARCODE, this, (requestKey, bundle) -> {
                    String codigo = bundle.getString("codigo");
                    if (codigo != null) {
                        codigoEscaneadoPendiente = codigo;
                        binding.etCodigoBarras.setText(codigo);
                    }
                });
    }

    private void handleCategorias(Resource<List<Categoria>> resource) {
        if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
            categoriasList.clear();
            categoriasList.addAll(resource.getData());

            List<String> nombres = new ArrayList<>();
            for (Categoria c : categoriasList) nombres.add(c.getNombre());

            categoriaAdapter.clear();
            categoriaAdapter.addAll(nombres);
            categoriaAdapter.notifyDataSetChanged();

            // Si estamos editando y ya cargó el producto antes que las categorías, sincroniza la selección
            seleccionarCategoriaSiCorresponde();
        }
    }

    private Producto productoCargado;

    private void handleProductoDetalle(Resource<Producto> resource) {
        if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
            productoCargado = resource.getData();
            poblarFormulario(productoCargado);
        }
    }

    private void poblarFormulario(Producto p) {
        String codigoAMostrar = codigoEscaneadoPendiente != null
                ? codigoEscaneadoPendiente
                : p.getCodigo_barras();
        binding.etCodigoBarras.setText(codigoAMostrar);

        binding.etNombre.setText(p.getNombre());
        binding.etDescripcion.setText(p.getDescripcion());
        binding.etMarca.setText(p.getMarca());
        binding.etTalla.setText(p.getTalla());
        binding.etColor.setText(p.getColor());
        binding.etPrecioCompra.setText(String.valueOf(p.getPrecio_compra()));
        binding.etPrecioVenta.setText(String.valueOf(p.getPrecio_venta()));
        binding.etStockMinimo.setText(String.valueOf(p.getStock_minimo()));
        binding.btnGuardar.setText("Actualizar producto");

        seleccionarCategoriaSiCorresponde();
    }

    private void seleccionarCategoriaSiCorresponde() {
        if (productoCargado == null || productoCargado.getCategoria() == null || categoriasList.isEmpty()) return;

        for (int i = 0; i < categoriasList.size(); i++) {
            if (categoriasList.get(i).getId_categoria().equals(productoCargado.getCategoria().getId_categoria())) {
                binding.spinnerCategoria.setSelection(i);
                break;
            }
        }
    }

    private void guardarProducto() {
        String nombre = textOf(binding.etNombre);
        String codigoBarras = textOf(binding.etCodigoBarras);

        if (nombre.isEmpty() || codigoBarras.isEmpty() || categoriasList.isEmpty()) {
            mostrarError("Completa al menos nombre, código de barras y categoría");
            return;
        }

        int categoriaIndex = binding.spinnerCategoria.getSelectedItemPosition();
        if (categoriaIndex < 0 || categoriaIndex >= categoriasList.size()) {
            mostrarError("Selecciona una categoría");
            return;
        }

        CreateProductoRequest request = new CreateProductoRequest();
        request.setCodigo_barras(codigoBarras);
        request.setNombre(nombre);
        request.setDescripcion(textOf(binding.etDescripcion));
        request.setCategoria_id(categoriasList.get(categoriaIndex).getId_categoria());
        request.setMarca(textOf(binding.etMarca));
        request.setTalla(textOf(binding.etTalla));
        request.setColor(textOf(binding.etColor));

        try {
            request.setPrecio_compra(Double.parseDouble(textOf(binding.etPrecioCompra)));
            request.setPrecio_venta(Double.parseDouble(textOf(binding.etPrecioVenta)));
            request.setStock_minimo(
                    textOf(binding.etStockMinimo).isEmpty() ? 0 : Integer.parseInt(textOf(binding.etStockMinimo)));
        } catch (NumberFormatException e) {
            mostrarError("Revisa que los precios y el stock mínimo sean números válidos");
            return;
        }

        viewModel.guardarProducto(productoId, request);
    }

    private void handleGuardarResult(Resource<Producto> resource) {
        switch (resource.getStatus()) {
            case LOADING:
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnGuardar.setEnabled(false);
                break;
            case SUCCESS:
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Producto guardado", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
                break;
            case ERROR:
                binding.progressBar.setVisibility(View.GONE);
                binding.btnGuardar.setEnabled(true);
                mostrarError(resource.getMessage());
                break;
        }
    }

    private void mostrarError(String mensaje) {
        binding.tvError.setText(mensaje);
        binding.tvError.setVisibility(View.VISIBLE);
    }

    private String textOf(com.google.android.material.textfield.TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}