package com.comercialnova.service;

import java.util.stream.Collectors;
import com.comercialnova.dto.ClienteResponse;
import com.comercialnova.repository.ClienteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAllByOrderByNombreAsc().stream().map(ClienteResponse::de).collect(Collectors.toList());
    }
}
