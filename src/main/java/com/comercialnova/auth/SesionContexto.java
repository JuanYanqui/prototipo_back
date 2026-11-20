package com.comercialnova.auth;

public final class SesionContexto {

    private static final ThreadLocal<SesionActiva> ACTUAL = new ThreadLocal<SesionActiva>();

    private SesionContexto() {
    }

    public static void set(SesionActiva sesion) {
        ACTUAL.set(sesion);
    }

    public static SesionActiva get() {
        return ACTUAL.get();
    }

    public static void clear() {
        ACTUAL.remove();
    }
}
