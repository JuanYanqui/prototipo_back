package com.comercialnova.dto;

import com.comercialnova.model.MovimientoInventario;
import java.time.LocalDateTime;

public class MovimientoResponse {

    private final Long id;
    private final Long productoId;
    private final String producto;
    private final String tipo;
    private final int cantidad;
    private final int stockAnterior;
    private final int stockNuevo;
    private final String motivo;
    private final String responsable;
    private final LocalDateTime fecha;

    public MovimientoResponse(Long id, Long productoId, String producto, String tipo, int cantidad,
                              int stockAnterior, int stockNuevo, String motivo, String responsable,
                              LocalDateTime fecha) {
        this.id = id;
        this.productoId = productoId;
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockNuevo = stockNuevo;
        this.motivo = motivo;
        this.responsable = responsable;
        this.fecha = fecha;
    }

    public static MovimientoResponse de(MovimientoInventario m) {
        return new MovimientoResponse(m.getId(), m.getProducto().getId(), m.getProducto().getNombre(),
                m.getTipo().name(), m.getCantidad(), m.getStockAnterior(), m.getStockNuevo(),
                m.getMotivo(), m.getResponsable(), m.getFecha());
    }

    public Long getId() { return id; }
    public Long getProductoId() { return productoId; }
    public String getProducto() { return producto; }
    public String getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public int getStockAnterior() { return stockAnterior; }
    public int getStockNuevo() { return stockNuevo; }
    public String getMotivo() { return motivo; }
    public String getResponsable() { return responsable; }
    public LocalDateTime getFecha() { return fecha; }
}
