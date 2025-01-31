package com.iesvdc.carnaval.service;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Modalidad;
import com.iesvdc.carnaval.repository.AgrupacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgrupacionServiceTest {

    @Autowired
    private AgrupacionService agrupacionService;

    @MockitoBean
    private AgrupacionRepository agrupacionRepository;

    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        agrupacion = new Agrupacion(1L, "Los Alegres", Modalidad.Chirigota, 10, "Cádiz");

        Mockito.when(agrupacionRepository.findById(1L)).thenReturn(Optional.of(agrupacion));
        Mockito.when(agrupacionRepository.findAll()).thenReturn(List.of(agrupacion));
    }

    @Test
    @DisplayName("Prueba de obtención de una agrupación por ID válido")
    public void findByIdShouldReturnAgrupacion() {
        long id = 1L;
        Optional<Agrupacion> resultado = agrupacionService.obtenerAgrupacionPorId(id);
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        System.out.println("Agrupacion: " + resultado);
    }

    @Test
    @DisplayName("Prueba de obtención de una agrupación por ID no encontrado")
    void findByIdShouldReturnEmpty() {
        Optional<Agrupacion> resultado = agrupacionService.obtenerAgrupacionPorId(2L);
        assertFalse(resultado.isPresent());
        System.out.println("Agrupacion: " + resultado);

    }

    @Test
    @DisplayName("Prueba de guardar una agrupación")
    public void saveAgrupacionShouldWork() {
        Agrupacion nuevaAgrupacion = new Agrupacion(2L, "Los Traviesos", Modalidad.Comparsa, 15, "Sevilla");
        agrupacionService.guardarAgrupacion(nuevaAgrupacion);
        Mockito.verify(agrupacionRepository, Mockito.times(1)).save(nuevaAgrupacion);
    }

    @Test
    @DisplayName("Prueba de listar todas las agrupaciones")
    public void findAllShouldReturnList() {
        List<Agrupacion> resultado = agrupacionService.listarAgrupaciones();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        System.out.println("Agrupaciones: " + resultado);
    }

    @Test
    @DisplayName("Prueba de eliminación de una agrupación")
    public void deleteAgrupacionShouldWork() {
        long id = 1L;
        agrupacionService.eliminarAgrupacion(id);
        Mockito.verify(agrupacionRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Prueba de edición de una agrupación")
    public void updateAgrupacionShouldWork() {
        agrupacion.setNombre("Los Renegados");
        agrupacionService.editarAgrupacion(agrupacion);
        Mockito.verify(agrupacionRepository, Mockito.times(1)).save(agrupacion);
    }
}