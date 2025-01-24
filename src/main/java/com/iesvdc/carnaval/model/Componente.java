package com.iesvdc.carnaval.model;

public class Componente {
    // Atributos
    private long id;
    private String nombre;
    private Integer edad;
    private String rol;
    private String instrumento; // opcional

    private Agrupacion agrupacion; // relacion muchos a 1

    // Constructores
    public Componente() {
    }

    public Componente(long id, String nombre, Integer edad, String rol, String instrumento, Agrupacion agrupacion) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.rol = rol;
        this.instrumento = instrumento;
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
}
