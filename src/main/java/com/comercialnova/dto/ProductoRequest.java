package com.comercialnova.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * Datos para crear o editar un producto.
 * En la edicion, {@code version} es la version que el usuario vio al abrir
 * el formulario; si ya cambio, la edicion se rechaza.
 */
public class ProductoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    private Long version;

    /** Requerido por Jackson para leer el JSON. */
    public ProductoRequest() { }

    public ProductoRequest(String nombre, BigDecimal precio, Integer stock, Long version) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.version = version;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
