package com.iesvdc.carnaval.model;

import java.util.List;

public class Agrupacion {
    // Atributos
    private long id;
    private String nombre;
    private String modalidad; // cambiar a enum
    private Integer numeroDeComponentes;
    private String localidad;

    private Componente director; // relación 1 a 1

    private List<Componente> componentes; // Relacion 1 a muchos

    private List<Puntuacion> puntuaciones; // Relación 1 a muchos


    // Constructores
    public Agrupacion() {
    }

    // Sin listas
    public Agrupacion(long id, String nombre, Componente director, String modalidad, Integer numeroDeComponentes, String localidad) {
        this.id = id;
        this.nombre = nombre;
        this.director = director;
        this.modalidad = modalidad;
        this.numeroDeComponentes = numeroDeComponentes;
        this.localidad = localidad;
    }

    // Completo
    public Agrupacion(long id, String nombre, Componente director, String modalidad, Integer numeroDeComponentes, String localidad, List<Componente> componentes, List<Puntuacion> puntuaciones) {
        this.id = id;
        this.nombre = nombre;
        this.director = director;
        this.modalidad = modalidad;
        this.numeroDeComponentes = numeroDeComponentes;
        this.localidad = localidad;
        this.componentes = componentes;
        this.puntuaciones = puntuaciones;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Componente getDirector() {
        return director;
    }

    public void setDirector(Componente director) {
        this.director = director;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public Integer getNumeroDeComponentes() {
        return numeroDeComponentes;
    }

    public void setNumeroDeComponentes(Integer numeroDeComponentes) {
        this.numeroDeComponentes = numeroDeComponentes;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
}
