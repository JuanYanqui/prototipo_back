package com.comercialnova.service;

import com.comercialnova.auth.SesionActiva;
import com.comercialnova.dto.LoginRequest;
import com.comercialnova.dto.LoginResponse;
import com.comercialnova.exception.CredencialesInvalidasException;
import com.comercialnova.model.Usuario;
import com.comercialnova.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final SesionService sesionService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       SesionService sesionService,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.sesionService = sesionService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername() == null ? "" : request.getUsername().trim();
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(CredencialesInvalidasException::new);

        if (!usuario.isActivo() || !passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException();
        }

        SesionActiva sesion = sesionService.crear(usuario);
        return new LoginResponse(sesion.getSessionId(), usuario);
    }

    public LoginResponse me(String sessionId) {
        SesionActiva sesion = sesionService.obtener(sessionId);
        Usuario usuario = usuarioRepository.findById(sesion.getUsuarioId())
                .orElseThrow(CredencialesInvalidasException::new);
        return new LoginResponse(sesion.getSessionId(), usuario);
    }

    public void logout(String sessionId) {
        sesionService.cerrar(sessionId);
    }
}
