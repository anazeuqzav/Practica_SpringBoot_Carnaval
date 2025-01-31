package com.iesvdc.carnaval.service;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Componente;
import com.iesvdc.carnaval.model.Modalidad;
import com.iesvdc.carnaval.repository.ComponenteRepository;
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
class ComponenteServiceTest {

    @Autowired
    private ComponenteService componenteService;

    @MockitoBean
    private ComponenteRepository componenteRepository;

    private Componente componente;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        agrupacion = new Agrupacion(1L, "Los Alegres", Modalidad.Chirigota, 10, "Cádiz");
        componente = new Componente(1L, "Juan Pérez", 30, "Cantante", "Guitarra", agrupacion);

        Mockito.when(componenteRepository.findById(1L)).thenReturn(Optional.of(componente));
        Mockito.when(componenteRepository.findAll()).thenReturn(List.of(componente));
    }

    @Test
    @DisplayName("Prueba de obtención de un componente por ID válido")
    public void findByIdShouldReturnComponente() {
        long id = 1L;
        Optional<Componente> resultado = componenteService.obtenerComponentePorId(id);
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        System.out.println("Componente: " + resultado);
    }

    @Test
    @DisplayName("Prueba de obtención de un componente por ID no encontrado")
    void findByIdShouldReturnEmpty() {
        Optional<Componente> resultado = componenteService.obtenerComponentePorId(2L);
        assertFalse(resultado.isPresent());
        System.out.println("Componente: " + resultado);
    }

    @Test
    @DisplayName("Prueba de guardar un componente")
    public void saveComponenteShouldWork() {
        Componente nuevoComponente = new Componente(2L, "Ana López", 25, "Percusionista", "Caja", agrupacion);
        componenteService.guardarComponente(nuevoComponente);
        Mockito.verify(componenteRepository, Mockito.times(1)).save(nuevoComponente);
    }

    @Test
    @DisplayName("Prueba de listar todos los componentes")
    public void findAllShouldReturnList() {
        List<Componente> resultado = componenteService.listarComponentes();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        System.out.println("Componentes: " + resultado);
    }

    @Test
    @DisplayName("Prueba de eliminación de un componente")
    public void deleteComponenteShouldWork() {
        long id = 1L;
        componenteService.eliminarComponente(id);
        Mockito.verify(componenteRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Prueba de edición de un componente")
    public void updateComponenteShouldWork() {
        componente.setNombre("Pedro García");
        componenteService.editarAgrupacion(componente);
        Mockito.verify(componenteRepository, Mockito.times(1)).save(componente);
    }
}