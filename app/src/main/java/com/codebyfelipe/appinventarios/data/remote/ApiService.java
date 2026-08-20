package com.codebyfelipe.appinventarios.data.remote;

import com.codebyfelipe.appinventarios.data.remote.dto.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

public interface ApiService {

    // ---------- AUTH ----------
    @POST("auth/register")
    Call<Usuario> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // ---------- CATALOG: Productos ----------
    @GET("catalog/productos")
    Call<List<Producto>> getProductos();

    @GET("catalog/productos/{id}")
    Call<Producto> getProducto(@Path("id") String id);

    @POST("catalog/productos")
    Call<Producto> createProducto(@Body CreateProductoRequest request);

    @PATCH("catalog/productos/{id}")
    Call<Producto> updateProducto(@Path("id") String id, @Body CreateProductoRequest request);

    // ---------- CATALOG: Categorías ----------
    @GET("catalog/categorias")
    Call<List<Categoria>> getCategorias();

    @POST("catalog/categorias")
    Call<Categoria> createCategoria(@Body Categoria categoria);

    // ---------- CATALOG: Clientes ----------
    @GET("catalog/clientes")
    Call<List<Cliente>> getClientes();

    @POST("catalog/clientes")
    Call<Cliente> createCliente(@Body Cliente cliente);

    // ---------- CATALOG: Proveedores ----------
    @GET("catalog/proveedores")
    Call<List<Proveedor>> getProveedores();

    @POST("catalog/proveedores")
    Call<Proveedor> createProveedor(@Body Proveedor proveedor);

    // ---------- INVENTORY ----------
    @GET("inventory")
    Call<List<Inventario>> getStockAll();

    @GET("inventory/{productoId}")
    Call<Inventario> getStockByProducto(@Path("productoId") String productoId);

    @GET("inventory/movimientos/historial")
    Call<List<Movimiento>> getMovimientos(@Query("productoId") String productoId);

    @GET("inventory/tipos-movimiento")
    Call<List<TipoMovimiento>> getTiposMovimiento();

    // ---------- PURCHASES ----------
    @POST("purchases/entradas")
    Call<Entrada> createEntrada(@Body CreateEntradaRequest request);

    @GET("purchases/entradas")
    Call<List<Entrada>> getEntradas();

    // ---------- SALES ----------
    @POST("sales/salidas")
    Call<Salida> createSalida(@Body CreateSalidaRequest request);

    @GET("sales/salidas")
    Call<List<Salida>> getSalidas();
}