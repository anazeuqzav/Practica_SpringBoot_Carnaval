package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Componente;
import com.iesvdc.carnaval.model.Fase;
import com.iesvdc.carnaval.model.Puntuacion;
import com.iesvdc.carnaval.service.AgrupacionService;
import com.iesvdc.carnaval.service.PuntuacionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Manejo de las peticions HTTP
 */
@Controller
public class PuntuacionController {

    private final PuntuacionService puntuacionService;
    private final AgrupacionService agrupacionService;

    public PuntuacionController(PuntuacionService puntuacionService, AgrupacionService agrupacionService) {
        this.puntuacionService = puntuacionService;
        this.agrupacionService = agrupacionService;
    }
    // Mostrar formulario para añadir componente
    @GetMapping("/puntuacion/nuevo")
    public String mostrarFormularioPuntuacion(@RequestParam("idAgrupacion") Long idAgrupacion, Model model) {
        Puntuacion puntuacion = new Puntuacion();
        Agrupacion agrupacion = agrupacionService.obtenerAgrupacionPorId(idAgrupacion).get();
        puntuacion.setAgrupacion(agrupacion);

        model.addAttribute("puntuacion", puntuacion);
        model.addAttribute("agrupacion", agrupacion);
        model.addAttribute("fases", Fase.values());

        return "anadir_puntuacion"; // Vista del formulario
    }

    // Manejar la creación de un agrupacion
    @PostMapping("/puntuacion/guardar")
    public String anadirPuntuacion(@Valid @ModelAttribute("puntuacion") Puntuacion puntuacion,
                                   BindingResult result, Model model) {
        // Verificar si hay errores de validación
        if (result.hasErrors()) {
            return "anadir_puntuacion"; // Si hay errores, mostrar el formulario de nuevo
        }

        // Verificar que la agrupación no sea nula
        if (puntuacion.getAgrupacion() == null) {
            model.addAttribute("error", "La agrupación no puede ser nula");
            return "anadir_puntuacion"; // Mostrar el formulario con un mensaje de error
        }

        // Guardar la puntuación
        puntuacionService.guardarPuntuacion(puntuacion);

        // Redirigir a la página de detalles de la agrupación
        return "redirect:/agrupacion/" + puntuacion.getAgrupacion().getId();
    }

    // Ver detalle del puntuacion
    @GetMapping("/puntuacion/{id}")
    public String mostrarDetallePuntuacion(@PathVariable("id") Long id, Model model) {
        Optional<Puntuacion> puntuacionOptional = puntuacionService.obtenerPuntuacionPorId(id);
        if (puntuacionOptional.isPresent()) {
            model.addAttribute("puntuacion", puntuacionOptional.get());
            return "detalle_puntuacion";
        } else {
            // Redirigir a un error o página de inicio si no se encuentra el componente
            return "redirect:/";
        }
    }

    // Formulario para editar componente
    @GetMapping("/puntuacion/editar/{id}")
    public String mostrarFormularioEditarPuntuacion(@PathVariable Long id, Model model) {
        Optional <Puntuacion> puntuacionOptional = puntuacionService.obtenerPuntuacionPorId(id);
        if (puntuacionOptional.isPresent()) {
            model.addAttribute("puntuacion", puntuacionOptional.get());
            model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
            return "editar_puntuacion";
        } else {
            return "redirect:/";
        }
    }

    // Manejar editar componente
    @PostMapping("/puntuacion/editar")
    public String guardarCambios(@ModelAttribute Puntuacion puntuacion) {
        puntuacionService.guardarPuntuacion(puntuacion);
        return "redirect:/agrupacion/" + puntuacion.getAgrupacion().getId();
    }

    // Eliminar una componente
    @PostMapping("/puntuacion/eliminar/{id}")
    public String eliminarPuntuacion(@PathVariable Long id, Model model) {
        long agrupacionId = puntuacionService.obtenerPuntuacionPorId(id).get().getAgrupacion().getId();
        puntuacionService.eliminarPuntuacion(id);
        model.addAttribute("mensaje", "Puntuacion eliminada correctamente");
        return "redirect:/agrupacion/" + agrupacionId;
    }
}
