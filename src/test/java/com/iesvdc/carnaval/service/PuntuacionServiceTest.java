package com.iesvdc.carnaval.service;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Fase;
import com.iesvdc.carnaval.model.Modalidad;
import com.iesvdc.carnaval.model.Puntuacion;
import com.iesvdc.carnaval.repository.PuntuacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PuntuacionServiceTest {

    @Autowired
    private PuntuacionService puntuacionService;

    @MockitoBean
    private PuntuacionRepository puntuacionRepository;

    private Puntuacion puntuacion;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        agrupacion = new Agrupacion(1L, "Los Alegres", Modalidad.Chirigota, 10, "Cádiz");
        puntuacion = new Puntuacion(1L, Fase.Cuartos, 85.5, agrupacion);

        Mockito.when(puntuacionRepository.findById(1L)).thenReturn(Optional.of(puntuacion));
        Mockito.when(puntuacionRepository.findAll()).thenReturn(List.of(puntuacion));
    }

    @Test
    @DisplayName("Prueba de obtención de una puntuación por ID válido")
    public void findByIdShouldReturnPuntuacion() {
        long id = 1L;
        Optional<Puntuacion> resultado = puntuacionService.obtenerPuntuacionPorId(id);
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        System.out.println("Puntuación: " + resultado);
    }

    @Test
    @DisplayName("Prueba de obtención de una puntuación por ID no encontrado")
    void findByIdShouldReturnEmpty() {
        Optional<Puntuacion> resultado = puntuacionService.obtenerPuntuacionPorId(2L);
        assertFalse(resultado.isPresent());
        System.out.println("Puntuación: " + resultado);
    }

    @Test
    @DisplayName("Prueba de guardar una puntuación")
    public void savePuntuacionShouldWork() {
        Puntuacion nuevaPuntuacion = new Puntuacion(2L, Fase.Semifinales, 90.0, agrupacion);
        puntuacionService.guardarPuntuacion(nuevaPuntuacion);
        Mockito.verify(puntuacionRepository, Mockito.times(1)).save(nuevaPuntuacion);
    }

    @Test
    @DisplayName("Prueba de listar todas las puntuaciones")
    public void findAllShouldReturnList() {
        List<Puntuacion> resultado = puntuacionService.listarPuntuaciones();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        System.out.println("Puntuaciones: " + resultado);
    }

    @Test
    @DisplayName("Prueba de eliminación de una puntuación")
    public void deletePuntuacionShouldWork() {
        long id = 1L;
        puntuacionService.eliminarPuntuacion(id);
        Mockito.verify(puntuacionRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Prueba de edición de una puntuación")
    public void updatePuntuacionShouldWork() {
        puntuacion.setPuntos(95.0);
        puntuacionService.editarPuntuacion(puntuacion);
        Mockito.verify(puntuacionRepository, Mockito.times(1)).save(puntuacion);
    }
}