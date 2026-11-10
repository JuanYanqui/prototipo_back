package com.comercialnova.dto;

import com.comercialnova.model.DetalleVenta;
import com.comercialnova.model.Venta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class VentaResponse {

    private final Long id;
    private final String cliente;
    private final String vendedor;
    private final LocalDateTime fecha;
    private final BigDecimal total;
    private final List<Detalle> detalles;

    public VentaResponse(Long id, String cliente, String vendedor, LocalDateTime fecha,
                         BigDecimal total, List<Detalle> detalles) {
        this.id = id;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.fecha = fecha;
        this.total = total;
        this.detalles = detalles;
    }

    public static VentaResponse de(Venta v) {
        return new VentaResponse(v.getId(), v.getCliente().getNombre(), v.getVendedor(),
                v.getFecha(), v.getTotal(),
                v.getDetalles().stream().map(Detalle::de).collect(Collectors.toList()));
    }

    public Long getId() { return id; }
    public String getCliente() { return cliente; }
    public String getVendedor() { return vendedor; }
    public LocalDateTime getFecha() { return fecha; }
    public BigDecimal getTotal() { return total; }
    public List<Detalle> getDetalles() { return detalles; }

    public static class Detalle {

        private final Long productoId;
        private final String producto;
        private final int cantidad;
        private final BigDecimal precioUnitario;
        private final BigDecimal subtotal;

        public Detalle(Long productoId, String producto, int cantidad,
                       BigDecimal precioUnitario, BigDecimal subtotal) {
            this.productoId = productoId;
            this.producto = producto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = subtotal;
        }

        static Detalle de(DetalleVenta d) {
            return new Detalle(d.getProducto().getId(), d.getProducto().getNombre(),
                    d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal());
        }

        public Long getProductoId() { return productoId; }
        public String getProducto() { return producto; }
        public int getCantidad() { return cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public BigDecimal getSubtotal() { return subtotal; }
    }
}
