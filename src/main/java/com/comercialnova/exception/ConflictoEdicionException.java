package com.comercialnova.exception;

/**
 * Se lanza cuando un usuario intenta guardar un producto con una version
 * antigua, es decir, otro usuario (o una venta) lo modifico despues de que
 * el primero lo abrio.
 */
public class ConflictoEdicionException extends RuntimeException {

    public ConflictoEdicionException(String mensaje) {
        super(mensaje);
    }
}
