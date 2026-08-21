package com.codebyfelipe.appinventarios.data.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.codebyfelipe.appinventarios.data.remote.ApiClient;
import com.codebyfelipe.appinventarios.data.remote.ApiService;
import com.codebyfelipe.appinventarios.data.remote.dto.Categoria;
import com.codebyfelipe.appinventarios.data.remote.dto.CreateProductoRequest;
import com.codebyfelipe.appinventarios.data.remote.dto.Producto;
import com.codebyfelipe.appinventarios.util.Resource;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogRepository {

    private final ApiService apiService;

    public CatalogRepository(Context context) {
        this.apiService = ApiClient.getApiService(context);
    }

    public void getProductos(MutableLiveData<Resource<List<Producto>>> result) {
        result.setValue(Resource.loading());

        apiService.getProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("No se pudieron cargar los productos"));
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                result.setValue(Resource.error("Error de conexión: " + t.getMessage()));
            }
        });
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