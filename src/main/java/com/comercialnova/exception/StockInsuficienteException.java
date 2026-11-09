package com.comercialnova.exception;

/** Se lanza cuando se intenta vender mas unidades de las disponibles. */
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String producto, int disponible, int solicitado) {
        super("Stock insuficiente para '" + producto + "': disponible " + disponible
                + ", solicitado " + solicitado);
    }
}
