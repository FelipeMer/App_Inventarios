package com.codebyfelipe.appinventarios.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productos")
public class ProductoEntity {

    @PrimaryKey
    @NonNull
    public String id_producto;

    public String codigo_barras;
    public String nombre;
    public String descripcion;
    public String categoria_id;
    public String categoria_nombre;
    public String marca;
    public String talla;
    public String color;
    public double precio_compra;
    public double precio_venta;
    public int stock_minimo;
    public String imagen;
    public boolean estado;
    public String fecha_creacion;
}