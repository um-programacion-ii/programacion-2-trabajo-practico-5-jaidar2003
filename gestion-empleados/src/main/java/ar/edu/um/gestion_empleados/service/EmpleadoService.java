package ar.edu.um.gestion_empleados.service;

import ar.edu.um.gestion_empleados.model.Empleado;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    
    Empleado guardar(Empleado empleado);
    
    Empleado buscarPorId(Long id);
    
    Optional<Empleado> buscarPorEmail(String email);
    
    List<Empleado> buscarPorDepartamento(String nombreDepartamento);
    
    List<Empleado> buscarPorRangoSalario(Double salarioMin, Double salarioMax);
    
    Double obtenerSalarioPromedioPorDepartamento(Long departamentoId);
    
    List<Empleado> obtenerTodos();
    
    Empleado actualizar(Long id, Empleado empleado);
    
    void eliminar(Long id);
}