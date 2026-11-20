package com.comercialnova.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "producto")
public class Producto {

    public static final int STOCK_MINIMO_POR_DEFECTO = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private int stock;

    @Column(name = "stock_minimo", nullable = false)
    private int stockMinimo = STOCK_MINIMO_POR_DEFECTO;

    @Version
    private Long version;

    protected Producto() {
    }

    public Producto(String nombre, BigDecimal precio, int stock) {
        this(nombre, precio, stock, STOCK_MINIMO_POR_DEFECTO);
    }

    public Producto(String nombre, BigDecimal precio, int stock, int stockMinimo) {
        actualizarDatos(nombre, precio, stock, stockMinimo);
    }

    public final void actualizarDatos(String nombre, BigDecimal precio, int stock) {
        actualizarDatos(nombre, precio, stock, this.stockMinimo);
    }

    public final void actualizarDatos(String nombre, BigDecimal precio, int stock, int stockMinimo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (precio == null || precio.signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock minimo no puede ser negativo");
        }
        this.nombre = nombre.trim();
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public boolean tieneStock(int cantidad) {
        return cantidad > 0 && stock >= cantidad;
    }

    public boolean estaBajoMinimo() {
        return stock <= stockMinimo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public int getStock() { return stock; }
    public int getStockMinimo() { return stockMinimo; }
    public Long getVersion() { return version; }
}
