package com.iesvdc.carnaval.repository;

import com.iesvdc.carnaval.model.Componente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz para interactuar con la base de datos
 */
public interface ComponenteRepository extends JpaRepository<Componente, Long> {
}
