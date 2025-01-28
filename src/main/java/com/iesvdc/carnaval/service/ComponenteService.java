package com.iesvdc.carnaval.service;

import com.iesvdc.carnaval.model.Agrupacion;
import com.iesvdc.carnaval.model.Componente;
import com.iesvdc.carnaval.repository.AgrupacionRepository;
import com.iesvdc.carnaval.repository.ComponenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponenteService {

    private final ComponenteRepository componenteRepository;

    // Constructor
    public ComponenteService(ComponenteRepository componenteRepository) {
        this.componenteRepository = componenteRepository;
    }

    // Listar Componentes
    public List<Componente> listarComponentes() {
        return componenteRepository.findAll();
    }

    // Guardar Componentes
    public void guardarComponente(Componente componente) {
        componenteRepository.save(componente);
    }

    // Obtener Componentes por ID
    public Optional<Componente> obtenerComponentePorId(Long id) {
        return componenteRepository.findById(id);
    }

    // Eliminar Componente
    public void eliminarComponente(Long id){
        componenteRepository.deleteById(id);
    }

    // Editar Componente
    public void editarAgrupacion(Componente componente){
        componenteRepository.save(componente);
    }
}
