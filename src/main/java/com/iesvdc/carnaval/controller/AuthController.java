package com.iesvdc.carnaval.controller;
import com.iesvdc.carnaval.model.Usuario;
import com.iesvdc.carnaval.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Muestra el formulario de registro.
     * @param model Modelo de Thymeleaf para pasar el objeto usuario.
     * @return Vista "register.html".
     */
    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register"; // Retorna la plantilla register.html
    }

    /**
     * Procesa el registro de un nuevo usuario.
     * @param usuario Datos del usuario enviado desde el formulario.
     * @return Redirección a la página de login.
     */
    @PostMapping("/register")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        try {
            usuarioService.registrarUsuario(usuario);
            return "redirect:/auth/login?success"; // Redirige a login con mensaje de éxito
        } catch (RuntimeException e) {
            return "redirect:/auth/register?error=" + e.getMessage(); // Redirige con mensaje de error
        }
    }

    /**
     * Muestra el formulario de login.
     * @return Vista "login.html".
     */
    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login"; // Retorna la plantilla login.html
    }

    /**
     * Muestra el index si se hace el login exitoso
     * @return
     */
    @GetMapping("/index")
    public String loginSuccesfull(){
        return "index";
    }

    /**
     * Muestra el perfil del usuario autenticado.
     * @param model Modelo de Thymeleaf para pasar el usuario.
     * @return Vista "perfil.html".
     */
    @GetMapping("/perfil")
    public String mostrarPerfil(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuario = usuarioService.obtenerUsuarioPorUsername(username);
        model.addAttribute("usuario", usuario);
        return "perfil"; // Retorna la plantilla perfil.html
    }
}