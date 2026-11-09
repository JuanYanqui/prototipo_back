package com.comercialnova.controller;

import com.comercialnova.dto.ApiError;
import com.comercialnova.exception.ConflictoEdicionException;
import com.comercialnova.exception.RecursoNoEncontradoException;
import com.comercialnova.exception.StockInsuficienteException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones de negocio a respuestas HTTP claras.
 * Todos los errores salen con el mismo formato (ApiError).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ApiError> stockInsuficiente(StockInsuficienteException ex) {
        return error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ConflictoEdicionException.class)
    public ResponseEntity<ApiError> conflictoEdicion(ConflictoEdicionException ex) {
        return error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> bloqueoOptimista(ObjectOptimisticLockingFailureException ex) {
        return error(HttpStatus.CONFLICT,
                "Otro usuario guardo cambios en este registro al mismo tiempo. Recargue los datos y vuelva a intentar.");
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> noEncontrado(RecursoNoEncontradoException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getAllErrors().stream()
                .map(e -> e.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));
        return error(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> argumentoInvalido(IllegalArgumentException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> jsonInvalido(HttpMessageNotReadableException ex) {
        return error(HttpStatus.BAD_REQUEST, "El cuerpo de la peticion no es un JSON valido");
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(new ApiError(status.value(), status.getReasonPhrase(), mensaje));
    }
}
