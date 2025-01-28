package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.model.Componente;
import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.service.AgrupacionService;
import com.iesvdc.carnaval.service.ComponenteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String guardarComponente(@ModelAttribute("componente") Componente componente) {

        componenteService.guardarComponente(componente); // Guardar componente

        Agrupacion agrupacion = agrupacionService.obtenerAgrupacionPorId(componente.getAgrupacion().getId()).get();

        if (componente.getRol().equals("Director")) {
            agrupacion.setDirector(componente);
        }

        agrupacion.setNumeroDeComponentes(agrupacion.getNumeroDeComponentes() + 1);
        agrupacionService.guardarAgrupacion(agrupacion);

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
    public String guardarCambios(@ModelAttribute Componente componente) {
        componenteService.guardarComponente(componente);
        return "redirect:/";
    }

    // Eliminar una componente
    @PostMapping("/componente/eliminar/{id}")
    public String eliminarAgrupacion(@PathVariable Long id, Model model) {
       Optional<Componente> componenteOptional = componenteService.obtenerComponentePorId(id);
       if(componenteOptional.isPresent()){
           Componente componente = componenteOptional.get();
           Agrupacion agrupacion = componente.getAgrupacion();

           if(componente.getRol().equals("Director")) {
               agrupacion.setDirector(null);
           }
           agrupacion.setNumeroDeComponentes(agrupacion.getNumeroDeComponentes() - 1);
           agrupacionService.guardarAgrupacion(agrupacion);
       }
        componenteService.eliminarComponente(id);
        model.addAttribute("mensaje", "Componente eliminada correctamente");
        return "redirect:/agrupaciones";
    }

}
