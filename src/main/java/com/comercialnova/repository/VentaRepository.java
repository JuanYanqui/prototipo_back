package com.comercialnova.repository;

import com.comercialnova.model.Venta;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findTop20ByOrderByFechaDesc();

    @Query("SELECT d.producto.nombre, SUM(d.cantidad) FROM DetalleVenta d "
         + "GROUP BY d.producto.id, d.producto.nombre ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> productosMasVendidos(Pageable pageable);
}
