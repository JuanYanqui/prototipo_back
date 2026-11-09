package com.comercialnova.repository;

import com.comercialnova.model.Venta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findTop20ByOrderByFechaDesc();
}
