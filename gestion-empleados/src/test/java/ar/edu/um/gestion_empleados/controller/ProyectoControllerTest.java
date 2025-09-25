package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.model.Empleado;
import ar.edu.um.gestion_empleados.model.Proyecto;
import ar.edu.um.gestion_empleados.service.ProyectoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProyectoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProyectoService proyectoService;

    @InjectMocks
    private ProyectoController proyectoController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Proyecto proyecto;
    private Empleado empleado;

    @BeforeEach
    public void setup() {
        // Set up MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(proyectoController).build();
        
        // Configure ObjectMapper for LocalDate serialization
        objectMapper.findAndRegisterModules();
        
        // Set up test data
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
    public void getAllProyectos_shouldReturnProyectosList() throws Exception {
        List<Proyecto> proyectos = Arrays.asList(proyecto);
        when(proyectoService.obtenerTodos()).thenReturn(proyectos);

        mockMvc.perform(get("/api/proyectos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Sistema de Gestión")))
                .andExpect(jsonPath("$[0].descripcion", is("Sistema para gestionar recursos empresariales")));

        verify(proyectoService, times(1)).obtenerTodos();
    }

    @Test
    public void getProyectoById_shouldReturnProyecto() throws Exception {
        when(proyectoService.buscarPorId(1L)).thenReturn(proyecto);

        mockMvc.perform(get("/api/proyectos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Sistema de Gestión")))
                .andExpect(jsonPath("$.descripcion", is("Sistema para gestionar recursos empresariales")));

        verify(proyectoService, times(1)).buscarPorId(1L);
    }

    @Test
    public void getProyectoById_whenNotFound_shouldReturnNotFound() throws Exception {
        when(proyectoService.buscarPorId(99L)).thenThrow(new RuntimeException("Proyecto no encontrado"));

        mockMvc.perform(get("/api/proyectos/99"))
                .andExpect(status().isNotFound());

        verify(proyectoService, times(1)).buscarPorId(99L);
    }

    @Test
    public void getProyectoByNombre_shouldReturnProyecto() throws Exception {
        when(proyectoService.buscarPorNombre("Sistema de Gestión")).thenReturn(Optional.of(proyecto));

        mockMvc.perform(get("/api/proyectos/nombre/Sistema de Gestión"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Sistema de Gestión")))
                .andExpect(jsonPath("$.descripcion", is("Sistema para gestionar recursos empresariales")));

        verify(proyectoService, times(1)).buscarPorNombre("Sistema de Gestión");
    }

    @Test
    public void getProyectoByNombre_whenNotFound_shouldReturnNotFound() throws Exception {
        when(proyectoService.buscarPorNombre("No Existe")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/proyectos/nombre/No Existe"))
                .andExpect(status().isNotFound());

        verify(proyectoService, times(1)).buscarPorNombre("No Existe");
    }

    @Test
    public void getProyectosActivos_shouldReturnProyectos() throws Exception {
        List<Proyecto> proyectos = Arrays.asList(proyecto);
        when(proyectoService.buscarProyectosActivos()).thenReturn(proyectos);

        mockMvc.perform(get("/api/proyectos/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Sistema de Gestión")));

        verify(proyectoService, times(1)).buscarProyectosActivos();
    }

    @Test
    public void createProyecto_shouldReturnCreatedProyecto() throws Exception {
        when(proyectoService.guardar(any(Proyecto.class))).thenReturn(proyecto);

        mockMvc.perform(post("/api/proyectos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Sistema de Gestión")))
                .andExpect(jsonPath("$.descripcion", is("Sistema para gestionar recursos empresariales")));

        verify(proyectoService, times(1)).guardar(any(Proyecto.class));
    }

    @Test
    public void updateProyecto_shouldReturnUpdatedProyecto() throws Exception {
        Proyecto proyectoActualizado = new Proyecto();
        proyectoActualizado.setId(1L);
        proyectoActualizado.setNombre("Sistema de Gestión v2");
        proyectoActualizado.setDescripcion("Sistema actualizado para gestionar recursos empresariales");
        proyectoActualizado.setFechaInicio(LocalDate.now());
        proyectoActualizado.setFechaFinalizacion(LocalDate.now().plusMonths(8));

        when(proyectoService.actualizar(eq(1L), any(Proyecto.class))).thenReturn(proyectoActualizado);

        mockMvc.perform(put("/api/proyectos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proyectoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Sistema de Gestión v2")))
                .andExpect(jsonPath("$.descripcion", is("Sistema actualizado para gestionar recursos empresariales")));

        verify(proyectoService, times(1)).actualizar(eq(1L), any(Proyecto.class));
    }

    @Test
    public void updateProyecto_whenNotFound_shouldReturnNotFound() throws Exception {
        when(proyectoService.actualizar(eq(99L), any(Proyecto.class))).thenThrow(new RuntimeException("Proyecto no encontrado"));

        mockMvc.perform(put("/api/proyectos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proyecto)))
                .andExpect(status().isNotFound());

        verify(proyectoService, times(1)).actualizar(eq(99L), any(Proyecto.class));
    }

    @Test
    public void deleteProyecto_shouldReturnNoContent() throws Exception {
        doNothing().when(proyectoService).eliminar(1L);

        mockMvc.perform(delete("/api/proyectos/1"))
                .andExpect(status().isNoContent());

        verify(proyectoService, times(1)).eliminar(1L);
    }

    @Test
    public void deleteProyecto_whenNotFound_shouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Proyecto no encontrado")).when(proyectoService).eliminar(99L);

        mockMvc.perform(delete("/api/proyectos/99"))
                .andExpect(status().isNotFound());

        verify(proyectoService, times(1)).eliminar(99L);
    }

    @Test
    public void asignarEmpleado_shouldReturnOk() throws Exception {
        doNothing().when(proyectoService).asignarEmpleado(1L, 1L);

        mockMvc.perform(post("/api/proyectos/1/empleados/1"))
                .andExpect(status().isOk());

        verify(proyectoService, times(1)).asignarEmpleado(1L, 1L);
    }

    @Test
    public void asignarEmpleado_whenNotFound_shouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Proyecto o empleado no encontrado")).when(proyectoService).asignarEmpleado(99L, 1L);

        mockMvc.perform(post("/api/proyectos/99/empleados/1"))
                .andExpect(status().isNotFound());

        verify(proyectoService, times(1)).asignarEmpleado(99L, 1L);
    }

    @Test
    public void desasignarEmpleado_shouldReturnOk() throws Exception {
        doNothing().when(proyectoService).desasignarEmpleado(1L, 1L);

        mockMvc.perform(delete("/api/proyectos/1/empleados/1"))
                .andExpect(status().isOk());

        verify(proyectoService, times(1)).desasignarEmpleado(1L, 1L);
    }

    @Test
    public void desasignarEmpleado_whenNotFound_shouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Proyecto o empleado no encontrado")).when(proyectoService).desasignarEmpleado(99L, 1L);

        mockMvc.perform(delete("/api/proyectos/99/empleados/1"))
                .andExpect(status().isNotFound());

        verify(proyectoService, times(1)).desasignarEmpleado(99L, 1L);
    }
}