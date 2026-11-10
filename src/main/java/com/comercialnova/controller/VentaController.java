package com.comercialnova.controller;

import com.comercialnova.dto.VentaRequest;
import com.comercialnova.dto.VentaResponse;
import com.comercialnova.service.VentaService;
import javax.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
