package com.comercialnova.dto;

import com.comercialnova.model.Rol;
import com.comercialnova.model.Usuario;

public class LoginResponse {

    private final String sessionId;
    private final Long usuarioId;
    private final String username;
    private final String nombre;
    private final String rol;

    public LoginResponse(String sessionId, Usuario usuario) {
        this.sessionId = sessionId;
        this.usuarioId = usuario.getId();
        this.username = usuario.getUsername();
        this.nombre = usuario.getNombre();
        this.rol = usuario.getRol().name();
    }

    public String getSessionId() { return sessionId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }

    public boolean isAdmin() {
        return Rol.ADMIN.name().equals(rol);
    }
}
