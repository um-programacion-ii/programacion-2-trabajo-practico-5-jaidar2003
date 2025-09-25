package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.model.Departamento;
import ar.edu.um.gestion_empleados.model.Empleado;
import ar.edu.um.gestion_empleados.service.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EmpleadoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmpleadoService empleadoService;

    @InjectMocks
    private EmpleadoController empleadoController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Empleado empleado;
    private Departamento departamento;

    @BeforeEach
    public void setup() {
        // Set up MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(empleadoController).build();

        // Configure ObjectMapper for LocalDate serialization
        objectMapper.findAndRegisterModules();

        // Set up test data
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
    public void getAllEmpleados_shouldReturnEmpleadosList() throws Exception {
        List<Empleado> empleados = Arrays.asList(empleado);
        when(empleadoService.obtenerTodos()).thenReturn(empleados);

        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")))
                .andExpect(jsonPath("$[0].apellido", is("Pérez")));

        verify(empleadoService, times(1)).obtenerTodos();
    }

    @Test
    public void getEmpleadoById_shouldReturnEmpleado() throws Exception {
        when(empleadoService.buscarPorId(1L)).thenReturn(empleado);

        mockMvc.perform(get("/api/empleados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")));

        verify(empleadoService, times(1)).buscarPorId(1L);
    }

    @Test
    public void getEmpleadoById_whenNotFound_shouldReturnNotFound() throws Exception {
        when(empleadoService.buscarPorId(99L)).thenThrow(new RuntimeException("Empleado no encontrado"));

        mockMvc.perform(get("/api/empleados/99"))
                .andExpect(status().isNotFound());

        verify(empleadoService, times(1)).buscarPorId(99L);
    }

    @Test
    public void getEmpleadoByEmail_shouldReturnEmpleado() throws Exception {
        when(empleadoService.buscarPorEmail("juan.perez@empresa.com")).thenReturn(Optional.of(empleado));

        mockMvc.perform(get("/api/empleados/email/juan.perez@empresa.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")));

        verify(empleadoService, times(1)).buscarPorEmail("juan.perez@empresa.com");
    }

    @Test
    public void getEmpleadoByEmail_whenNotFound_shouldReturnNotFound() throws Exception {
        when(empleadoService.buscarPorEmail("noexiste@empresa.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/empleados/email/noexiste@empresa.com"))
                .andExpect(status().isNotFound());

        verify(empleadoService, times(1)).buscarPorEmail("noexiste@empresa.com");
    }

    @Test
    public void getEmpleadosByDepartamento_shouldReturnEmpleados() throws Exception {
        List<Empleado> empleados = Arrays.asList(empleado);
        when(empleadoService.buscarPorDepartamento("Recursos Humanos")).thenReturn(empleados);

        mockMvc.perform(get("/api/empleados/departamento/Recursos Humanos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")));

        verify(empleadoService, times(1)).buscarPorDepartamento("Recursos Humanos");
    }

    @Test
    public void getEmpleadosBySalarioRange_shouldReturnEmpleados() throws Exception {
        List<Empleado> empleados = Arrays.asList(empleado);
        when(empleadoService.buscarPorRangoSalario(40000.0, 60000.0)).thenReturn(empleados);

        mockMvc.perform(get("/api/empleados/salario")
                .param("min", "40000.0")
                .param("max", "60000.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")));

        verify(empleadoService, times(1)).buscarPorRangoSalario(40000.0, 60000.0);
    }

    @Test
    public void getAverageSalarioByDepartamento_shouldReturnAverage() throws Exception {
        when(empleadoService.obtenerSalarioPromedioPorDepartamento(1L)).thenReturn(55000.0);

        mockMvc.perform(get("/api/empleados/departamento/1/salario-promedio"))
                .andExpect(status().isOk())
                .andExpect(content().string("55000.0"));

        verify(empleadoService, times(1)).obtenerSalarioPromedioPorDepartamento(1L);
    }

    @Test
    public void createEmpleado_shouldReturnCreatedEmpleado() throws Exception {
        when(empleadoService.guardar(any(Empleado.class))).thenReturn(empleado);

        mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")));

        verify(empleadoService, times(1)).guardar(any(Empleado.class));
    }

    @Test
    public void updateEmpleado_shouldReturnUpdatedEmpleado() throws Exception {
        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setId(1L);
        empleadoActualizado.setNombre("Juan Carlos");
        empleadoActualizado.setApellido("Pérez González");
        empleadoActualizado.setEmail("juancarlos.perez@empresa.com");
        empleadoActualizado.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        empleadoActualizado.setSalario(55000.0);
        empleadoActualizado.setDepartamento(departamento);

        when(empleadoService.actualizar(eq(1L), any(Empleado.class))).thenReturn(empleadoActualizado);

        mockMvc.perform(put("/api/empleados/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan Carlos")))
                .andExpect(jsonPath("$.apellido", is("Pérez González")));

        verify(empleadoService, times(1)).actualizar(eq(1L), any(Empleado.class));
    }

    @Test
    public void updateEmpleado_whenNotFound_shouldReturnNotFound() throws Exception {
        when(empleadoService.actualizar(eq(99L), any(Empleado.class))).thenThrow(new RuntimeException("Empleado no encontrado"));

        mockMvc.perform(put("/api/empleados/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)))
                .andExpect(status().isNotFound());

        verify(empleadoService, times(1)).actualizar(eq(99L), any(Empleado.class));
    }

    @Test
    public void deleteEmpleado_shouldReturnNoContent() throws Exception {
        doNothing().when(empleadoService).eliminar(1L);

        mockMvc.perform(delete("/api/empleados/1"))
                .andExpect(status().isNoContent());

        verify(empleadoService, times(1)).eliminar(1L);
    }

    @Test
    public void deleteEmpleado_whenNotFound_shouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Empleado no encontrado")).when(empleadoService).eliminar(99L);

        mockMvc.perform(delete("/api/empleados/99"))
                .andExpect(status().isNotFound());

        verify(empleadoService, times(1)).eliminar(99L);
    }
}
