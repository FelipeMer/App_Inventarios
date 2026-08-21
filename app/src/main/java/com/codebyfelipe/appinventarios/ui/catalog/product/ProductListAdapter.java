package com.codebyfelipe.appinventarios.ui.catalog.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codebyfelipe.appinventarios.data.remote.dto.Producto;
import com.codebyfelipe.appinventarios.databinding.ItemProductBinding;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Producto producto);
    }

    private List<Producto> productos = new ArrayList<>();
    private final OnProductClickListener listener;

    public ProductListAdapter(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void setProductos(List<Producto> nuevaLista) {
        this.productos = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(productos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;
        private final NumberFormat currencyFormat =
                NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Producto producto, OnProductClickListener listener) {
            binding.tvNombre.setText(producto.getNombre());

            String categoriaNombre = producto.getCategoria() != null
                    ? producto.getCategoria().getNombre()
                    : "Sin categoría";
            binding.tvCategoria.setText(categoriaNombre);

            binding.tvPrecio.setText(currencyFormat.format(producto.getPrecio_venta()));

            binding.getRoot().setOnClickListener(v -> listener.onProductClick(producto));
        }
    }
}