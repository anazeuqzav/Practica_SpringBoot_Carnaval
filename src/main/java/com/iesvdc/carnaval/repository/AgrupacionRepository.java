package com.iesvdc.carnaval.repository;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Fase;
import com.iesvdc.carnaval.model.Modalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interfaz para interactuar con la base de datos
 */
public interface AgrupacionRepository extends JpaRepository<Agrupacion, Long> {
    @Query("SELECT a, SUM(p.puntos) FROM Agrupacion a " +
            "JOIN a.puntuaciones p " +
            "WHERE a.modalidad = :modalidad " +
            "AND p.fase <= :fase " +
            "GROUP BY a.id, a.modalidad " +
            "ORDER BY SUM(p.puntos) DESC")
    List<Object[]> obtenerClasificacionPorModalidadYFase(@Param("modalidad") Modalidad modalidad, @Param("fase") Fase fase);
    // Consulta para obtener todas las modalidades disponibles
    @Query("SELECT DISTINCT a.modalidad FROM Agrupacion a")
    List<Modalidad> obtenerModalidades();

}

