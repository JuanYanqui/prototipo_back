package com.comercialnova.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class IngresoRequest {

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    private String motivo;

    public IngresoRequest() { }

    public IngresoRequest(Integer cantidad, String motivo) {
        this.cantidad = cantidad;
        this.motivo = motivo;
    }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
