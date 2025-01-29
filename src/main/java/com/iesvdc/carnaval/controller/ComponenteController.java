package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.model.Componente;
import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.service.AgrupacionService;
import com.iesvdc.carnaval.service.ComponenteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;


import java.util.Optional;

/**
 * Manejo de las peticions HTTP
 */

@Controller
public class ComponenteController {

    private final ComponenteService componenteService;
    private final AgrupacionService agrupacionService;

    public ComponenteController(ComponenteService componenteService, AgrupacionService agrupacionService) {
        this.componenteService = componenteService;
        this.agrupacionService = agrupacionService;
    }

    // Mostrar formulario para añadir componente
    @GetMapping("/componente/nuevo")
    public String mostrarFormularioComponente(@RequestParam("idAgrupacion") Long idAgrupacion, Model model) {
        Componente nuevoComponente = new Componente();
        Agrupacion agrupacion = agrupacionService.obtenerAgrupacionPorId(idAgrupacion).get();
        nuevoComponente.setAgrupacion(agrupacion); // Asociar componente a la agrupación

        model.addAttribute("componente", nuevoComponente);
        model.addAttribute("agrupacion", agrupacion);

        return "anadir_componente"; // Vista del formulario
    }

    // Manejar la creación del componente
    @PostMapping("/componente/guardar")
    public String guardarComponente(@Valid @ModelAttribute("componente") Componente componente,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            // Si hay errores, vuelve a mostrar el formulario con los mensajes de error
            model.addAttribute("agrupacion", componente.getAgrupacion());
            return "anadir_componente";
        }

        Agrupacion agrupacion = agrupacionService.obtenerAgrupacionPorId(componente.getAgrupacion().getId()).get();

        if (componente.getRol().equals("Director")) {
            if (agrupacion.getDirector()!= null){
                // si la agrupacion ya tiene director lanza un error
                model.addAttribute("error", "Ya existe un director en esta agrupación.");
                model.addAttribute("agrupacion", agrupacion);
                return "anadir_componente";  // Volver al formulario con el error
            }
            agrupacion.setDirector(componente);
        }
        agrupacion.setNumeroDeComponentes(agrupacion.getNumeroDeComponentes() + 1);
        agrupacionService.guardarAgrupacion(agrupacion);

        componenteService.guardarComponente(componente); // Guardar componente

        return "redirect:/agrupacion/" + componente.getAgrupacion().getId(); // Volver al detalle de la agrupación
    }

    // Ver detalle del componente
    @GetMapping("/componente/{id}")
    public String mostrarDetalleComponente(@PathVariable("id") Long id, Model model) {
        Optional<Componente> componenteOptional = componenteService.obtenerComponentePorId(id);
        if (componenteOptional.isPresent()) {
            model.addAttribute("componente", componenteOptional.get());
            return "detalle_componente";
        } else {
            // Redirigir a un error o página de inicio si no se encuentra el componente
            return "redirect:/";
        }
    }

    // Formulario para editar componente
    @GetMapping("/componente/editar/{id}")
    public String mostrarFormularioEditarComponente(@PathVariable Long id, Model model) {
        Optional<Componente> componenteOptional = componenteService.obtenerComponentePorId(id);
        if (componenteOptional.isPresent()) {
            model.addAttribute("componente", componenteOptional.get());
            model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
            return "editar_componente";
        } else {
            return "redirect:/";
        }
    }

    // Manejar editar componente
    @PostMapping("/componente/editar")
    public String guardarCambios(@Valid @ModelAttribute("componente") Componente componente,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
            return "editar_componente";
        }

        // Obtener de la base de datos el componente antes de los cambios
        Componente componenteOriginal = componenteService.obtenerComponentePorId(componente.getId()).get();

        // Obtener la agrupacion anterior
        Agrupacion agrupacionAnterior = componenteOriginal.getAgrupacion();

        // Obtener la agrupacion nueva
        Agrupacion nuevaAgrupacion = agrupacionService.obtenerAgrupacionPorId(componente.getAgrupacion().getId()).get();

        // Comprobación del número máximo de componentes de la agrupación
        if (nuevaAgrupacion.getNumeroDeComponentes() >= nuevaAgrupacion.getCapacidadMaxima()) {
            model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
            model.addAttribute("error", "La agrupación seleccionada está llena.");
            return "editar_componente";
        }

        // Si el componente es director y ya hay un director en la nueva agrupación, mostrar error
        if (componente.getRol().equals("Director")) {
            if (nuevaAgrupacion.getDirector() != null) {
                model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
                model.addAttribute("error", "Ya existe un director en esta agrupación.");
                return "editar_componente";  // Volver al formulario con el error
            }
            nuevaAgrupacion.setDirector(componente);
        }

        // Si el componente era director en la agrupación anterior, quitarlo
        if (componenteOriginal.getRol().equals("Director")) {
            agrupacionAnterior.setDirector(null);
        }

        // Actualizar los números de componentes en las agrupaciones
        if (agrupacionAnterior != null) {
            agrupacionAnterior.setNumeroDeComponentes(agrupacionAnterior.getNumeroDeComponentes() - 1);
            agrupacionService.guardarAgrupacion(agrupacionAnterior);
        }

        nuevaAgrupacion.setNumeroDeComponentes(nuevaAgrupacion.getNumeroDeComponentes() + 1);

        agrupacionService.guardarAgrupacion(nuevaAgrupacion);
        componente.setAgrupacion(nuevaAgrupacion);

        // Guardar el componente actualizado
        componenteService.guardarComponente(componente);
        return "redirect:/agrupacion/" + componente.getAgrupacion().getId();
    }

    // Eliminar una componente
    @PostMapping("/componente/eliminar/{id}")
    public String eliminarAgrupacion(@PathVariable Long id, Model model) {
        Optional<Componente> componenteOptional = componenteService.obtenerComponentePorId(id);
        Componente componente = null;
        if (componenteOptional.isPresent()) {
            componente = componenteOptional.get();
            Agrupacion agrupacion = componente.getAgrupacion();

            if (componente.getRol().equals("Director")) {
                agrupacion.setDirector(null);
            }
            agrupacion.setNumeroDeComponentes(agrupacion.getNumeroDeComponentes() - 1);
            agrupacionService.guardarAgrupacion(agrupacion);
        }
        componenteService.eliminarComponente(id);
        model.addAttribute("mensaje", "Componente eliminada correctamente");
        return "redirect:/agrupacion/" + componente.getAgrupacion().getId();
    }

}
