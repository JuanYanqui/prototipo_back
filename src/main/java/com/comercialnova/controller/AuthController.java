package com.comercialnova.controller;

import com.comercialnova.dto.LoginRequest;
import com.comercialnova.dto.LoginResponse;
import com.comercialnova.service.AuthService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static final String HEADER_SESION = "X-Session-Id";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader(value = HEADER_SESION, required = false) String sessionId) {
        authService.logout(sessionId);
    }

    @GetMapping("/me")
    public LoginResponse me(@RequestHeader(value = HEADER_SESION, required = false) String sessionId) {
        return authService.me(sessionId);
    }
}
