package ar.edu.um.gestion_empleados.service;

import ar.edu.um.gestion_empleados.model.Proyecto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProyectoService {
    
    Proyecto guardar(Proyecto proyecto);
    
    Proyecto buscarPorId(Long id);
    
    Optional<Proyecto> buscarPorNombre(String nombre);
    
    List<Proyecto> buscarProyectosActivos();
    
    List<Proyecto> obtenerTodos();
    
    Proyecto actualizar(Long id, Proyecto proyecto);
    
    void eliminar(Long id);
    
    void asignarEmpleado(Long proyectoId, Long empleadoId);
    
    void desasignarEmpleado(Long proyectoId, Long empleadoId);
}