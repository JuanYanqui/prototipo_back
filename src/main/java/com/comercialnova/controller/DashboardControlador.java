package com.comercialnova.controller;

import com.comercialnova.dto.Dashboard;
import com.comercialnova.model.Producto;
import com.comercialnova.repository.ProductoRepository;
import com.comercialnova.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardControlador {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @GetMapping
    public Dashboard obtenerDatosDashboard() {
        List<Producto> productos = productoRepository.findAll();
        Dashboard dto = new Dashboard();

        for (Producto p : productos) {
            dto.getNombresProductos().add(p.getNombre());
            dto.getStocksProductos().add(p.getStock());
            if (p.getStock() == 0) dto.setStockAgotado(dto.getStockAgotado() + 1);
            else if (p.estaBajoMinimo()) dto.setStockBajo(dto.getStockBajo() + 1);
            else dto.setStockNormal(dto.getStockNormal() + 1);
        }

        for (Object[] fila : ventaRepository.productosMasVendidos(PageRequest.of(0, 5))) {
            dto.getNombresTopProductos().add((String) fila[0]);
            dto.getCantidadesVendidas().add(((Number) fila[1]).intValue());
        }

        return dto;
    }
}
