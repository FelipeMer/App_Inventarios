package com.codebyfelipe.appinventarios.data.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.codebyfelipe.appinventarios.data.local.AppDatabase;
import com.codebyfelipe.appinventarios.data.local.dao.ProductoDao;
import com.codebyfelipe.appinventarios.data.local.entity.ProductoEntity;
import com.codebyfelipe.appinventarios.data.remote.ApiClient;
import com.codebyfelipe.appinventarios.data.remote.ApiService;
import com.codebyfelipe.appinventarios.data.remote.dto.Categoria;
import com.codebyfelipe.appinventarios.data.remote.dto.CreateProductoRequest;
import com.codebyfelipe.appinventarios.data.remote.dto.Producto;
import com.codebyfelipe.appinventarios.util.AppExecutors;
import com.codebyfelipe.appinventarios.util.NetworkUtils;
import com.codebyfelipe.appinventarios.util.Resource;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogRepository {  //Clase para el manejo de los productos si hay internet, pide al backend y actualiza Room en segundo plano, si no hay internet o falla la red, lee directamente de Room como respaldo.

    private final ApiService apiService;
    private final ProductoDao productoDao;
    private final Context appContext;

    public CatalogRepository(Context context) {
        this.appContext = context.getApplicationContext();
        this.apiService = ApiClient.getApiService(context);
        this.productoDao = AppDatabase.getInstance(context).productoDao();
    }

    public void getProductos(MutableLiveData<Resource<List<Producto>>> result) {
        result.setValue(Resource.loading());

        if (!NetworkUtils.isConnected(appContext)) {
            // Sin internet: va directo a Room, sin intentar la red
            cargarDesdeCache(result, "Sin conexión — mostrando datos guardados");
            return;
        }

        apiService.getProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body();

                    // Actualiza el caché en segundo plano (nunca en el hilo principal)
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        productoDao.deleteAll();
                        productoDao.insertAll(mapToEntities(productos));
                    });

                    result.setValue(Resource.success(productos));
                } else {
                    // La red respondió pero con error entonces intenta caché como respaldo
                    cargarDesdeCache(result, "Error del servidor — mostrando datos guardados");
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                // Falló la conexión a mitad de camino entonces intenta caché como respaldo
                cargarDesdeCache(result, "Error de conexión — mostrando datos guardados");
            }
        });
    }

    private void cargarDesdeCache(MutableLiveData<Resource<List<Producto>>> result, String mensajeSiVacio) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<ProductoEntity> entities = productoDao.getAllSync();

            if (entities.isEmpty()) {
                result.postValue(Resource.error("Sin conexión y sin datos guardados aún"));
            } else {
                result.postValue(Resource.success(mapToDtos(entities)));
            }
        });
    }

    // funciones que convierten entre el DTO de la API y el dto de la bd local

    private List<ProductoEntity> mapToEntities(List<Producto> productos) {
        List<ProductoEntity> entities = new ArrayList<>();
        for (Producto p : productos) {
            ProductoEntity e = new ProductoEntity();
            e.id_producto = p.getId_producto();
            e.codigo_barras = p.getCodigo_barras();
            e.nombre = p.getNombre();
            e.descripcion = p.getDescripcion();
            if (p.getCategoria() != null) {
                e.categoria_id = p.getCategoria().getId_categoria();
                e.categoria_nombre = p.getCategoria().getNombre();
            }
            e.marca = p.getMarca();
            e.talla = p.getTalla();
            e.color = p.getColor();
            e.precio_compra = p.getPrecio_compra();
            e.precio_venta = p.getPrecio_venta();
            e.stock_minimo = p.getStock_minimo();
            e.imagen = p.getImagen();
            e.estado = p.isEstado();
            e.fecha_creacion = p.getFecha_creacion();
            entities.add(e);
        }
        return entities;
    }

    private List<Producto> mapToDtos(List<ProductoEntity> entities) {
        List<Producto> productos = new ArrayList<>();
        for (ProductoEntity e : entities) {
            Producto p = new Producto();
            p.setId_producto(e.id_producto);
            p.setCodigo_barras(e.codigo_barras);
            p.setNombre(e.nombre);
            p.setDescripcion(e.descripcion);

            if (e.categoria_id != null) {
                Categoria c = new Categoria();
                c.setId_categoria(e.categoria_id);
                c.setNombre(e.categoria_nombre);
                p.setCategoria(c);
            }

            p.setMarca(e.marca);
            p.setTalla(e.talla);
            p.setColor(e.color);
            p.setPrecio_compra(e.precio_compra);
            p.setPrecio_venta(e.precio_venta);
            p.setStock_minimo(e.stock_minimo);
            p.setImagen(e.imagen);
            p.setEstado(e.estado);
            p.setFecha_creacion(e.fecha_creacion);
            productos.add(p);
        }
        return productos;
    }

    public void getProducto(String id, MutableLiveData<Resource<Producto>> result) {
        result.setValue(Resource.loading());
        apiService.getProducto(id).enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("No se pudo cargar el producto"));
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                result.setValue(Resource.error("Error de conexión: " + t.getMessage()));
            }
        });
    }

    public void createProducto(CreateProductoRequest request, MutableLiveData<Resource<Producto>> result) {
        result.setValue(Resource.loading());
        apiService.createProducto(request).enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("No se pudo crear el producto"));
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                result.setValue(Resource.error("Error de conexión: " + t.getMessage()));
            }
        });
    }

    public void updateProducto(String id, CreateProductoRequest request, MutableLiveData<Resource<Producto>> result) {
        result.setValue(Resource.loading());
        apiService.updateProducto(id, request).enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("No se pudo actualizar el producto"));
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                result.setValue(Resource.error("Error de conexión: " + t.getMessage()));
            }
        });
    }

    public void getCategorias(MutableLiveData<Resource<List<Categoria>>> result) {
        result.setValue(Resource.loading());
        apiService.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("No se pudieron cargar las categorías"));
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                result.setValue(Resource.error("Error de conexión: " + t.getMessage()));
            }
        });
    }
}