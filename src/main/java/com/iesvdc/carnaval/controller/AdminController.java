package com.iesvdc.carnaval.controller;


import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Componente;
import com.iesvdc.carnaval.model.Fase;
import com.iesvdc.carnaval.model.Puntuacion;
import com.iesvdc.carnaval.service.AgrupacionService;
import com.iesvdc.carnaval.service.ComponenteService;
import com.iesvdc.carnaval.service.PuntuacionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AgrupacionService agrupacionService;
    private final ComponenteService componenteService;
    private final PuntuacionService puntuacionService;


    public AdminController(AgrupacionService agrupacionService, ComponenteService componenteService, PuntuacionService puntuacionService) {
        this.agrupacionService = agrupacionService;
        this.componenteService = componenteService;
        this.puntuacionService = puntuacionService;
    }

    /** AGRUPACIONES **/

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
        return "redirect:/agrupaciones";
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

        // Recuperar el director anterior
        Componente directorAnterior = agrupacionOriginal.getDirector();

        // Solo quitar el rol si hay un director
        if (directorAnterior != null) {
            directorAnterior.setRol(null); // O el valor por defecto
            componenteService.guardarComponente(directorAnterior); // Guardar cambios
        } // Guardar cambios


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

        return "redirect:/user/agrupacion/" + componente.getAgrupacion().getId(); // Volver al detalle de la agrupación
    }

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
        // Comprobar si hay errores de validación
        if (bindingResult.hasErrors()) {
            model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
            return "editar_componente";
        }

        // Obtener de la base de datos el componente antes de los cambios
        Optional<Componente> componenteOriginalOptional = componenteService.obtenerComponentePorId(componente.getId());
        if (componenteOriginalOptional.isEmpty()) {
            model.addAttribute("error", "Componente no encontrado");
            model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
            return "editar_componente";
        }
        Componente componenteOriginal = componenteOriginalOptional.get();

        // Obtener la agrupación anterior y la nueva
        Agrupacion agrupacionAnterior = componenteOriginal.getAgrupacion();
        Optional<Agrupacion> nuevaAgrupacionOptional = agrupacionService.obtenerAgrupacionPorId(componente.getAgrupacion().getId());
        if (nuevaAgrupacionOptional.isEmpty()) {
            model.addAttribute("error", "Agrupación no encontrada");
            model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
            return "editar_componente";
        }

        Agrupacion nuevaAgrupacion = nuevaAgrupacionOptional.get();

        if (agrupacionAnterior.getDirector().getId() != componente.getId()) {
            // Si el componente es director y ya hay un director en la nueva agrupación, mostrar error
            if (componente.getRol().equals("Director")) {
                if (nuevaAgrupacion.getDirector() != null) {
                    model.addAttribute("agrupaciones", agrupacionService.listarAgrupaciones());
                    model.addAttribute("error", "Ya existe un director en esta agrupación.");
                    return "editar_componente";
                }
                nuevaAgrupacion.setDirector(componente);
            }
            // Si el componente era director en la agrupación anterior, quitarlo
            if (componenteOriginal.getRol().equals("Director")) {
                agrupacionAnterior.setDirector(null);
            }
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

        return "redirect:/user/agrupacion/" + nuevaAgrupacion.getId();
    }

    // Eliminar una componente
    @PostMapping("/componente/eliminar/{id}")
    public String eliminarComponente(@PathVariable Long id, Model model) {
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
        return "redirect:/user/agrupacion/" + componente.getAgrupacion().getId();
    }

    /** PUNTUACIÓN **/

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
        return "redirect:/user/agrupacion/" + puntuacion.getAgrupacion().getId();
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
        return "redirect:/user/agrupacion/" + puntuacion.getAgrupacion().getId();
    }

    // Eliminar una puntuacion
    @PostMapping("/puntuacion/eliminar/{id}")
    public String eliminarPuntuacion(@PathVariable Long id, Model model) {
        long agrupacionId = puntuacionService.obtenerPuntuacionPorId(id).get().getAgrupacion().getId();
        puntuacionService.eliminarPuntuacion(id);
        model.addAttribute("mensaje", "Puntuacion eliminada correctamente");
        return "redirect:/user/agrupacion/" + agrupacionId;
    }





}
