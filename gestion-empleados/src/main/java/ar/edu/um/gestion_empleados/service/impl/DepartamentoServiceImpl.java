package ar.edu.um.gestion_empleados.service.impl;

import ar.edu.um.gestion_empleados.exception.RecursoNoEncontradoException;
import ar.edu.um.gestion_empleados.exception.ValidacionException;
import ar.edu.um.gestion_empleados.model.Departamento;
import ar.edu.um.gestion_empleados.repository.DepartamentoRepository;
import ar.edu.um.gestion_empleados.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    @Autowired
    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Departamento guardar(Departamento departamento) {
        // Verificar si ya existe un departamento con el mismo nombre
        if (departamento.getId() == null && 
            departamentoRepository.findByNombre(departamento.getNombre()).isPresent()) {
            throw new ValidacionException("Ya existe un departamento con el nombre: " + departamento.getNombre());
        }
        return departamentoRepository.save(departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Departamento no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Departamento> buscarPorNombre(String nombre) {
        return departamentoRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Departamento> buscarConMasEmpleadosQue(int cantidad) {
        return departamentoRepository.findByEmpleadosCountGreaterThan(cantidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Departamento> obtenerTodos() {
        return departamentoRepository.findAll();
    }

    @Override
    public Departamento actualizar(Long id, Departamento departamento) {
        if (!departamentoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Departamento no encontrado con ID: " + id);
        }
        
        // Verificar si el nombre ya está en uso por otro departamento
        Optional<Departamento> departamentoExistente = departamentoRepository.findByNombre(departamento.getNombre());
        if (departamentoExistente.isPresent() && !departamentoExistente.get().getId().equals(id)) {
            throw new ValidacionException("Ya existe un departamento con el nombre: " + departamento.getNombre());
        }
        
        departamento.setId(id);
        return departamentoRepository.save(departamento);
    }

    @Override
    public void eliminar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Departamento no encontrado con ID: " + id);
        }
        
        Departamento departamento = departamentoRepository.findById(id).get();
        if (!departamento.getEmpleados().isEmpty()) {
            throw new ValidacionException("No se puede eliminar un departamento que tiene empleados asignados");
        }
        
        departamentoRepository.deleteById(id);
    }
}
