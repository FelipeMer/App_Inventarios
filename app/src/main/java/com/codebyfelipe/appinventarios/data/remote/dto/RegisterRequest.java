package com.codebyfelipe.appinventarios.data.remote.dto;

public class RegisterRequest {
    private String nombre;
    private String email;
    private String password;
    private String rol_id; // opcional — puede quedar null

    public RegisterRequest(String nombre, String email, String password, String rol_id) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol_id = rol_id;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRol_id() { return rol_id; }
}