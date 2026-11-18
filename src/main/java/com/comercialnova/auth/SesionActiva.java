package com.comercialnova.auth;

import com.comercialnova.model.Rol;
import com.comercialnova.model.Usuario;

public class SesionActiva {

    private final String sessionId;
    private final Long usuarioId;
    private final String username;
    private final String nombre;
    private final Rol rol;

    public SesionActiva(String sessionId, Usuario usuario) {
        this.sessionId = sessionId;
        this.usuarioId = usuario.getId();
        this.username = usuario.getUsername();
        this.nombre = usuario.getNombre();
        this.rol = usuario.getRol();
    }

    public String getSessionId() { return sessionId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public Rol getRol() { return rol; }

    public boolean esAdmin() {
        return rol == Rol.ADMIN;
    }
}
