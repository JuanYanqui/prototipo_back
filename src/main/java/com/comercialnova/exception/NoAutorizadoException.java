package com.comercialnova.exception;

public class NoAutorizadoException extends RuntimeException {

    private final boolean autenticado;

    public NoAutorizadoException(String mensaje, boolean autenticado) {
        super(mensaje);
        this.autenticado = autenticado;
    }

    public boolean isAutenticado() {
        return autenticado;
    }
}
