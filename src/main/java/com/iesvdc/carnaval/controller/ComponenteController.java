package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.service.ComponenteService;

/**
 * Manejo de las peticions HTTP
 */
public class ComponenteController {

    private final ComponenteService componenteService;

    public ComponenteController(ComponenteService componenteService) {
        this.componenteService = componenteService;
    }
}
