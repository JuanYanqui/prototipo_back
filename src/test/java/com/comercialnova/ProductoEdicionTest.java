package com.comercialnova;

import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.comercialnova.dto.ItemVentaRequest;
import com.comercialnova.dto.ProductoRequest;
import com.comercialnova.dto.ProductoResponse;
import com.comercialnova.dto.VentaRequest;
import com.comercialnova.exception.ConflictoEdicionException;
import com.comercialnova.repository.ClienteRepository;
import com.comercialnova.service.ProductoService;
import com.comercialnova.service.VentaService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** Problema #2 del caso: dos empleados editan el mismo producto. */
@SpringBootTest
class ProductoEdicionTest {

    @Autowired ProductoService productoService;
    @Autowired VentaService ventaService;
    @Autowired ClienteRepository clienteRepository;

    @Test
    @DisplayName("Dos empleados abren el mismo producto: el segundo en guardar es rechazado")
    void edicionSimultanea() {
        ProductoResponse original = productoService.crear(
                new ProductoRequest("Impresora", new BigDecimal("120.00"), 4, null));
        Long versionVista = original.getVersion(); // ambos empleados ven esta version

        // Empleado A guarda primero: OK
        ProductoResponse deA = productoService.actualizar(original.getId(),
                new ProductoRequest("Impresora", new BigDecimal("115.00"), 4, versionVista));
        assertThat(deA.getVersion()).isGreaterThan(versionVista);

        // Empleado B guarda con la version vieja: RECHAZADO, no pisa el cambio de A
        assertThatThrownBy(() -> productoService.actualizar(original.getId(),
                new ProductoRequest("Impresora", new BigDecimal("130.00"), 4, versionVista)))
                .isInstanceOf(ConflictoEdicionException.class);

        assertThat(productoService.obtener(original.getId()).getPrecio()).isEqualByComparingTo("115.00");
    }

    @Test
    @DisplayName("Una venta mientras se edita invalida la edicion (no se revive stock vendido)")
    void ventaDuranteEdicion() {
        ProductoResponse original = productoService.crear(
                new ProductoRequest("Router", new BigDecimal("45.00"), 10, null));
        Long versionVista = original.getVersion();

        Long clienteId = clienteRepository.findAll().get(0).getId();
        ventaService.registrarVenta(new VentaRequest(clienteId, "Vendedor 1",
                Arrays.asList(new ItemVentaRequest(original.getId(), 1))));

        // El administrador guarda el formulario que abrio antes de la venta (stock 10)
        assertThatThrownBy(() -> productoService.actualizar(original.getId(),
                new ProductoRequest("Router", new BigDecimal("45.00"), 10, versionVista)))
                .isInstanceOf(ConflictoEdicionException.class);

        assertThat(productoService.obtener(original.getId()).getStock()).isEqualTo(9);
    }
}
