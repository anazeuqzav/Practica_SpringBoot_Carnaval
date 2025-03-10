package com.iesvdc.carnaval.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "componentes")
public class Componente {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 100, message = "La edad no puede ser mayor de 100")
    @Column(name = "edad")
    private Integer edad;

    @Column(name = "rol")
    private String rol;

    @Column(name = "instrumento")
    private String instrumento = null; // opcional

    @OneToOne(mappedBy = "director")
    private Agrupacion agrupacionDirigida;

    @ManyToOne
    @JoinColumn(name = "agrupacion_id")
    private Agrupacion agrupacion;

    // Constructores
    public Componente() {
    }


    public Componente(long id, String nombre, int edad, String rol, String instrumento, Agrupacion agrupacion) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.rol = rol;
        this.instrumento = instrumento;
        this.agrupacion = agrupacion;
    }


    public Componente(long id, String nombre, Integer edad, String rol, String instrumento, Agrupacion agrupacionDirigida, Agrupacion agrupacion) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.rol = rol;
        this.instrumento = instrumento;
        this.agrupacionDirigida = agrupacionDirigida;
        this.agrupacion = agrupacion;
    }

    public Componente(long id, String nombre, String rol, Agrupacion agrupacion) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.agrupacion = agrupacion;
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

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }

    public Agrupacion getAgrupacionDirigida() {
        return agrupacionDirigida;
    }

    public void setAgrupacionDirigida(Agrupacion agrupacionDirigida) {
        this.agrupacionDirigida = agrupacionDirigida;
    }

    public Agrupacion getAgrupacion() {
        return agrupacion;
    }

    public void setAgrupacion(Agrupacion agrupacion) {
        this.agrupacion = agrupacion;
    }

    @Override
    public String toString() {
        return "Componente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", rol='" + rol + '\'' +
                ", instrumento='" + instrumento + '\'' +
                '}';
    }
}
