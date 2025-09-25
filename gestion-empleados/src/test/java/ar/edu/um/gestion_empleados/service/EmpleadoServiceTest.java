package ar.edu.um.gestion_empleados.service;

import ar.edu.um.gestion_empleados.model.Departamento;
import ar.edu.um.gestion_empleados.model.Empleado;
import ar.edu.um.gestion_empleados.repository.DepartamentoRepository;
import ar.edu.um.gestion_empleados.repository.EmpleadoRepository;
import ar.edu.um.gestion_empleados.service.impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;
    private Departamento departamento;

    @BeforeEach
    public void setup() {
        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Recursos Humanos");
        departamento.setUbicacion("Piso 3");

        empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleado.setEmail("juan.perez@empresa.com");
        empleado.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        empleado.setSalario(50000.0);
        empleado.setDepartamento(departamento);
    }

    @Test
    public void whenGuardarEmpleado_thenReturnEmpleado() {
        // given
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        // when
        Empleado saved = empleadoService.guardar(empleado);

        // then
        assertNotNull(saved);
        assertEquals(empleado.getNombre(), saved.getNombre());
        assertEquals(empleado.getApellido(), saved.getApellido());
        verify(empleadoRepository, times(1)).save(empleado);
    }

    @Test
    public void whenBuscarPorId_thenReturnEmpleado() {
        // given
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));

        // when
        Empleado found = empleadoService.buscarPorId(1L);

        // then
        assertNotNull(found);
        assertEquals(empleado.getNombre(), found.getNombre());
        assertEquals(empleado.getApellido(), found.getApellido());
        verify(empleadoRepository, times(1)).findById(1L);
    }

    @Test
    public void whenBuscarPorIdNoExiste_thenThrowException() {
        // given
        when(empleadoRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            empleadoService.buscarPorId(99L);
        });
        verify(empleadoRepository, times(1)).findById(99L);
    }

    @Test
    public void whenBuscarPorEmail_thenReturnEmpleado() {
        // given
        when(empleadoRepository.findByEmail("juan.perez@empresa.com")).thenReturn(Optional.of(empleado));

        // when
        Optional<Empleado> found = empleadoService.buscarPorEmail("juan.perez@empresa.com");

        // then
        assertTrue(found.isPresent());
        assertEquals(empleado.getNombre(), found.get().getNombre());
        assertEquals(empleado.getApellido(), found.get().getApellido());
        verify(empleadoRepository, times(1)).findByEmail("juan.perez@empresa.com");
    }

    @Test
    public void whenBuscarPorDepartamento_thenReturnEmpleados() {
        // given
        List<Empleado> empleados = Arrays.asList(empleado);
        when(empleadoRepository.findByNombreDepartamento("Recursos Humanos")).thenReturn(empleados);

        // when
        List<Empleado> found = empleadoService.buscarPorDepartamento("Recursos Humanos");

        // then
        assertEquals(1, found.size());
        assertEquals(empleado.getNombre(), found.get(0).getNombre());
        verify(empleadoRepository, times(1)).findByNombreDepartamento("Recursos Humanos");
    }

    @Test
    public void whenBuscarPorRangoSalario_thenReturnEmpleados() {
        // given
        List<Empleado> empleados = Arrays.asList(empleado);
        when(empleadoRepository.findBySalarioBetween(40000.0, 60000.0)).thenReturn(empleados);

        // when
        List<Empleado> found = empleadoService.buscarPorRangoSalario(40000.0, 60000.0);

        // then
        assertEquals(1, found.size());
        assertEquals(empleado.getNombre(), found.get(0).getNombre());
        verify(empleadoRepository, times(1)).findBySalarioBetween(40000.0, 60000.0);
    }

    @Test
    public void whenObtenerSalarioPromedioPorDepartamento_thenReturnPromedio() {
        // given
        when(empleadoRepository.findAverageSalarioByDepartamento(1L)).thenReturn(Optional.of(55000.0));

        // when
        Double promedio = empleadoService.obtenerSalarioPromedioPorDepartamento(1L);

        // then
        assertEquals(55000.0, promedio);
        verify(empleadoRepository, times(1)).findAverageSalarioByDepartamento(1L);
    }

    @Test
    public void whenObtenerTodos_thenReturnEmpleados() {
        // given
        List<Empleado> empleados = Arrays.asList(empleado);
        when(empleadoRepository.findAll()).thenReturn(empleados);

        // when
        List<Empleado> found = empleadoService.obtenerTodos();

        // then
        assertEquals(1, found.size());
        assertEquals(empleado.getNombre(), found.get(0).getNombre());
        verify(empleadoRepository, times(1)).findAll();
    }

    @Test
    public void whenActualizar_thenReturnEmpleadoActualizado() {
        // given
        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setId(1L);
        empleadoActualizado.setNombre("Juan Carlos");
        empleadoActualizado.setApellido("Pérez González");
        empleadoActualizado.setEmail("juancarlos.perez@empresa.com");
        empleadoActualizado.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        empleadoActualizado.setSalario(55000.0);
        empleadoActualizado.setDepartamento(departamento);

        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleadoActualizado);

        // when
        Empleado updated = empleadoService.actualizar(1L, empleadoActualizado);

        // then
        assertNotNull(updated);
        assertEquals("Juan Carlos", updated.getNombre());
        assertEquals("Pérez González", updated.getApellido());
        assertEquals("juancarlos.perez@empresa.com", updated.getEmail());
        assertEquals(55000.0, updated.getSalario());
        verify(empleadoRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).save(any(Empleado.class));
    }

    @Test
    public void whenEliminar_thenRepositoryMethodCalled() {
        // given
        when(empleadoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(empleadoRepository).deleteById(1L);

        // when
        empleadoService.eliminar(1L);

        // then
        verify(empleadoRepository, times(1)).existsById(1L);
        verify(empleadoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void whenEliminarNoExiste_thenThrowException() {
        // given
        when(empleadoRepository.existsById(99L)).thenReturn(false);

        // when & then
        assertThrows(RuntimeException.class, () -> {
            empleadoService.eliminar(99L);
        });
        verify(empleadoRepository, times(1)).existsById(99L);
        verify(empleadoRepository, never()).deleteById(99L);
    }
}