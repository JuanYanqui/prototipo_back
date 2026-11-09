package com.comercialnova.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/** Datos que envia el cliente (web o movil) para registrar una venta. */
public class VentaRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotBlank(message = "El vendedor es obligatorio")
    private String vendedor;

    @Valid
    @NotEmpty(message = "La venta debe tener al menos un producto")
    private List<ItemVentaRequest> items;

    /** Requerido por Jackson para leer el JSON. */
    public VentaRequest() { }

    public VentaRequest(Long clienteId, String vendedor, List<ItemVentaRequest> items) {
        this.clienteId = clienteId;
        this.vendedor = vendedor;
        this.items = items;
    }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getVendedor() { return vendedor; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }
    public List<ItemVentaRequest> getItems() { return items; }
    public void setItems(List<ItemVentaRequest> items) { this.items = items; }
}
