package com.comercialnova.dto;

import java.time.LocalDateTime;

/** Formato unico de error que reciben todos los clientes de la API. */
public class ApiError {

    private final int status;
    private final String error;
    private final String mensaje;
    private final LocalDateTime fecha;

    public ApiError(int status, String error, String mensaje) {
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }

    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMensaje() { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }
}
