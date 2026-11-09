package com.comercialnova.dto;

import com.comercialnova.model.Producto;
import java.math.BigDecimal;

public class ProductoResponse {

    private final Long id;
    private final String nombre;
    private final BigDecimal precio;
    private final int stock;
    private final Long version;

    public ProductoResponse(Long id, String nombre, BigDecimal precio, int stock, Long version) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.version = version;
    }

    public static ProductoResponse de(Producto p) {
        return new ProductoResponse(p.getId(), p.getNombre(), p.getPrecio(), p.getStock(), p.getVersion());
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public int getStock() { return stock; }
    public Long getVersion() { return version; }
}
