package com.codebyfelipe.appinventarios.ui.catalog.product;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.codebyfelipe.appinventarios.data.remote.dto.Categoria;
import com.codebyfelipe.appinventarios.data.remote.dto.CreateProductoRequest;
import com.codebyfelipe.appinventarios.data.remote.dto.Producto;
import com.codebyfelipe.appinventarios.data.repository.CatalogRepository;
import com.codebyfelipe.appinventarios.util.Resource;
import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private final CatalogRepository catalogRepository;
    private final MutableLiveData<Resource<List<Producto>>> productos = new MutableLiveData<>();
    private final MutableLiveData<Resource<Producto>> productoDetalle = new MutableLiveData<>();
    private final MutableLiveData<Resource<Producto>> guardarResult = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Categoria>>> categorias = new MutableLiveData<>();

    public ProductViewModel(@NonNull Application application) {
        super(application);
        this.catalogRepository = new CatalogRepository(application.getApplicationContext());
    }

    public MutableLiveData<Resource<List<Producto>>> getProductos() { return productos; }
    public MutableLiveData<Resource<Producto>> getProductoDetalle() { return productoDetalle; }
    public MutableLiveData<Resource<Producto>> getGuardarResult() { return guardarResult; }
    public MutableLiveData<Resource<List<Categoria>>> getCategorias() { return categorias; }

    public void cargarProductos() {
        catalogRepository.getProductos(productos);
    }

    public void cargarProducto(String id) {
        catalogRepository.getProducto(id, productoDetalle);
    }

    public void cargarCategorias() {
        catalogRepository.getCategorias(categorias);
    }

    // Si productoId es null -> crea. Si no -> actualiza.
    public void guardarProducto(String productoId, CreateProductoRequest request) {
        if (productoId == null) {
            catalogRepository.createProducto(request, guardarResult);
        } else {
            catalogRepository.updateProducto(productoId, request, guardarResult);
        }
    }
}