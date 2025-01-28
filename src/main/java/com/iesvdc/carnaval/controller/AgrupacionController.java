package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Componente;
import com.iesvdc.carnaval.service.AgrupacionService;
import com.iesvdc.carnaval.service.ComponenteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

/**
 * Manejo de las peticions HTTP
 */

@Controller
public class AgrupacionController {

    private final AgrupacionService agrupacionService;
    private final ComponenteService componenteService;

    public AgrupacionController(AgrupacionService agrupacionService, ComponenteService componenteService) {
        this.agrupacionService = agrupacionService;
        this.componenteService = componenteService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Método para mostrar una lista de agrupaciones
    @GetMapping("/agrupaciones")
    public String agrupaciones(Model model) {
        model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
        return "agrupaciones";
    }

    // Página para crear un agrupacion
    @GetMapping("/crear_agrupacion")
    public String mostrarFormularioCrearAgrupacion(Model model) {
        model.addAttribute("agrupacion", new Agrupacion());
        return "crear_agrupacion";
    }

    // Manejar la creación de un agrupacion
    @PostMapping("/crear_agrupacion")
    public String crearAgrupacion(@ModelAttribute("agrupacion") Agrupacion agrupacion, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "crear_agrupacion";
        }
        agrupacionService.guardarAgrupacion(agrupacion);
        model.addAttribute("mensaje", "Agrupación creada correctamente");
        return "redirect:/agrupaciones";
    }

    // Ver detalles de una agrupacion
    @GetMapping("/agrupacion/{id}")
    public String verDetalleAgrupacion(@PathVariable Long id, Model model) {
        // Buscar la agrupación por ID
        Optional<Agrupacion> agrupacionOptional = agrupacionService.obtenerAgrupacionPorId(id);
        if (agrupacionOptional.isPresent()){
            Agrupacion agrupacion = agrupacionOptional.get();
            model.addAttribute("agrupacion", agrupacion);

            // Pasar la lista de componentes de la agrupación
            model.addAttribute("componentes", agrupacion.getComponentes());

            // Pasar la lista de puntuaciones de la agrupación
            model.addAttribute("puntuaciones", agrupacion.getPuntuaciones());
        }

        return "detalle_agrupacion";
    }
    // Eliminar una agrupacion
    @PostMapping("/agrupacion/eliminar/{id}")
    public String eliminarAgrupacion(@PathVariable Long id, Model model) {
        agrupacionService.eliminarAgrupacion(id);
        model.addAttribute("mensaje", "Agrupación eliminada correctamente");
        return "redirect:/agrupaciones";
    }

    // Mostrar el formulario de edición con los datos actuales del producto
    @GetMapping("/agrupacion/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Agrupacion> agrupacionOptional = agrupacionService.obtenerAgrupacionPorId(id);
        if (agrupacionOptional.isPresent()) {
            Agrupacion agrupacion = agrupacionOptional.get();
            model.addAttribute("agrupacion", agrupacion);
            model.addAttribute("componentes", agrupacion.getComponentes());
            return "editar_agrupacion";
        }
        return "redirect:/agrupaciones"; // Si no se encuentra el producto, vuelve al catálogo
    }

    // Manejar la edición de la agrupación
    @PostMapping("/agrupacion/editar")
    public String editarProducto(@ModelAttribute("agrupacion") Agrupacion agrupacion, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editar_agrupacion"; // Vuelve al formulario si hay errores
        }
        // Recuperar la agrupación original desde la base de datos
        Agrupacion agrupacionOriginal = agrupacionService.obtenerAgrupacionPorId(agrupacion.getId()).get();

        // Quitar el rol del director actual
        Componente directorAnterior = agrupacionOriginal.getDirector();
        directorAnterior.setRol(null); // O el valor por defecto
        componenteService.guardarComponente(directorAnterior); // Guardar cambios


        // Asignar el rol de director al nuevo componente seleccionado (si existe)
        if (agrupacion.getDirector() != null) {
            Componente nuevoDirector = componenteService.obtenerComponentePorId(agrupacion.getDirector().getId()).get();
            nuevoDirector.setRol("Director");
            componenteService.guardarComponente(nuevoDirector);
        }

        // Guardar los cambios en la agrupación
        agrupacionService.guardarAgrupacion(agrupacion); // Actualiza el producto
        model.addAttribute("mensaje", "Agrupacion editada correctamente");
        return "redirect:/agrupaciones"; // Redirige al catálogo
    }



}
