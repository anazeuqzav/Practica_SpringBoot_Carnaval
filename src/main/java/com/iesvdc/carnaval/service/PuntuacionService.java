package com.iesvdc.carnaval.service;

import com.iesvdc.carnaval.repository.PuntuacionRepository;

public class PuntuacionService {

    private final PuntuacionRepository puntuacionRepository;

    // Constructor
    public PuntuacionService(PuntuacionRepository puntuacionRepository) {
        this.puntuacionRepository = puntuacionRepository;
    }


}
