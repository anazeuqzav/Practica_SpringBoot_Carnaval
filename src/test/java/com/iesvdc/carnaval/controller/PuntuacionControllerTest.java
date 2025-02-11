package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Fase;
import com.iesvdc.carnaval.model.Modalidad;
import com.iesvdc.carnaval.model.Puntuacion;
import com.iesvdc.carnaval.service.AgrupacionService;
import com.iesvdc.carnaval.service.PuntuacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/*
@WebMvcTest(PuntuacionController.class)
class PuntuacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PuntuacionService puntuacionService;

    @MockitoBean
    private AgrupacionService agrupacionService;

    private Puntuacion puntuacion;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        agrupacion = new Agrupacion(55L, "Los Alegres", Modalidad.Chirigota, 10, "Cádiz");
        puntuacion = new Puntuacion(1L, Fase.Final, 90, agrupacion);
    }

    @Test
    @DisplayName("Prueba de mostrar formulario para añadir puntuación")
    public void mostrarFormularioPuntuacion_debeRetornarVistaConFormulario() throws Exception {
        Mockito.when(agrupacionService.obtenerAgrupacionPorId(55L)).thenReturn(Optional.of(agrupacion));

        mockMvc.perform(get("/puntuacion/nuevo?idAgrupacion=55"))
                .andExpect(status().isOk())
                .andExpect(view().name("anadir_puntuacion"))
                .andExpect(model().attributeExists("puntuacion"))
                .andExpect(model().attribute("agrupacion", agrupacion))
                .andExpect(model().attributeExists("fases"));
    }

    @Test
    @DisplayName("Prueba de guardar una puntuación correctamente")
    public void anadirPuntuacion_debeGuardarYRedirigir() throws Exception {
        Mockito.when(agrupacionService.obtenerAgrupacionPorId(55L)).thenReturn(Optional.of(agrupacion));

        // Realizar la solicitud POST
        mockMvc.perform(post("/puntuacion/guardar")
                        .param("puntos", "90")
                        .param("fase", "Final")
                        .param("agrupacion.id", "55")) // Asegúrate de que este parámetro esté correctamente enviado
                .andExpect(status().is3xxRedirection()) // Esperar una redirección
                .andExpect(redirectedUrl("/agrupacion/55")); // Esperar la URL de redirección

        // Verificar que se llamó al método de guardar
        Mockito.verify(puntuacionService, Mockito.times(1)).guardarPuntuacion(Mockito.any(Puntuacion.class));
    }

    @Test
    @DisplayName("Prueba de mostrar el detalle de una puntuación")
    public void mostrarDetallePuntuacion_debeRetornarVistaConDetalles() throws Exception {
        Mockito.when(puntuacionService.obtenerPuntuacionPorId(1L)).thenReturn(Optional.of(puntuacion));

        mockMvc.perform(get("/puntuacion/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("detalle_puntuacion"))
                .andExpect(model().attributeExists("puntuacion"))
                .andExpect(model().attribute("puntuacion", puntuacion));
    }

    @Test
    @DisplayName("Prueba de mostrar formulario de edición de puntuación")
    public void mostrarFormularioEditarPuntuacion_debeRetornarVistaConPuntuacion() throws Exception {
        Mockito.when(puntuacionService.obtenerPuntuacionPorId(1L)).thenReturn(Optional.of(puntuacion));
        Mockito.when(agrupacionService.listarAgrupaciones()).thenReturn(Arrays.asList(agrupacion));

        mockMvc.perform(get("/puntuacion/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editar_puntuacion"))
                .andExpect(model().attributeExists("puntuacion"))
                .andExpect(model().attribute("puntuacion", puntuacion))
                .andExpect(model().attributeExists("agrupaciones"));
    }

    @Test
    @DisplayName("Prueba de editar una puntuación")
    public void guardarCambios_debeGuardarYRedirigir() throws Exception {
        mockMvc.perform(post("/puntuacion/editar")
                        .param("id", "1")
                        .param("puntos", "95")
                        .param("fase", "Final")
                        .param("agrupacion.id", "55"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/agrupacion/55"));

        Mockito.verify(puntuacionService, Mockito.times(1)).guardarPuntuacion(Mockito.any(Puntuacion.class));
    }

    @Test
    @DisplayName("Prueba de eliminar una puntuación")
    public void eliminarPuntuacion_debeRedirigir() throws Exception {
        Mockito.when(puntuacionService.obtenerPuntuacionPorId(1L)).thenReturn(Optional.of(puntuacion));

        mockMvc.perform(post("/puntuacion/eliminar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/agrupacion/55"));

        Mockito.verify(puntuacionService, Mockito.times(1)).eliminarPuntuacion(1L);
    }
}
*/