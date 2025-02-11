package com.iesvdc.carnaval.controller;

import com.iesvdc.carnaval.model.*;
import com.iesvdc.carnaval.service.AgrupacionService;
import com.iesvdc.carnaval.service.ComponenteService;
import com.iesvdc.carnaval.service.PuntuacionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final AgrupacionService agrupacionService;
    private final ComponenteService componenteService;
    private final PuntuacionService puntuacionService;

    public UserController(AgrupacionService agrupacionService, ComponenteService componenteService, PuntuacionService puntuacionService) {
        this.agrupacionService = agrupacionService;
        this.componenteService = componenteService;
        this.puntuacionService = puntuacionService;
    }

    /** AGRUPACIONES **/

    // Método para mostrar una lista de agrupaciones
    @GetMapping("/agrupaciones")
    public String agrupaciones(Model model) {
        model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
        return "agrupaciones";
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
            return "detalle_agrupacion";
        } else {
            // Si no se encuentra la agrupación, redirigir o manejar el error
            return "redirect:/agrupaciones";
        }
    }

    @GetMapping("/clasificacion")
    public String verClasificacion(@RequestParam(name = "modalidad", required = false) Modalidad modalidad,
                                   @RequestParam(name = "fase", required = false) Fase fase,
                                   Model model) {
        if (modalidad == null) {
            modalidad = Modalidad.values()[0];
        }

        if (fase == null) {
            fase = Fase.Final; // Por defecto, mostrar la clasificación final
        }

        List<Agrupacion> clasificacion = agrupacionService.obtenerClasificacionPorModalidadYFase(modalidad, fase);
        List<Modalidad> modalidades = agrupacionService.obtenerModalidades();
        List<Fase> fases = agrupacionService.obtenerFases();

        model.addAttribute("clasificacion", clasificacion);
        model.addAttribute("modalidades", modalidades);
        model.addAttribute("modalidadSeleccionada", modalidad);
        model.addAttribute("faseSeleccionada", fase);
        model.addAttribute("fases", fases);

        return "clasificacion_final";
    }

    /** COMPONENTES **/

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

        // Comprobación del número máximo de componentes de la agrupación
        if (agrupacion.getNumeroDeComponentes() >= agrupacion.getCapacidadMaxima()) {
            model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
            model.addAttribute("error", "La agrupación seleccionada está llena.");
            return "anadir_componente";
        }

        if (componente.getRol().equals("Director")) {
            if (agrupacion.getDirector() != null){
                // si la agrupacion ya tiene director lanza un error
                model.addAttribute("error", "Ya existe un director en esta agrupación.");
                model.addAttribute("agrupacion", agrupacion);
                return "anadir_componente";  // Volver al formulario con el error
            }
            componenteService.guardarComponente(componente);
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

    /** PUNTUACION **/
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




}
