package com.codebyfelipe.appinventarios.data.remote.dto;

public class LoginResponse {
    private String access_token;
    private Usuario usuario;

    public String getAccess_token() { return access_token; }
    public void setAccess_token(String access_token) { this.access_token = access_token; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}