package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.service.AgrupacionService;

/**
 * Manejo de las peticions HTTP
 */

public class AgrupacionController {

    private final AgrupacionService agrupacionService;

    public AgrupacionController(AgrupacionService agrupacionService) {
        this.agrupacionService = agrupacionService;
    }
}
