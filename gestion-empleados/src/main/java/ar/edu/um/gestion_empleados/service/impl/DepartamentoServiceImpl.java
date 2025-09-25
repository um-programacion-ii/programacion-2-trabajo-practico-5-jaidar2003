package ar.edu.um.gestion_empleados.service.impl;

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
        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado con id: " + id));
    }

    @Override
    public Optional<Departamento> buscarPorNombre(String nombre) {
        return departamentoRepository.findByNombre(nombre);
    }

    @Override
    public List<Departamento> buscarConMasEmpleadosQue(int cantidad) {
        return departamentoRepository.findByEmpleadosCountGreaterThan(cantidad);
    }

    @Override
    public List<Departamento> obtenerTodos() {
        return departamentoRepository.findAll();
    }

    @Override
    public Departamento actualizar(Long id, Departamento departamentoActualizado) {
        Departamento departamentoExistente = buscarPorId(id);
        
        departamentoExistente.setNombre(departamentoActualizado.getNombre());
        departamentoExistente.setUbicacion(departamentoActualizado.getUbicacion());
        
        return departamentoRepository.save(departamentoExistente);
    }

    @Override
    public void eliminar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new RuntimeException("Departamento no encontrado con id: " + id);
        }
        departamentoRepository.deleteById(id);
    }
}