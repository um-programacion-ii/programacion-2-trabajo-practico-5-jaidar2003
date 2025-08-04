package ar.edu.um.gestion_empleados.service.impl;

import ar.edu.um.gestion_empleados.exception.RecursoNoEncontradoException;
import ar.edu.um.gestion_empleados.exception.ValidacionException;
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
import java.util.Set;

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
        // Verificar si ya existe un proyecto con el mismo nombre
        if (proyecto.getId() == null && 
            proyectoRepository.findByNombre(proyecto.getNombre()).isPresent()) {
            throw new ValidacionException("Ya existe un proyecto con el nombre: " + proyecto.getNombre());
        }
        return proyectoRepository.save(proyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public Proyecto buscarPorId(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proyecto> buscarPorNombre(String nombre) {
        return proyectoRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> buscarProyectosActivos() {
        return proyectoRepository.findActiveProjects();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public Proyecto actualizar(Long id, Proyecto proyecto) {
        if (!proyectoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Proyecto no encontrado con ID: " + id);
        }
        
        // Verificar si el nombre ya está en uso por otro proyecto
        Optional<Proyecto> proyectoExistente = proyectoRepository.findByNombre(proyecto.getNombre());
        if (proyectoExistente.isPresent() && !proyectoExistente.get().getId().equals(id)) {
            throw new ValidacionException("Ya existe un proyecto con el nombre: " + proyecto.getNombre());
        }
        
        // Preservar la lista de empleados
        Proyecto proyectoActual = proyectoRepository.findById(id).get();
        proyecto.setEmpleados(proyectoActual.getEmpleados());
        
        proyecto.setId(id);
        return proyectoRepository.save(proyecto);
    }

    @Override
    public void eliminar(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Proyecto no encontrado con ID: " + id);
        }
        
        // Antes de eliminar, desasociar todos los empleados
        Proyecto proyecto = proyectoRepository.findById(id).get();
        Set<Empleado> empleados = proyecto.getEmpleados();
        
        for (Empleado empleado : empleados) {
            empleado.getProyectos().remove(proyecto);
            empleadoRepository.save(empleado);
        }
        
        proyectoRepository.deleteById(id);
    }

    @Override
    public void asignarEmpleado(Long proyectoId, Long empleadoId) {
        Proyecto proyecto = buscarPorId(proyectoId);
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado con ID: " + empleadoId));
        
        // Agregar el proyecto al empleado
        empleado.getProyectos().add(proyecto);
        empleadoRepository.save(empleado);
    }

    @Override
    public void desasignarEmpleado(Long proyectoId, Long empleadoId) {
        Proyecto proyecto = buscarPorId(proyectoId);
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado con ID: " + empleadoId));
        
        // Remover el proyecto del empleado
        empleado.getProyectos().remove(proyecto);
        empleadoRepository.save(empleado);
    }
}
