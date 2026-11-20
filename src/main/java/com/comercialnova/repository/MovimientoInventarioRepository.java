package com.comercialnova.repository;

import com.comercialnova.model.MovimientoInventario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findTop200ByProductoIdOrderByFechaDescIdDesc(Long productoId);
}
