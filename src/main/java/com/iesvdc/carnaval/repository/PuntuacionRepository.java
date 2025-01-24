package com.iesvdc.carnaval.repository;

import com.iesvdc.carnaval.model.Puntuacion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz para interactuar con la base de datos
 */
public interface PuntuacionRepository extends JpaRepository<Puntuacion, Long> {



}
