package com.iesvdc.carnaval.service;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.repository.AgrupacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Logica de negocio: CRUD
 *
 */
@Service
public class AgrupacionService {

    private final AgrupacionRepository agrupacionRepository;

    // Constructor
    public AgrupacionService(AgrupacionRepository agrupacionRepository) {
        this.agrupacionRepository = agrupacionRepository;
    }

    // Listar Agrupaciones
    public List<Agrupacion> listarAgrupaciones() {
        return agrupacionRepository.findAll();
    }

    // Guardar Agrupaciones
    public void guardarAgrupacion(Agrupacion agrupacion) {
        agrupacionRepository.save(agrupacion);
    }

    // Obtener Agrupacion por ID
    public Optional<Agrupacion> obtenerAgrupacionPorId(Long id) {
        return agrupacionRepository.findById(id);
    }

    // Eliminar Agrupacion
    public void eliminarAgrupacion(Long id){
        agrupacionRepository.deleteById(id);
    }

    // Editar Agrupacion
    public void editarAgrupacion(Agrupacion agrupacion){
        agrupacionRepository.save(agrupacion);
    }






}
