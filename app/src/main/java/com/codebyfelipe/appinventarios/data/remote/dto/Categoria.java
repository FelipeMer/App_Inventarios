package com.codebyfelipe.appinventarios.data.remote.dto;

public class Categoria {
    private String id_categoria;
    private String nombre;
    private String descripcion;
    private boolean estado;
    private String fecha_creacion;

    public String getId_categoria() { return id_categoria; }
    public void setId_categoria(String id_categoria) { this.id_categoria = id_categoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public String getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion; }
}