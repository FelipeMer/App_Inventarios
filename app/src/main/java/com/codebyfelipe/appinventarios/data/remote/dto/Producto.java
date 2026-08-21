package com.codebyfelipe.appinventarios.data.remote.dto;

public class Producto {
    private String id_producto;
    private String codigo_barras;
    private String nombre;
    private String descripcion;
    private Categoria categoria;
    private String marca;
    private String talla;
    private String color;
    private double precio_compra;
    private double precio_venta;
    private int stock_minimo;
    private String imagen;
    private boolean estado;
    private String fecha_creacion;

    public String getId_producto() { return id_producto; }
    public void setId_producto(String id_producto) { this.id_producto = id_producto; }

    public String getCodigo_barras() { return codigo_barras; }
    public void setCodigo_barras(String codigo_barras) { this.codigo_barras = codigo_barras; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public double getPrecio_compra() { return precio_compra; }
    public void setPrecio_compra(double precio_compra) { this.precio_compra = precio_compra; }

    public double getPrecio_venta() { return precio_venta; }
    public void setPrecio_venta(double precio_venta) { this.precio_venta = precio_venta; }

    public int getStock_minimo() { return stock_minimo; }
    public void setStock_minimo(int stock_minimo) { this.stock_minimo = stock_minimo; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public String getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion; }
}