package com.comercialnova.service;

import java.util.stream.Collectors;
import com.comercialnova.dto.ItemVentaRequest;
import com.comercialnova.dto.VentaRequest;
import com.comercialnova.dto.VentaResponse;
import com.comercialnova.exception.RecursoNoEncontradoException;
import com.comercialnova.exception.StockInsuficienteException;
import com.comercialnova.model.Cliente;
import com.comercialnova.model.Producto;
import com.comercialnova.model.Venta;
import com.comercialnova.repository.ClienteRepository;
import com.comercialnova.repository.ProductoRepository;
import com.comercialnova.repository.VentaRepository;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    public VentaService(VentaRepository ventaRepository,
                        ProductoRepository productoRepository,
                        ClienteRepository clienteRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public VentaResponse registrarVenta(VentaRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un producto");
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", request.getClienteId()));

        Venta venta = new Venta(cliente, request.getVendedor());

        for (Map.Entry<Long, Integer> item : agruparPorProducto(request.getItems()).entrySet()) {
            Long productoId = item.getKey();
            int cantidad = item.getValue();

            int filasActualizadas = productoRepository.descontarStock(productoId, cantidad);
            if (filasActualizadas == 0) {
                Producto producto = productoRepository.findById(productoId)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Producto", productoId));
                throw new StockInsuficienteException(producto.getNombre(), producto.getStock(), cantidad);
            }

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto", productoId));
            venta.agregarDetalle(producto, cantidad);
        }

        return VentaResponse.de(ventaRepository.save(venta));
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> ultimasVentas() {
        return ventaRepository.findTop20ByOrderByFechaDesc().stream().map(VentaResponse::de).collect(Collectors.toList());
    }

    private Map<Long, Integer> agruparPorProducto(List<ItemVentaRequest> items) {
        Map<Long, Integer> agrupado = new TreeMap<>();
        for (ItemVentaRequest item : items) {
            if (item.getProductoId() == null) {
                throw new IllegalArgumentException("Cada item debe indicar el producto");
            }
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
            }
            agrupado.merge(item.getProductoId(), item.getCantidad(), Integer::sum);
        }
        return agrupado;
    }
}
