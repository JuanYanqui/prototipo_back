package com.comercialnova;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import com.comercialnova.model.Producto;
import com.comercialnova.repository.ClienteRepository;
import com.comercialnova.repository.ProductoRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class VentaApiTest {

    @Autowired MockMvc mvc;
    @Autowired ProductoRepository productoRepository;
    @Autowired ClienteRepository clienteRepository;

    @Test
    @DisplayName("POST /api/ventas valido responde 201 con el total")
    void ventaValida() throws Exception {
        Producto p = productoRepository.saveAndFlush(new Producto("Mousepad", new BigDecimal("5.00"), 10));
        mvc.perform(post("/api/ventas").contentType(MediaType.APPLICATION_JSON).content(json(p.getId(), 2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total").value(10.00));
    }

    @Test
    @DisplayName("POST /api/ventas sin stock responde 409 con mensaje claro")
    void ventaSinStock() throws Exception {
        Producto p = productoRepository.saveAndFlush(new Producto("Disco SSD", new BigDecimal("60.00"), 1));
        mvc.perform(post("/api/ventas").contentType(MediaType.APPLICATION_JSON).content(json(p.getId(), 3)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje", containsString("Stock insuficiente")));
    }

    @Test
    @DisplayName("POST /api/ventas con cantidad 0 responde 400")
    void cantidadInvalida() throws Exception {
        Producto p = productoRepository.saveAndFlush(new Producto("Hub USB", new BigDecimal("12.00"), 5));
        mvc.perform(post("/api/ventas").contentType(MediaType.APPLICATION_JSON).content(json(p.getId(), 0)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje", containsString("mayor a cero")));
    }

    private String json(Long productoId, int cantidad) {
        Long clienteId = clienteRepository.findAll().get(0).getId();
        return String.format(
                "{\"clienteId\": %d, \"vendedor\": \"Vendedor API\","
                + " \"items\": [{\"productoId\": %d, \"cantidad\": %d}]}",
                clienteId, productoId, cantidad);
    }
}
