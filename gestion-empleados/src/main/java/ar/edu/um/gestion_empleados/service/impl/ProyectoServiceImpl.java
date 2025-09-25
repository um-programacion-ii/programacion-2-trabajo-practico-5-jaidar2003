package ar.edu.um.gestion_empleados.service.impl;

import ar.edu.um.gestion_empleados.model.Empleado;
import ar.edu.um.gestion_empleados.model.Proyecto;
import ar.edu.um.gestion_empleados.repository.EmpleadoRepository;
import ar.edu.um.gestion_empleados.repository.ProyectoRepository;
import ar.edu.um.gestion_empleados.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public ProyectoServiceImpl(ProyectoRepository proyectoRepository, EmpleadoRepository empleadoRepository) {
        this.proyectoRepository = proyectoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto buscarPorId(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
    }

    @Override
    public Optional<Proyecto> buscarPorNombre(String nombre) {
        return proyectoRepository.findByNombre(nombre);
    }

    @Override
    public List<Proyecto> buscarProyectosActivos() {
        return proyectoRepository.findActiveProjects();
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public Proyecto actualizar(Long id, Proyecto proyectoActualizado) {
        Proyecto proyectoExistente = buscarPorId(id);
        
        proyectoExistente.setNombre(proyectoActualizado.getNombre());
        proyectoExistente.setDescripcion(proyectoActualizado.getDescripcion());
        proyectoExistente.setFechaInicio(proyectoActualizado.getFechaInicio());
        proyectoExistente.setFechaFinalizacion(proyectoActualizado.getFechaFinalizacion());
        
        return proyectoRepository.save(proyectoExistente);
    }

    @Override
    public void eliminar(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new RuntimeException("Proyecto no encontrado con id: " + id);
        }
        proyectoRepository.deleteById(id);
    }

    @Override
    public void asignarEmpleado(Long proyectoId, Long empleadoId) {
        Proyecto proyecto = buscarPorId(proyectoId);
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con id: " + empleadoId));
        
        empleado.getProyectos().add(proyecto);
        empleadoRepository.save(empleado);
    }

    @Override
    public void desasignarEmpleado(Long proyectoId, Long empleadoId) {
        Proyecto proyecto = buscarPorId(proyectoId);
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con id: " + empleadoId));
        
        empleado.getProyectos().remove(proyecto);
        empleadoRepository.save(empleado);
    }
}