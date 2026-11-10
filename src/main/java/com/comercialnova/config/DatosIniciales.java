package com.comercialnova.config;

import java.util.Arrays;
import com.comercialnova.model.Cliente;
import com.comercialnova.model.Producto;
import com.comercialnova.repository.ClienteRepository;
import com.comercialnova.repository.ProductoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    public DatosIniciales(ProductoRepository productoRepository, ClienteRepository clienteRepository) {
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) {
        if (productoRepository.count() == 0) {
            productoRepository.saveAll(Arrays.asList(
                    new Producto("Laptop Nova 14\"", new BigDecimal("749.90"), 5),
                    new Producto("Mouse inalambrico", new BigDecimal("18.50"), 40),
                    new Producto("Monitor 24\" Full HD", new BigDecimal("159.00"), 8),
                    new Producto("Teclado mecanico", new BigDecimal("59.99"), 12),
                    new Producto("Tablet Nova 10 (ultima unidad)", new BigDecimal("299.00"), 1)));
        }
        if (clienteRepository.count() == 0) {
            clienteRepository.saveAll(Arrays.asList(
                    new Cliente("0900000001", "Consumidor Final", null),
                    new Cliente("0911111111", "Ana Torres", "ana.torres@ejemplo.com"),
                    new Cliente("0922222222", "Luis Mendoza", "luis.mendoza@ejemplo.com")));
        }
    }
}
