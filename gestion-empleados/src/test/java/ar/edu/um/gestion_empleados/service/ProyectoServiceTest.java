package ar.edu.um.gestion_empleados.service;

import ar.edu.um.gestion_empleados.model.Empleado;
import ar.edu.um.gestion_empleados.model.Proyecto;
import ar.edu.um.gestion_empleados.repository.EmpleadoRepository;
import ar.edu.um.gestion_empleados.repository.ProyectoRepository;
import ar.edu.um.gestion_empleados.service.impl.ProyectoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProyectoServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private ProyectoServiceImpl proyectoService;

    private Proyecto proyecto;
    private Empleado empleado;

    @BeforeEach
    public void setup() {
        proyecto = new Proyecto();
        proyecto.setId(1L);
        proyecto.setNombre("Sistema de Gestión");
        proyecto.setDescripcion("Sistema para gestionar recursos empresariales");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFinalizacion(LocalDate.now().plusMonths(6));
        proyecto.setEmpleados(new HashSet<>());

        empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleado.setEmail("juan.perez@empresa.com");
        empleado.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        empleado.setSalario(50000.0);
        empleado.setProyectos(new HashSet<>());
    }

    @Test
    public void whenGuardarProyecto_thenReturnProyecto() {
        // given
        when(proyectoRepository.save(any(Proyecto.class))).thenReturn(proyecto);

        // when
        Proyecto saved = proyectoService.guardar(proyecto);

        // then
        assertNotNull(saved);
        assertEquals(proyecto.getNombre(), saved.getNombre());
        assertEquals(proyecto.getDescripcion(), saved.getDescripcion());
        verify(proyectoRepository, times(1)).save(proyecto);
    }

    @Test
    public void whenBuscarPorId_thenReturnProyecto() {
        // given
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        // when
        Proyecto found = proyectoService.buscarPorId(1L);

        // then
        assertNotNull(found);
        assertEquals(proyecto.getNombre(), found.getNombre());
        assertEquals(proyecto.getDescripcion(), found.getDescripcion());
        verify(proyectoRepository, times(1)).findById(1L);
    }

    @Test
    public void whenBuscarPorIdNoExiste_thenThrowException() {
        // given
        when(proyectoRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            proyectoService.buscarPorId(99L);
        });
        verify(proyectoRepository, times(1)).findById(99L);
    }

    @Test
    public void whenBuscarPorNombre_thenReturnProyecto() {
        // given
        when(proyectoRepository.findByNombre("Sistema de Gestión")).thenReturn(Optional.of(proyecto));

        // when
        Optional<Proyecto> found = proyectoService.buscarPorNombre("Sistema de Gestión");

        // then
        assertTrue(found.isPresent());
        assertEquals(proyecto.getId(), found.get().getId());
        assertEquals(proyecto.getDescripcion(), found.get().getDescripcion());
        verify(proyectoRepository, times(1)).findByNombre("Sistema de Gestión");
    }

    @Test
    public void whenBuscarProyectosActivos_thenReturnProyectos() {
        // given
        List<Proyecto> proyectos = Arrays.asList(proyecto);
        when(proyectoRepository.findActiveProjects()).thenReturn(proyectos);

        // when
        List<Proyecto> found = proyectoService.buscarProyectosActivos();

        // then
        assertEquals(1, found.size());
        assertEquals(proyecto.getNombre(), found.get(0).getNombre());
        verify(proyectoRepository, times(1)).findActiveProjects();
    }

    @Test
    public void whenObtenerTodos_thenReturnProyectos() {
        // given
        List<Proyecto> proyectos = Arrays.asList(proyecto);
        when(proyectoRepository.findAll()).thenReturn(proyectos);

        // when
        List<Proyecto> found = proyectoService.obtenerTodos();

        // then
        assertEquals(1, found.size());
        assertEquals(proyecto.getNombre(), found.get(0).getNombre());
        verify(proyectoRepository, times(1)).findAll();
    }

    @Test
    public void whenActualizar_thenReturnProyectoActualizado() {
        // given
        Proyecto proyectoActualizado = new Proyecto();
        proyectoActualizado.setId(1L);
        proyectoActualizado.setNombre("Sistema de Gestión v2");
        proyectoActualizado.setDescripcion("Sistema actualizado para gestionar recursos empresariales");
        proyectoActualizado.setFechaInicio(LocalDate.now());
        proyectoActualizado.setFechaFinalizacion(LocalDate.now().plusMonths(8));

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(proyectoRepository.save(any(Proyecto.class))).thenReturn(proyectoActualizado);

        // when
        Proyecto updated = proyectoService.actualizar(1L, proyectoActualizado);

        // then
        assertNotNull(updated);
        assertEquals("Sistema de Gestión v2", updated.getNombre());
        assertEquals("Sistema actualizado para gestionar recursos empresariales", updated.getDescripcion());
        verify(proyectoRepository, times(1)).findById(1L);
        verify(proyectoRepository, times(1)).save(any(Proyecto.class));
    }

    @Test
    public void whenEliminar_thenRepositoryMethodCalled() {
        // given
        when(proyectoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(proyectoRepository).deleteById(1L);

        // when
        proyectoService.eliminar(1L);

        // then
        verify(proyectoRepository, times(1)).existsById(1L);
        verify(proyectoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void whenEliminarNoExiste_thenThrowException() {
        // given
        when(proyectoRepository.existsById(99L)).thenReturn(false);

        // when & then
        assertThrows(RuntimeException.class, () -> {
            proyectoService.eliminar(99L);
        });
        verify(proyectoRepository, times(1)).existsById(99L);
        verify(proyectoRepository, never()).deleteById(99L);
    }

    @Test
    public void whenAsignarEmpleado_thenEmpleadoAsignado() {
        // given
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        // when
        proyectoService.asignarEmpleado(1L, 1L);

        // then
        verify(proyectoRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).save(empleado);
    }

    @Test
    public void whenAsignarEmpleadoProyectoNoExiste_thenThrowException() {
        // given
        when(proyectoRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            proyectoService.asignarEmpleado(99L, 1L);
        });
        verify(proyectoRepository, times(1)).findById(99L);
        verify(empleadoRepository, never()).findById(anyLong());
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @Test
    public void whenAsignarEmpleadoEmpleadoNoExiste_thenThrowException() {
        // given
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(empleadoRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            proyectoService.asignarEmpleado(1L, 99L);
        });
        verify(proyectoRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).findById(99L);
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @Test
    public void whenDesasignarEmpleado_thenEmpleadoDesasignado() {
        // given
        empleado.getProyectos().add(proyecto);
        
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        // when
        proyectoService.desasignarEmpleado(1L, 1L);

        // then
        verify(proyectoRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).save(empleado);
    }

    @Test
    public void whenDesasignarEmpleadoProyectoNoExiste_thenThrowException() {
        // given
        when(proyectoRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            proyectoService.desasignarEmpleado(99L, 1L);
        });
        verify(proyectoRepository, times(1)).findById(99L);
        verify(empleadoRepository, never()).findById(anyLong());
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @Test
    public void whenDesasignarEmpleadoEmpleadoNoExiste_thenThrowException() {
        // given
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(empleadoRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            proyectoService.desasignarEmpleado(1L, 99L);
        });
        verify(proyectoRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).findById(99L);
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }
}