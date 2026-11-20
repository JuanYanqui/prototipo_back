package com.comercialnova.model;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "movimiento_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "stock_anterior", nullable = false)
    private int stockAnterior;

    @Column(name = "stock_nuevo", nullable = false)
    private int stockNuevo;

    @Column(length = 255)
    private String motivo;

    @Column(length = 120)
    private String responsable;

    @Column(nullable = false)
    private LocalDateTime fecha;

    protected MovimientoInventario() {
    }

    public MovimientoInventario(Producto producto, TipoMovimiento tipo, int cantidad, int stockAnterior,
                                int stockNuevo, String motivo, String responsable) {
        if (producto == null || tipo == null) {
            throw new IllegalArgumentException("El movimiento necesita producto y tipo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad del movimiento debe ser mayor a cero");
        }
        if (stockAnterior < 0 || stockNuevo < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockNuevo = stockNuevo;
        this.motivo = motivo;
        this.responsable = responsable;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Producto getProducto() { return producto; }
    public TipoMovimiento getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public int getStockAnterior() { return stockAnterior; }
    public int getStockNuevo() { return stockNuevo; }
    public String getMotivo() { return motivo; }
    public String getResponsable() { return responsable; }
    public LocalDateTime getFecha() { return fecha; }
}
