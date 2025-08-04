package ar.edu.um.gestion_empleados.service;

import ar.edu.um.gestion_empleados.model.Departamento;

import java.util.List;
import java.util.Optional;

public interface DepartamentoService {
    
    Departamento guardar(Departamento departamento);
    
    Departamento buscarPorId(Long id);
    
    Optional<Departamento> buscarPorNombre(String nombre);
    
    List<Departamento> buscarConMasEmpleadosQue(int cantidad);
    
    List<Departamento> obtenerTodos();
    
    Departamento actualizar(Long id, Departamento departamento);
    
    void eliminar(Long id);
}