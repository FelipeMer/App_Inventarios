package com.codebyfelipe.appinventarios.data.remote.dto;

public class Rol {
    private String id_rol;
    private String nombre;
    private String descripcion;

    public String getId_rol() { return id_rol; }
    public void setId_rol(String id_rol) { this.id_rol = id_rol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}