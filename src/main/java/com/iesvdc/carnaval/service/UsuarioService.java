package com.iesvdc.carnaval.service;

import com.iesvdc.carnaval.model.Usuario;
import com.iesvdc.carnaval.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en la base de datos.
     * @param usuario Datos del usuario a registrar.
     * @throws RuntimeException Si el nombre de usuario ya existe.
     */
    public void registrarUsuario(Usuario usuario) {
        // Verificar si el usuario ya existe en la base de datos
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario '" + usuario.getUsername() + "' ya está registrado.");
        }

        // Encriptar la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Guardar el usuario en la base de datos
        usuarioRepository.save(usuario);
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     * @param username Nombre de usuario a buscar.
     * @return Usuario encontrado o una excepción si no existe.
     */
    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /**
     * Obtiene todos los usuarios registrados.
     * @return Lista de usuarios.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Elimina un usuario por su ID.
     * @param id Identificador del usuario a eliminar.
     */
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("No se encontró un usuario con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}