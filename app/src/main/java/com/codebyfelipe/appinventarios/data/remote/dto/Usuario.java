package com.codebyfelipe.appinventarios.data.remote.dto;

public class Usuario {
    private String id_usuario;
    private String nombre;
    private String email;
    private Rol rol;
    private boolean estado;
    private String fecha_creacion;

    public String getId_usuario() { return id_usuario; }
    public void setId_usuario(String id_usuario) { this.id_usuario = id_usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public String getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion; }
}