package com.iesvdc.carnaval.service;

import com.iesvdc.carnaval.repository.AgrupacionRepository;

/**
 * Logica de negocio: CRUD
 *
 */
public class AgrupacionService {

    private final AgrupacionRepository agrupacionRepository;

    // Constructor
    public AgrupacionService(AgrupacionRepository agrupacionRepository) {
        this.agrupacionRepository = agrupacionRepository;
    }


}
