package com.comercialnova.repository;

import com.comercialnova.model.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Acceso a datos de productos (abstraccion: el resto del sistema no sabe
 * si debajo hay H2, PostgreSQL u otra base).
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findAllByOrderByNombreAsc();

    /**
     * DESCUENTO ATOMICO DE STOCK (solucion al problema #1 del caso).
     *
     * La validacion "hay stock suficiente" y el descuento se hacen en UNA
     * sola sentencia SQL. La base de datos bloquea la fila mientras la
     * actualiza, asi que si 20 vendedores intentan vender la ultima unidad
     * al mismo tiempo, solo uno logra que la condicion {@code stock >= :cantidad}
     * sea verdadera; los otros 19 reciben 0 filas afectadas.
     *
     * Tambien incrementa la version, para que una edicion del producto que
     * se haya abierto antes de esta venta sea rechazada (evita que un
     * administrador "reviva" stock ya vendido).
     *
     * @return 1 si se desconto, 0 si no habia stock suficiente o no existe
     */
    @Modifying(flushAutomatically = true)
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad, p.version = p.version + 1 "
         + "WHERE p.id = :id AND p.stock >= :cantidad")
    int descontarStock(@Param("id") Long id, @Param("cantidad") int cantidad);
}
