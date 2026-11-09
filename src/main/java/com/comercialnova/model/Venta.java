package com.comercialnova.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad Venta (cabecera). Agrupa los detalles y calcula su propio total,
 * de modo que el total nunca puede quedar desalineado con los detalles.
 */
@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false)
    private String vendedor;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    protected Venta() {
        // requerido por JPA
    }

    public Venta(Cliente cliente, String vendedor) {
        if (cliente == null) {
            throw new IllegalArgumentException("La venta requiere un cliente");
        }
        if (vendedor == null || vendedor.trim().isEmpty()) {
            throw new IllegalArgumentException("La venta requiere un vendedor");
        }
        this.cliente = cliente;
        this.vendedor = vendedor.trim();
        this.fecha = LocalDateTime.now();
    }

    /** Agrega una linea a la venta y recalcula el total. */
    public void agregarDetalle(Producto producto, int cantidad) {
        DetalleVenta detalle = new DetalleVenta(this, producto, cantidad);
        detalles.add(detalle);
        total = total.add(detalle.getSubtotal());
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public String getVendedor() { return vendedor; }
    public LocalDateTime getFecha() { return fecha; }
    public BigDecimal getTotal() { return total; }
    public List<DetalleVenta> getDetalles() { return Collections.unmodifiableList(detalles); }
}
