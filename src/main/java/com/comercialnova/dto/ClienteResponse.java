package com.comercialnova.dto;

import com.comercialnova.model.Cliente;

public class ClienteResponse {

    private final Long id;
    private final String identificacion;
    private final String nombre;

    public ClienteResponse(Long id, String identificacion, String nombre) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
    }

    public static ClienteResponse de(Cliente c) {
        return new ClienteResponse(c.getId(), c.getIdentificacion(), c.getNombre());
    }

    public Long getId() { return id; }
    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
}
