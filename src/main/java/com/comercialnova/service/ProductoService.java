package com.comercialnova.service;

import java.util.stream.Collectors;
import com.comercialnova.dto.ProductoRequest;
import com.comercialnova.dto.ProductoResponse;
import com.comercialnova.exception.ConflictoEdicionException;
import com.comercialnova.exception.RecursoNoEncontradoException;
import com.comercialnova.model.Producto;
import com.comercialnova.model.TipoMovimiento;
import com.comercialnova.repository.ProductoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final KardexService kardexService;

    public ProductoService(ProductoRepository productoRepository, KardexService kardexService) {
        this.productoRepository = productoRepository;
        this.kardexService = kardexService;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return productoRepository.findAllByOrderByNombreAsc().stream().map(ProductoResponse::de).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtener(Long id) {
        return ProductoResponse.de(buscar(id));
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        int stockMinimo = request.getStockMinimo() == null ? Producto.STOCK_MINIMO_POR_DEFECTO : request.getStockMinimo();
        Producto producto = productoRepository.saveAndFlush(
                new Producto(request.getNombre(), request.getPrecio(), request.getStock(), stockMinimo));
        kardexService.registrar(producto, TipoMovimiento.ENTRADA, producto.getStock(), 0, producto.getStock(),
                "Stock inicial", null);
        return ProductoResponse.de(producto);
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        if (request.getVersion() == null) {
            throw new IllegalArgumentException("Debe enviar la version del producto que esta editando");
        }
        Producto producto = buscar(id);

        if (!producto.getVersion().equals(request.getVersion())) {
            throw new ConflictoEdicionException("El producto '" + producto.getNombre()
                    + "' fue modificado por otro usuario o por una venta mientras usted lo editaba"
                    + " (su version: " + request.getVersion() + ", version actual: " + producto.getVersion()
                    + "). Recargue los datos y vuelva a intentar.");
        }

        int stockAnterior = producto.getStock();
        int stockMinimo = request.getStockMinimo() == null ? producto.getStockMinimo() : request.getStockMinimo();
        producto.actualizarDatos(request.getNombre(), request.getPrecio(), request.getStock(), stockMinimo);
        Producto guardado = productoRepository.saveAndFlush(producto);

        if (guardado.getStock() != stockAnterior) {
            kardexService.registrar(guardado, TipoMovimiento.AJUSTE, Math.abs(guardado.getStock() - stockAnterior),
                    stockAnterior, guardado.getStock(), "Ajuste manual de inventario", null);
        }
        return ProductoResponse.de(guardado);
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
    }
}
