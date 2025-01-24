package com.iesvdc.carnaval.service;

import com.iesvdc.carnaval.repository.ComponenteRepository;

public class ComponenteService {

    private final ComponenteRepository componenteRepository;

    // Constructor
    public ComponenteService(ComponenteRepository componenteRepository) {
        this.componenteRepository = componenteRepository;
    }
}
