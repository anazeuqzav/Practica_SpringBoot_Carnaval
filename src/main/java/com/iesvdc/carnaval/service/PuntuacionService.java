package com.iesvdc.carnaval.service;

import com.iesvdc.carnaval.model.Componente;
import com.iesvdc.carnaval.model.Puntuacion;
import com.iesvdc.carnaval.repository.PuntuacionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PuntuacionService {

    private final PuntuacionRepository puntuacionRepository;

    // Constructor
    public PuntuacionService(PuntuacionRepository puntuacionRepository) {
        this.puntuacionRepository = puntuacionRepository;
    }

    // Listar Puntuacion
    public List<Puntuacion> listarPuntuaciones() {
        return puntuacionRepository.findAll();
    }

    // Guardar Puntuacion
    public void guardarPuntuacion(Puntuacion puntuacion) {
        puntuacionRepository.save(puntuacion);
    }

    // Obtener Puntuacion por ID
    public Optional<Puntuacion> obtenerPuntuacionPorId(Long id) {
        return puntuacionRepository.findById(id);
    }

    // Eliminar Puntuacion
    public void eliminarPuntuacion(Long id){
        puntuacionRepository.deleteById(id);
    }

    // Editar Puntuacion
    public void editarPuntuacion(Puntuacion puntuacion){
        puntuacionRepository.save(puntuacion);
    }
}

