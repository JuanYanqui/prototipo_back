package com.comercialnova;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.comercialnova.dto.ItemVentaRequest;
import com.comercialnova.dto.MovimientoResponse;
import com.comercialnova.dto.ProductoRequest;
import com.comercialnova.dto.ProductoResponse;
import com.comercialnova.dto.VentaRequest;
import com.comercialnova.exception.StockInsuficienteException;
import com.comercialnova.repository.ClienteRepository;
import com.comercialnova.service.KardexService;
import com.comercialnova.service.ProductoService;
import com.comercialnova.service.VentaService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KardexTest {

    @Autowired ProductoService productoService;
    @Autowired VentaService ventaService;
    @Autowired KardexService kardexService;
    @Autowired ClienteRepository clienteRepository;

    private ProductoResponse crear(String nombre, int stock, Integer stockMinimo) {
        return productoService.crear(new ProductoRequest(nombre, new BigDecimal("10.00"), stock, stockMinimo, null));
    }

    private void vender(Long productoId, int cantidad) {
        Long clienteId = clienteRepository.findAll().get(0).getId();
        ventaService.registrarVenta(new VentaRequest(clienteId, "Vendedor Kardex",
                Arrays.asList(new ItemVentaRequest(productoId, cantidad))));
    }

    @Test
    @DisplayName("Crear un producto con stock registra la ENTRADA de stock inicial")
    void stockInicial() {
        ProductoResponse p = crear("Kardex inicial", 8, null);

        List<MovimientoResponse> movimientos = kardexService.movimientos(p.getId());
        assertThat(movimientos).hasSize(1);
        assertThat(movimientos.get(0).getTipo()).isEqualTo("ENTRADA");
        assertThat(movimientos.get(0).getStockAnterior()).isZero();
        assertThat(movimientos.get(0).getStockNuevo()).isEqualTo(8);
    }

    @Test
    @DisplayName("Cada venta registra una SALIDA con el stock anterior y el nuevo")
    void ventaRegistraSalida() {
        ProductoResponse p = crear("Kardex venta", 10, null);
        vender(p.getId(), 3);

        MovimientoResponse ultimo = kardexService.movimientos(p.getId()).get(0);
        assertThat(ultimo.getTipo()).isEqualTo("SALIDA");
        assertThat(ultimo.getCantidad()).isEqualTo(3);
        assertThat(ultimo.getStockAnterior()).isEqualTo(10);
        assertThat(ultimo.getStockNuevo()).isEqualTo(7);
        assertThat(ultimo.getMotivo()).startsWith("Venta #");
    }

    @Test
    @DisplayName("Una venta rechazada por stock no deja movimiento")
    void ventaFallidaSinMovimiento() {
        ProductoResponse p = crear("Kardex sin stock", 1, null);

        assertThatThrownBy(() -> vender(p.getId(), 5)).isInstanceOf(StockInsuficienteException.class);
        assertThat(kardexService.movimientos(p.getId())).hasSize(1);
    }

    @Test
    @DisplayName("El ingreso de mercaderia suma stock y registra una ENTRADA")
    void ingresoDeMercaderia() {
        ProductoResponse p = crear("Kardex ingreso", 2, null);
        kardexService.ingresar(p.getId(), 8, "Compra a proveedor");

        assertThat(productoService.obtener(p.getId()).getStock()).isEqualTo(10);
        MovimientoResponse ultimo = kardexService.movimientos(p.getId()).get(0);
        assertThat(ultimo.getTipo()).isEqualTo("ENTRADA");
        assertThat(ultimo.getStockAnterior()).isEqualTo(2);
        assertThat(ultimo.getStockNuevo()).isEqualTo(10);
        assertThat(ultimo.getMotivo()).isEqualTo("Compra a proveedor");
    }

    @Test
    @DisplayName("Editar el stock a mano registra un AJUSTE")
    void ajusteManual() {
        ProductoResponse p = crear("Kardex ajuste", 5, null);
        productoService.actualizar(p.getId(),
                new ProductoRequest(p.getNombre(), p.getPrecio(), 3, null, p.getVersion()));

        MovimientoResponse ultimo = kardexService.movimientos(p.getId()).get(0);
        assertThat(ultimo.getTipo()).isEqualTo("AJUSTE");
        assertThat(ultimo.getCantidad()).isEqualTo(2);
        assertThat(ultimo.getStockAnterior()).isEqualTo(5);
        assertThat(ultimo.getStockNuevo()).isEqualTo(3);
    }

    @Test
    @DisplayName("El producto avisa cuando su stock esta en o bajo el minimo")
    void stockMinimo() {
        ProductoResponse bajo = crear("Kardex minimo bajo", 3, 4);
        ProductoResponse normal = crear("Kardex minimo normal", 20, 4);

        assertThat(bajo.getStockMinimo()).isEqualTo(4);
        assertThat(bajo.isBajoMinimo()).isTrue();
        assertThat(normal.isBajoMinimo()).isFalse();
    }
}
