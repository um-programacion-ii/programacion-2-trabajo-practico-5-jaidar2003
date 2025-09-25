package ar.edu.um.gestion_empleados.service;

import ar.edu.um.gestion_empleados.model.Departamento;
import ar.edu.um.gestion_empleados.repository.DepartamentoRepository;
import ar.edu.um.gestion_empleados.service.impl.DepartamentoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartamentoServiceTest {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @InjectMocks
    private DepartamentoServiceImpl departamentoService;

    private Departamento departamento;

    @BeforeEach
    public void setup() {
        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Recursos Humanos");
        departamento.setUbicacion("Piso 3");
    }

    @Test
    public void whenGuardarDepartamento_thenReturnDepartamento() {
        // given
        when(departamentoRepository.save(any(Departamento.class))).thenReturn(departamento);

        // when
        Departamento saved = departamentoService.guardar(departamento);

        // then
        assertNotNull(saved);
        assertEquals(departamento.getNombre(), saved.getNombre());
        assertEquals(departamento.getUbicacion(), saved.getUbicacion());
        verify(departamentoRepository, times(1)).save(departamento);
    }

    @Test
    public void whenBuscarPorId_thenReturnDepartamento() {
        // given
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));

        // when
        Departamento found = departamentoService.buscarPorId(1L);

        // then
        assertNotNull(found);
        assertEquals(departamento.getNombre(), found.getNombre());
        assertEquals(departamento.getUbicacion(), found.getUbicacion());
        verify(departamentoRepository, times(1)).findById(1L);
    }

    @Test
    public void whenBuscarPorIdNoExiste_thenThrowException() {
        // given
        when(departamentoRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            departamentoService.buscarPorId(99L);
        });
        verify(departamentoRepository, times(1)).findById(99L);
    }

    @Test
    public void whenBuscarPorNombre_thenReturnDepartamento() {
        // given
        when(departamentoRepository.findByNombre("Recursos Humanos")).thenReturn(Optional.of(departamento));

        // when
        Optional<Departamento> found = departamentoService.buscarPorNombre("Recursos Humanos");

        // then
        assertTrue(found.isPresent());
        assertEquals(departamento.getId(), found.get().getId());
        assertEquals(departamento.getUbicacion(), found.get().getUbicacion());
        verify(departamentoRepository, times(1)).findByNombre("Recursos Humanos");
    }

    @Test
    public void whenBuscarConMasEmpleadosQue_thenReturnDepartamentos() {
        // given
        List<Departamento> departamentos = Arrays.asList(departamento);
        when(departamentoRepository.findByEmpleadosCountGreaterThan(5)).thenReturn(departamentos);

        // when
        List<Departamento> found = departamentoService.buscarConMasEmpleadosQue(5);

        // then
        assertEquals(1, found.size());
        assertEquals(departamento.getNombre(), found.get(0).getNombre());
        verify(departamentoRepository, times(1)).findByEmpleadosCountGreaterThan(5);
    }

    @Test
    public void whenObtenerTodos_thenReturnDepartamentos() {
        // given
        List<Departamento> departamentos = Arrays.asList(departamento);
        when(departamentoRepository.findAll()).thenReturn(departamentos);

        // when
        List<Departamento> found = departamentoService.obtenerTodos();

        // then
        assertEquals(1, found.size());
        assertEquals(departamento.getNombre(), found.get(0).getNombre());
        verify(departamentoRepository, times(1)).findAll();
    }

    @Test
    public void whenActualizar_thenReturnDepartamentoActualizado() {
        // given
        Departamento departamentoActualizado = new Departamento();
        departamentoActualizado.setId(1L);
        departamentoActualizado.setNombre("Recursos Humanos y Administración");
        departamentoActualizado.setUbicacion("Piso 4");

        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(departamento));
        when(departamentoRepository.save(any(Departamento.class))).thenReturn(departamentoActualizado);

        // when
        Departamento updated = departamentoService.actualizar(1L, departamentoActualizado);

        // then
        assertNotNull(updated);
        assertEquals("Recursos Humanos y Administración", updated.getNombre());
        assertEquals("Piso 4", updated.getUbicacion());
        verify(departamentoRepository, times(1)).findById(1L);
        verify(departamentoRepository, times(1)).save(any(Departamento.class));
    }

    @Test
    public void whenEliminar_thenRepositoryMethodCalled() {
        // given
        when(departamentoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(departamentoRepository).deleteById(1L);

        // when
        departamentoService.eliminar(1L);

        // then
        verify(departamentoRepository, times(1)).existsById(1L);
        verify(departamentoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void whenEliminarNoExiste_thenThrowException() {
        // given
        when(departamentoRepository.existsById(99L)).thenReturn(false);

        // when & then
        assertThrows(RuntimeException.class, () -> {
            departamentoService.eliminar(99L);
        });
        verify(departamentoRepository, times(1)).existsById(99L);
        verify(departamentoRepository, never()).deleteById(99L);
    }
}