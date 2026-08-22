package com.codebyfelipe.appinventarios.ui.catalog.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codebyfelipe.appinventarios.R;
import com.codebyfelipe.appinventarios.data.remote.dto.Producto;
import com.codebyfelipe.appinventarios.databinding.FragmentProductListBinding;
import com.codebyfelipe.appinventarios.util.Resource;

public class ProductListFragment extends Fragment { //Muestra el RecyclerView de productos, tiene la opción para crear uno nuevo.

    private FragmentProductListBinding binding;
    private ProductViewModel viewModel;
    private ProductListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        adapter = new ProductListAdapter(producto -> {
            Bundle args = new Bundle();
            args.putString(ProductFormFragment.ARG_PRODUCTO_ID, producto.getId_producto());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_productListFragment_to_productFormFragment, args);
        });

        binding.rvProductos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProductos.setAdapter(adapter);

        binding.fabAdd.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_productListFragment_to_productFormFragment)
        );

        viewModel.getProductos().observe(getViewLifecycleOwner(), this::handleResult);

        viewModel.cargarProductos();
    }

    private void handleResult(Resource<java.util.List<Producto>> resource) {
        switch (resource.getStatus()) {
            case LOADING:
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.tvEmpty.setVisibility(View.GONE);
                break;

            case SUCCESS:
                binding.progressBar.setVisibility(View.GONE);
                java.util.List<Producto> productos = resource.getData();
                adapter.setProductos(productos);
                binding.tvEmpty.setVisibility(
                        (productos == null || productos.isEmpty()) ? View.VISIBLE : View.GONE
                );
                break;

            case ERROR:
                binding.progressBar.setVisibility(View.GONE);
                binding.tvEmpty.setText(resource.getMessage());
                binding.tvEmpty.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}