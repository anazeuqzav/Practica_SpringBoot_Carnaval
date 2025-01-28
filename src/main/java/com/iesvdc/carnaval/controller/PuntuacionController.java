package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Componente;
import com.iesvdc.carnaval.model.Fase;
import com.iesvdc.carnaval.model.Puntuacion;
import com.iesvdc.carnaval.service.AgrupacionService;
import com.iesvdc.carnaval.service.PuntuacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String anadirPuntuacion(@ModelAttribute("puntuacion") Puntuacion puntuacion, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "anadir_puntuacion";
        }
        puntuacionService.guardarPuntuacion(puntuacion);
        model.addAttribute("mensaje", "Puntuacion creada correctamente");
        return "redirect:/agrupaciones";
    }

}
