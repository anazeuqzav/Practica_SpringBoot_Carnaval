package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Fase;
import com.iesvdc.carnaval.model.Modalidad;
import com.iesvdc.carnaval.service.AgrupacionService;
import com.iesvdc.carnaval.service.ComponenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgrupacionController.class)
class AgrupacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgrupacionService agrupacionService;

    @MockitoBean
    private ComponenteService componenteService;

    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        agrupacion = new Agrupacion(55L, "Los Alegres", Modalidad.Chirigota, 10, "Cádiz");
    }

    @Test
    @DisplayName("Prueba de listar agrupaciones correctamente")
    public void listarAgrupaciones_debeRetornarVistaConAgrupaciones() throws Exception {
        List<Agrupacion> agrupaciones = Arrays.asList(agrupacion);
        Mockito.when(agrupacionService.listarAgrupaciones()).thenReturn(agrupaciones);

        mockMvc.perform(get("/agrupaciones"))
                .andExpect(status().isOk())
                .andExpect(view().name("agrupaciones"))
                .andExpect(model().attributeExists("agrupaciones"))
                .andExpect(model().attribute("agrupaciones", agrupaciones));
    }

    @Test
    @DisplayName("Prueba de guardar una agrupación y redirigir a lista de agrupaciones")
    public void crearAgrupacion_debeGuardarYRedirigir() throws Exception {
        mockMvc.perform(post("/crear_agrupacion")
                        .param("nombre", "Los Alegres")
                        .param("modalidad", "Chirigota")
                        .param("numeroComponentes", "10")
                        .param("localidad", "Cádiz"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/agrupaciones"));

        Mockito.verify(agrupacionService, Mockito.times(1)).guardarAgrupacion(Mockito.any(Agrupacion.class));
    }

    @Test
    @DisplayName("Prueba de mostrar los detalles de una agrupacion")
    public void verDetalleAgrupacion_debeRetornarVistaConDetalles() throws Exception {
        Mockito.when(agrupacionService.obtenerAgrupacionPorId(55L)).thenReturn(Optional.of(agrupacion));

        mockMvc.perform(get("/agrupacion/55"))
                .andExpect(status().isOk())
                .andExpect(view().name("detalle_agrupacion"))
                .andExpect(model().attributeExists("agrupacion"))
                .andExpect(model().attribute("agrupacion", agrupacion));
    }

    @Test
    @DisplayName("Prueba de eliminar una agrupacion y redirigir a la lista de agrupaciones")
    public void eliminarAgrupacion_debeRedirigir() throws Exception {
        mockMvc.perform(post("/agrupacion/eliminar/55"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/agrupaciones"));

        Mockito.verify(agrupacionService, Mockito.times(1)).eliminarAgrupacion(55L);
    }

    @Test
    @DisplayName("Prueba de mostrar el formulario de edición con los datos actuales")
    public void mostrarFormularioEditar_debeRetornarVistaConAgrupacion() throws Exception {
        Mockito.when(agrupacionService.obtenerAgrupacionPorId(55L)).thenReturn(Optional.of(agrupacion));

        mockMvc.perform(get("/agrupacion/editar/55"))
                .andExpect(status().isOk())
                .andExpect(view().name("editar_agrupacion"))
                .andExpect(model().attributeExists("agrupacion"))
                .andExpect(model().attribute("agrupacion", agrupacion));
    }

    @Test
    @DisplayName("Prueba de actualizar una agrupación ")
    public void editarAgrupacion_debeGuardarYRedirigir() throws Exception {
        Mockito.when(agrupacionService.obtenerAgrupacionPorId(55L)).thenReturn(Optional.of(agrupacion));

        mockMvc.perform(post("/agrupacion/editar")
                        .param("id", "55")
                        .param("nombre", "Los Reformados")
                        .param("modalidad", "Chirigota")
                        .param("numeroComponentes", "12")
                        .param("localidad", "Cádiz"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/agrupaciones"));

        Mockito.verify(agrupacionService, Mockito.times(1)).guardarAgrupacion(Mockito.any(Agrupacion.class));
    }

}