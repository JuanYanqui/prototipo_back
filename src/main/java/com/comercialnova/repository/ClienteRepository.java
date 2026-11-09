package com.comercialnova.repository;

import com.comercialnova.model.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAllByOrderByNombreAsc();
}
