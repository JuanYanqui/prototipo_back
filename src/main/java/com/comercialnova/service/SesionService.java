package com.comercialnova.service;

import com.comercialnova.auth.SesionActiva;
import com.comercialnova.exception.NoAutorizadoException;
import com.comercialnova.model.Usuario;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class SesionService {

    private final Map<String, SesionActiva> sesiones = new ConcurrentHashMap<String, SesionActiva>();

    public SesionActiva crear(Usuario usuario) {
        String sessionId = UUID.randomUUID().toString();
        SesionActiva sesion = new SesionActiva(sessionId, usuario);
        sesiones.put(sessionId, sesion);
        return sesion;
    }

    public SesionActiva obtener(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new NoAutorizadoException("Debe iniciar sesion", false);
        }
        SesionActiva sesion = sesiones.get(sessionId.trim());
        if (sesion == null) {
            throw new NoAutorizadoException("Sesion invalida o expirada. Vuelva a iniciar sesion", false);
        }
        return sesion;
    }

    public void cerrar(String sessionId) {
        if (sessionId != null) {
            sesiones.remove(sessionId.trim());
        }
    }
}
