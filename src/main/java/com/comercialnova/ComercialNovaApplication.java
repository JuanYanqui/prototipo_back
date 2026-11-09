package com.comercialnova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del prototipo.
 * Al ejecutar, levanta el servidor en http://localhost:8090
 */
@SpringBootApplication
public class ComercialNovaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComercialNovaApplication.class, args);
    }
}
