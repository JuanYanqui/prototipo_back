package com.comercialnova.controller;

import com.comercialnova.dto.VentaRequest;
import com.comercialnova.dto.VentaResponse;
import com.comercialnova.service.VentaService;
import javax.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST de ventas (la "C" de MVC).
 * Solo recibe la peticion, delega en el servicio y devuelve la respuesta;
 * no contiene reglas de negocio. Lo mismo lo puede usar la web o una app movil.
 */
@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VentaResponse registrar(@Valid @RequestBody VentaRequest request) {
        return ventaService.registrarVenta(request);
    }

    @GetMapping
    public List<VentaResponse> ultimas() {
        return ventaService.ultimasVentas();
    }
}
