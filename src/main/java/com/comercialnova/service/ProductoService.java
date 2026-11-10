package com.comercialnova.service;

import java.util.stream.Collectors;
import com.comercialnova.dto.ProductoRequest;
import com.comercialnova.dto.ProductoResponse;
import com.comercialnova.exception.ConflictoEdicionException;
import com.comercialnova.exception.RecursoNoEncontradoException;
import com.comercialnova.model.Producto;
import com.comercialnova.repository.ProductoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
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
        Producto producto = new Producto(request.getNombre(), request.getPrecio(), request.getStock());
        return ProductoResponse.de(productoRepository.saveAndFlush(producto));
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

        producto.actualizarDatos(request.getNombre(), request.getPrecio(), request.getStock());
        return ProductoResponse.de(productoRepository.saveAndFlush(producto));
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
    }
}
