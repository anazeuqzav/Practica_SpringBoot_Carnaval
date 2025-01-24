package com.iesvdc.carnaval.model;

public class Puntuacion {
    // Atributos
    private long id;
    private String fase; // cambiar a enum
    private double puntos;

    private Agrupacion agrupacion;

    // Constructores
    public Puntuacion() {
    }

    public Puntuacion(long id, String fase, double puntos, Agrupacion agrupacion) {
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

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public double getPuntos() {
        return puntos;
    }

    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }
}
