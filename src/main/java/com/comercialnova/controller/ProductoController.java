package com.comercialnova.controller;

import com.comercialnova.dto.IngresoRequest;
import com.comercialnova.dto.MovimientoResponse;
import com.comercialnova.dto.ProductoRequest;
import com.comercialnova.dto.ProductoResponse;
import com.comercialnova.service.KardexService;
import com.comercialnova.service.ProductoService;
import javax.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final KardexService kardexService;

    public ProductoController(ProductoService productoService, KardexService kardexService) {
        this.productoService = productoService;
        this.kardexService = kardexService;
    }

    @GetMapping
    public List<ProductoResponse> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable Long id) {
        return productoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse crear(@Valid @RequestBody ProductoRequest request) {
        return productoService.crear(request);
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoService.actualizar(id, request);
    }

    @PostMapping("/{id}/ingresos")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponse ingresar(@PathVariable Long id, @Valid @RequestBody IngresoRequest request) {
        return kardexService.ingresar(id, request.getCantidad(), request.getMotivo());
    }

    @GetMapping("/{id}/movimientos")
    public List<MovimientoResponse> movimientos(@PathVariable Long id) {
        return kardexService.movimientos(id);
    }
}
