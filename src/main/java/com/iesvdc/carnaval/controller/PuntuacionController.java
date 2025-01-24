package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.service.PuntuacionService;

/**
 * Manejo de las peticions HTTP
 */
public class PuntuacionController {

    private final PuntuacionService puntuacionService;

    public PuntuacionController(PuntuacionService puntuacionService) {
        this.puntuacionService = puntuacionService;
    }
}
