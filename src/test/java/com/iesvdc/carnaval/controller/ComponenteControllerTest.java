package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Componente;
import com.iesvdc.carnaval.model.Modalidad;
import com.iesvdc.carnaval.service.AgrupacionService;
import com.iesvdc.carnaval.service.ComponenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComponenteController.class)
class ComponenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComponenteService componenteService;

    @MockitoBean
    private AgrupacionService agrupacionService;

    private Componente componente;
    private Agrupacion agrupacion;

    @BeforeEach
    void setUp() {
        agrupacion = new Agrupacion(55L, "Los Alegres", Modalidad.Chirigota, 10, "Cádiz");
        componente = new Componente(555L, "Juan Pérez", "Director", agrupacion);
    }

    @Test
    @DisplayName("Prueba de mostrar formulario para añadir componente")
    public void mostrarFormularioComponente_debeRetornarVistaConFormulario() throws Exception {
        Mockito.when(agrupacionService.obtenerAgrupacionPorId(55L)).thenReturn(Optional.of(agrupacion));

        mockMvc.perform(get("/componente/nuevo?idAgrupacion=55"))
                .andExpect(status().isOk())
                .andExpect(view().name("anadir_componente"))
                .andExpect(model().attributeExists("componente"))
                .andExpect(model().attribute("agrupacion", agrupacion));
    }

    @Test
    @DisplayName("Prueba de guardar un componente Director correctamente")
    public void guardarComponente_debeGuardarYRedirigir() throws Exception {
        Mockito.when(agrupacionService.obtenerAgrupacionPorId(55L)).thenReturn(Optional.of(agrupacion));

        mockMvc.perform(post("/componente/guardar")
                        .param("nombre", "Juan Pérez")
                        .param("rol", "Director")
                        .param("agrupacion.id", "55"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/agrupacion/55"));

        Mockito.verify(componenteService, Mockito.times(1)).guardarComponente(Mockito.any(Componente.class));
        Mockito.verify(agrupacionService, Mockito.times(1)).guardarAgrupacion(Mockito.any(Agrupacion.class));
    }

    @Test
    @DisplayName("Prueba de guardar un componente Director si ya hay un director en la agrupación")
    public void guardarComponente_debeRetornarErrorSiYaHayUnDirector() throws Exception {
        // Simulamos que la agrupación ya tiene un director asignado
        Agrupacion agrupacionConDirector = new Agrupacion(55L, "Los Alegres", Modalidad.Chirigota, 10, "Cádiz");
        Componente directorExistente = new Componente(1L, "Antonio López", "Director", agrupacionConDirector);
        agrupacionConDirector.setDirector(directorExistente);

        // Simulamos la respuesta del servicio para obtener la agrupación
        Mockito.when(agrupacionService.obtenerAgrupacionPorId(55L)).thenReturn(Optional.of(agrupacionConDirector));

        // Realizamos el POST para intentar guardar un nuevo director
        mockMvc.perform(post("/componente/guardar")
                        .param("nombre", "Juan Pérez")
                        .param("rol", "Director")
                        .param("agrupacion.id", "55"))
                .andExpect(status().isOk()) // Debería devolver una vista, no una redirección
                .andExpect(view().name("anadir_componente")) // La vista debe ser el formulario de adición
                .andExpect(model().attributeExists("error")) // Verificamos que existe el atributo error
                .andExpect(model().attribute("error", "Ya existe un director en esta agrupación.")); // El mensaje de error

        // Verificamos que no se haya llamado a los métodos de guardar el componente ni la agrupación
        Mockito.verify(componenteService, Mockito.times(0)).guardarComponente(Mockito.any(Componente.class));
        Mockito.verify(agrupacionService, Mockito.times(0)).guardarAgrupacion(Mockito.any(Agrupacion.class));
    }

    @Test
    @DisplayName("Prueba de mostrar el detalle de un componente")
    public void verDetalleComponente_debeRetornarVistaConDetalles() throws Exception {
        Mockito.when(componenteService.obtenerComponentePorId(555L)).thenReturn(Optional.of(componente));

        mockMvc.perform(get("/componente/555"))
                .andExpect(status().isOk())
                .andExpect(view().name("detalle_componente"))
                .andExpect(model().attributeExists("componente"))
                .andExpect(model().attribute("componente", componente));
    }

    @Test
    @DisplayName("Prueba de mostrar el formulario de edición con los datos actuales")
    public void mostrarFormularioEditarComponente_debeRetornarVistaConComponente() throws Exception {
        Mockito.when(componenteService.obtenerComponentePorId(555L)).thenReturn(Optional.of(componente));
        Mockito.when(agrupacionService.listarAgrupaciones()).thenReturn(Arrays.asList(agrupacion));

        mockMvc.perform(get("/componente/editar/555"))
                .andExpect(status().isOk())
                .andExpect(view().name("editar_componente"))
                .andExpect(model().attributeExists("componente"))
                .andExpect(model().attribute("componente", componente))
                .andExpect(model().attributeExists("agrupaciones"))
                .andExpect(model().attribute("agrupaciones", Arrays.asList(agrupacion)));
    }

    @Test
    @DisplayName("Prueba de editar un componente")
    public void editarComponente_debeGuardarYRedirigir() throws Exception {
        // Configurar los mocks
        Mockito.when(componenteService.obtenerComponentePorId(555L)).thenReturn(Optional.of(componente));
        Mockito.when(agrupacionService.obtenerAgrupacionPorId(55L)).thenReturn(Optional.of(agrupacion));
        Mockito.when(agrupacionService.listarAgrupaciones()).thenReturn(Arrays.asList(agrupacion));

        // Realizar la solicitud POST
        mockMvc.perform(post("/componente/editar")
                        .param("id", "555")
                        .param("nombre", "Juan Pérez")
                        .param("rol", "Director")
                        .param("agrupacion.id", "55"))
                .andExpect(status().is3xxRedirection()) // Se espera redirección
                .andExpect(redirectedUrl("/agrupacion/55"));

        // Verificar que se llamó a los métodos de guardar
        Mockito.verify(componenteService, Mockito.times(1)).guardarComponente(Mockito.any(Componente.class));
        Mockito.verify(agrupacionService, Mockito.times(2)).guardarAgrupacion(Mockito.any(Agrupacion.class));
    }

    @Test
    @DisplayName("Prueba de eliminar un componente y redirigir al detalle de la agrupación")
    public void eliminarComponente_debeRedirigir() throws Exception {
        Mockito.when(componenteService.obtenerComponentePorId(555L)).thenReturn(Optional.of(componente));

        mockMvc.perform(post("/componente/eliminar/555"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/agrupacion/55"));

        Mockito.verify(componenteService, Mockito.times(1)).eliminarComponente(555L);
        Mockito.verify(agrupacionService, Mockito.times(1)).guardarAgrupacion(Mockito.any(Agrupacion.class));
    }
}
