package com.comercialnova.config;

import com.comercialnova.auth.SesionActiva;
import com.comercialnova.auth.SesionContexto;
import com.comercialnova.controller.AuthController;
import com.comercialnova.exception.NoAutorizadoException;
import com.comercialnova.service.SesionService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final SesionService sesionService;
    private final boolean authEnabled;

    public AuthInterceptor(SesionService sesionService,
                           @Value("${comercialnova.auth.enabled:true}") boolean authEnabled) {
        this.sesionService = sesionService;
        this.authEnabled = authEnabled;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!authEnabled || HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/login")) {
            return true;
        }

        String sessionId = request.getHeader(AuthController.HEADER_SESION);
        SesionActiva sesion = sesionService.obtener(sessionId);
        SesionContexto.set(sesion);

        if (requiereAdmin(request) && !sesion.esAdmin()) {
            throw new NoAutorizadoException(
                    "No tiene permiso para esta operacion. Se requiere perfil ADMIN", true);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        SesionContexto.clear();
    }

    private boolean requiereAdmin(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        if (!path.startsWith("/api/productos")) {
            return false;
        }
        return HttpMethod.POST.matches(method) || HttpMethod.PUT.matches(method)
                || HttpMethod.DELETE.matches(method);
    }
}
