package com.comercialnova.service;

import com.comercialnova.auth.SesionActiva;
import com.comercialnova.auth.SesionContexto;
import com.comercialnova.dto.MovimientoResponse;
import com.comercialnova.exception.RecursoNoEncontradoException;
import com.comercialnova.model.MovimientoInventario;
import com.comercialnova.model.Producto;
import com.comercialnova.model.TipoMovimiento;
import com.comercialnova.repository.MovimientoInventarioRepository;
import com.comercialnova.repository.ProductoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KardexService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    public KardexService(MovimientoInventarioRepository movimientoRepository,
                         ProductoRepository productoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
    }

    public void registrar(Producto producto, TipoMovimiento tipo, int cantidad, int stockAnterior,
                          int stockNuevo, String motivo, String responsablePorDefecto) {
        if (cantidad <= 0) {
            return;
        }
        movimientoRepository.save(new MovimientoInventario(producto, tipo, cantidad, stockAnterior,
                stockNuevo, motivo, responsable(responsablePorDefecto)));
    }

    @Transactional
    public MovimientoResponse ingresar(Long productoId, int cantidad, String motivo) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a ingresar debe ser mayor a cero");
        }
        if (productoRepository.aumentarStock(productoId, cantidad) == 0) {
            throw new RecursoNoEncontradoException("Producto", productoId);
        }
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", productoId));
        String detalle = motivo == null || motivo.trim().isEmpty() ? "Ingreso de mercaderia" : motivo.trim();
        MovimientoInventario movimiento = movimientoRepository.save(new MovimientoInventario(producto,
                TipoMovimiento.ENTRADA, cantidad, producto.getStock() - cantidad, producto.getStock(),
                detalle, responsable(null)));
        return MovimientoResponse.de(movimiento);
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> movimientos(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new RecursoNoEncontradoException("Producto", productoId);
        }
        return movimientoRepository.findTop200ByProductoIdOrderByFechaDescIdDesc(productoId).stream()
                .map(MovimientoResponse::de).collect(Collectors.toList());
    }

    private static String responsable(String porDefecto) {
        SesionActiva sesion = SesionContexto.get();
        if (sesion != null) {
            return sesion.getNombre();
        }
        return porDefecto == null || porDefecto.trim().isEmpty() ? "sistema" : porDefecto;
    }
}
