package com.iesvdc.carnaval.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "puntuaciones")
public class Puntuacion {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "fase")// cambiar a enum
    private Fase fase;

    @NotNull(message = "Los puntos no pueden estar vacíos")
    @Min(value = 0, message = "La puntuación debe ser mayor o igual a 0")
    @Max(value = 100, message = "La puntuación no puede superar los 100 puntos")
    @Column(name = "puntos")
    private double puntos;

    @ManyToOne
    @JoinColumn(name = "agrupacion_id")
    private Agrupacion agrupacion;

    // Constructores
    public Puntuacion() {
    }

    public Puntuacion(long id, Fase fase, double puntos, Agrupacion agrupacion) {
        this.id = id;
        this.fase = fase;
        this.puntos = puntos;
        this.agrupacion = agrupacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public Agrupacion getAgrupacion() {
        return agrupacion;
    }

    public void setAgrupacion(Agrupacion agrupacion) {
        this.agrupacion = agrupacion;
    }

    public double getPuntos() {
        return puntos;
    }

    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }
}
