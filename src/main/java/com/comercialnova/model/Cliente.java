package com.comercialnova.model;

import javax.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 13)
    private String identificacion;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 120)
    private String email;

    protected Cliente() {
    }

    public Cliente(String identificacion, String nombre, String email) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La identificacion es obligatoria");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        this.identificacion = identificacion.trim();
        this.nombre = nombre.trim();
        this.email = email;
    }

    public Long getId() { return id; }
    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
}
