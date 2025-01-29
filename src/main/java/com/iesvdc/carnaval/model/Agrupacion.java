package com.iesvdc.carnaval.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agrupaciones")
public class Agrupacion {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "El nombre es obligatorio")
    @Column(name = "nombre")
    private String nombre;

    @NotNull(message = "La modalidad es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "modalidad")
    private Modalidad modalidad;

    @Column(name = "numero_de_componentes")
    private Integer numeroDeComponentes = 0;

    @Column(name = "localidad")
    private String localidad;

    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "director_id", unique = true)
    private Componente director; // relación 1 a 1

    @OneToMany(mappedBy = "agrupacion", cascade = CascadeType.ALL)
    private List<Componente> componentes = new ArrayList<>(); // Relacion 1 a muchos

    @OneToMany(mappedBy = "agrupacion", cascade = CascadeType.ALL)
    private List<Puntuacion> puntuaciones = new ArrayList<>(); // Relación 1 a muchos

    private Double puntuacionTotal;


    // Constructores
    public Agrupacion() {
    }

    // Sin listas
    public Agrupacion(long id, String nombre, Modalidad modalidad, Integer numeroDeComponentes, String localidad, Componente director) {
        this.id = id;
        this.nombre = nombre;
        this.modalidad = modalidad;
        this.numeroDeComponentes = numeroDeComponentes;
        this.localidad = localidad;
        this.director = director;
    }

    // Completo
    public Agrupacion(long id, String nombre, Modalidad modalidad, Integer numeroDeComponentes, String localidad, Componente director, List<Componente> componentes, List<Puntuacion> puntuaciones) {
        this.id = id;
        this.nombre = nombre;
        this.modalidad = modalidad;
        this.numeroDeComponentes = numeroDeComponentes;
        this.localidad = localidad;
        this.director = director;
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

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
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

    public List<Componente> getComponentes() {
        return componentes;
    }

    public void setComponentes(List<Componente> componentes) {
        this.componentes = componentes;
    }

    public List<Puntuacion> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(List<Puntuacion> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public Double getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public void setPuntuacionTotal(Double puntuacionTotal) {
        this.puntuacionTotal = puntuacionTotal;
    }

    public int getCapacidadMaxima() {
        switch (modalidad) {
            case Chirigota:
                return 12;
            case Comparsa:
                return 15;
            case Coro:
                return 45;
            case Cuarteto:
                return 4;
            default:
                return 0;
        }
    }
}
