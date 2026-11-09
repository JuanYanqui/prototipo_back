package com.comercialnova;

import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.comercialnova.dto.ItemVentaRequest;
import com.comercialnova.dto.VentaRequest;
import com.comercialnova.dto.VentaResponse;
import com.comercialnova.exception.RecursoNoEncontradoException;
import com.comercialnova.exception.StockInsuficienteException;
import com.comercialnova.model.Producto;
import com.comercialnova.repository.ClienteRepository;
import com.comercialnova.repository.ProductoRepository;
import com.comercialnova.repository.VentaRepository;
import com.comercialnova.service.VentaService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** Pruebas del flujo de venta: operaciones validas e invalidas. */
@SpringBootTest
class VentaServiceTest {

    @Autowired VentaService ventaService;
    @Autowired ProductoRepository productoRepository;
    @Autowired ClienteRepository clienteRepository;
    @Autowired VentaRepository ventaRepository;

    Long clienteId;

    @BeforeEach
    void setUp() {
        clienteId = clienteRepository.findAll().get(0).getId();
    }

    @Test
    @DisplayName("Venta valida: descuenta stock y calcula el total")
    void ventaValida() {
        Producto p = productoRepository.saveAndFlush(new Producto("Cable HDMI", new BigDecimal("2.50"), 10));

        VentaResponse venta = ventaService.registrarVenta(
                new VentaRequest(clienteId, "Vendedor 1", Arrays.asList(new ItemVentaRequest(p.getId(), 3))));

        assertThat(venta.getId()).isNotNull();
        assertThat(venta.getTotal()).isEqualByComparingTo("7.50");
        assertThat(stockDe(p.getId())).isEqualTo(7);
    }

    @Test
    @DisplayName("Venta invalida: sin stock suficiente se rechaza y el stock no cambia")
    void ventaSinStock() {
        Producto p = productoRepository.saveAndFlush(new Producto("Parlante", new BigDecimal("30.00"), 2));
        long ventasAntes = ventaRepository.count();

        assertThatThrownBy(() -> ventaService.registrarVenta(
                new VentaRequest(clienteId, "Vendedor 1", Arrays.asList(new ItemVentaRequest(p.getId(), 5)))))
                .isInstanceOf(StockInsuficienteException.class)
                .hasMessageContaining("disponible 2");

        assertThat(stockDe(p.getId())).isEqualTo(2);
        assertThat(ventaRepository.count()).isEqualTo(ventasAntes);
    }

    @Test
    @DisplayName("Venta todo-o-nada: si un producto no alcanza, no se descuenta ninguno")
    void ventaVariosProductosRollback() {
        Producto conStock = productoRepository.saveAndFlush(new Producto("Audifonos", new BigDecimal("15.00"), 10));
        Producto sinStock = productoRepository.saveAndFlush(new Producto("Webcam", new BigDecimal("40.00"), 1));

        assertThatThrownBy(() -> ventaService.registrarVenta(new VentaRequest(clienteId, "Vendedor 1", Arrays.asList(
                new ItemVentaRequest(conStock.getId(), 2),
                new ItemVentaRequest(sinStock.getId(), 5)))))
                .isInstanceOf(StockInsuficienteException.class);

        assertThat(stockDe(conStock.getId())).isEqualTo(10);
        assertThat(stockDe(sinStock.getId())).isEqualTo(1);
    }

    @Test
    @DisplayName("Venta invalida: cantidad cero se rechaza")
    void cantidadCero() {
        Producto p = productoRepository.saveAndFlush(new Producto("Pendrive", new BigDecimal("8.00"), 5));

        assertThatThrownBy(() -> ventaService.registrarVenta(
                new VentaRequest(clienteId, "Vendedor 1", Arrays.asList(new ItemVentaRequest(p.getId(), 0)))))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(stockDe(p.getId())).isEqualTo(5);
    }

    @Test
    @DisplayName("Venta invalida: producto inexistente")
    void productoInexistente() {
        assertThatThrownBy(() -> ventaService.registrarVenta(
                new VentaRequest(clienteId, "Vendedor 1", Arrays.asList(new ItemVentaRequest(999_999L, 1)))))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    private int stockDe(Long id) {
        return productoRepository.findById(id).get().getStock();
    }
}
