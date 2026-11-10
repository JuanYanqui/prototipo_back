package com.comercialnova.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private int stock;

    @Version
    private Long version;

    protected Producto() {
    }

    public Producto(String nombre, BigDecimal precio, int stock) {
        actualizarDatos(nombre, precio, stock);
    }

    public final void actualizarDatos(String nombre, BigDecimal precio, int stock) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (precio == null || precio.signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.nombre = nombre.trim();
        this.precio = precio;
        this.stock = stock;
    }

    public boolean tieneStock(int cantidad) {
        return cantidad > 0 && stock >= cantidad;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public int getStock() { return stock; }
    public Long getVersion() { return version; }
}
