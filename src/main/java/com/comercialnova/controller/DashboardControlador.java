package com.comercialnova.controller;

import com.comercialnova.dto.Dashboard; 
import com.comercialnova.model.Producto; 
import com.comercialnova.repository.ProductoRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardControlador {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public Dashboard obtenerDatosDashboard() { 
        List<Producto> productos = productoRepository.findAll();
        Dashboard dto = new Dashboard(); 

        for (Producto p : productos) {
            dto.getNombresProductos().add(p.getNombre());
            dto.getStocksProductos().add(p.getStock());
            if (p.getStock() == 0) dto.setStockAgotado(dto.getStockAgotado() + 1);
            else if (p.getStock() < 10) dto.setStockBajo(dto.getStockBajo() + 1);
            else dto.setStockNormal(dto.getStockNormal() + 1);
        }

        // Datos simulados para la pestaña de ventas
        dto.getNombresTopProductos().addAll(java.util.Arrays.asList("Mouse inalambrico", "Teclado mecanico", "Monitor 24\"", "Laptop Nova 14\""));
        dto.getCantidadesVendidas().addAll(java.util.Arrays.asList(50, 35, 20, 10));

        return dto;
    }
}