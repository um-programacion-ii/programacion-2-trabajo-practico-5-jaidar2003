package ar.edu.um.gestion_empleados.service.impl;

import ar.edu.um.gestion_empleados.exception.EmailDuplicadoException;
import ar.edu.um.gestion_empleados.exception.RecursoNoEncontradoException;
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
        if (empleado.getId() != null && empleadoRepository.findByEmail(empleado.getEmail()).isPresent()) {
            throw new EmailDuplicadoException("El email ya está registrado: " + empleado.getEmail());
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado buscarPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empleado> buscarPorEmail(String email) {
        return empleadoRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> buscarPorDepartamento(String nombreDepartamento) {
        return empleadoRepository.findByNombreDepartamento(nombreDepartamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> buscarPorRangoSalario(Double salarioMin, Double salarioMax) {
        return empleadoRepository.findBySalarioBetween(salarioMin, salarioMax);
    }

    @Override
    @Transactional(readOnly = true)
    public Double obtenerSalarioPromedioPorDepartamento(Long departamentoId) {
        return empleadoRepository.findAverageSalarioByDepartamento(departamentoId)
                .orElse(0.0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado actualizar(Long id, Empleado empleado) {
        if (!empleadoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Empleado no encontrado con ID: " + id);
        }
        
        // Verificar si el email ya está en uso por otro empleado
        Optional<Empleado> empleadoExistente = empleadoRepository.findByEmail(empleado.getEmail());
        if (empleadoExistente.isPresent() && !empleadoExistente.get().getId().equals(id)) {
            throw new EmailDuplicadoException("El email ya está registrado por otro empleado: " + empleado.getEmail());
        }
        
        empleado.setId(id);
        return empleadoRepository.save(empleado);
    }

    @Override
    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Empleado no encontrado con ID: " + id);
        }
        empleadoRepository.deleteById(id);
    }
}
