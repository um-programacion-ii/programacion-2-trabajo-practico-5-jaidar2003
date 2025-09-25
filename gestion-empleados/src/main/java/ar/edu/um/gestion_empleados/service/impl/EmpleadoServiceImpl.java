package ar.edu.um.gestion_empleados.service.impl;

import ar.edu.um.gestion_empleados.model.Empleado;
import ar.edu.um.gestion_empleados.repository.EmpleadoRepository;
import ar.edu.um.gestion_empleados.repository.DepartamentoRepository;
import ar.edu.um.gestion_empleados.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoRepository departamentoRepository;

    @Autowired
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository, DepartamentoRepository departamentoRepository) {
        this.empleadoRepository = empleadoRepository;
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Empleado guardar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado buscarPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con id: " + id));
    }

    @Override
    public Optional<Empleado> buscarPorEmail(String email) {
        return empleadoRepository.findByEmail(email);
    }

    @Override
    public List<Empleado> buscarPorDepartamento(String nombreDepartamento) {
        return empleadoRepository.findByNombreDepartamento(nombreDepartamento);
    }

    @Override
    public List<Empleado> buscarPorRangoSalario(Double salarioMin, Double salarioMax) {
        return empleadoRepository.findBySalarioBetween(salarioMin, salarioMax);
    }

    @Override
    public Double obtenerSalarioPromedioPorDepartamento(Long departamentoId) {
        return empleadoRepository.findAverageSalarioByDepartamento(departamentoId)
                .orElse(0.0);
    }

    @Override
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado actualizar(Long id, Empleado empleadoActualizado) {
        Empleado empleadoExistente = buscarPorId(id);
        
        empleadoExistente.setNombre(empleadoActualizado.getNombre());
        empleadoExistente.setApellido(empleadoActualizado.getApellido());
        empleadoExistente.setEmail(empleadoActualizado.getEmail());
        empleadoExistente.setFechaNacimiento(empleadoActualizado.getFechaNacimiento());
        empleadoExistente.setSalario(empleadoActualizado.getSalario());
        
        if (empleadoActualizado.getDepartamento() != null) {
            empleadoExistente.setDepartamento(empleadoActualizado.getDepartamento());
        }
        
        return empleadoRepository.save(empleadoExistente);
    }

    @Override
    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado con id: " + id);
        }
        empleadoRepository.deleteById(id);
    }
}