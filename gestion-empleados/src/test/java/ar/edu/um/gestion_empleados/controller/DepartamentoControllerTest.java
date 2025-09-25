package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.model.Departamento;
import ar.edu.um.gestion_empleados.service.DepartamentoService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DepartamentoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartamentoService departamentoService;

    @InjectMocks
    private DepartamentoController departamentoController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Departamento departamento;

    @BeforeEach
    public void setup() {
        // Set up MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(departamentoController).build();
        
        // Configure ObjectMapper for serialization
        objectMapper.findAndRegisterModules();
        
        // Set up test data
        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("Recursos Humanos");
        departamento.setUbicacion("Piso 3");
    }

    @Test
    public void getAllDepartamentos_shouldReturnDepartamentosList() throws Exception {
        List<Departamento> departamentos = Arrays.asList(departamento);
        when(departamentoService.obtenerTodos()).thenReturn(departamentos);

        mockMvc.perform(get("/api/departamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Recursos Humanos")))
                .andExpect(jsonPath("$[0].ubicacion", is("Piso 3")));

        verify(departamentoService, times(1)).obtenerTodos();
    }

    @Test
    public void getDepartamentoById_shouldReturnDepartamento() throws Exception {
        when(departamentoService.buscarPorId(1L)).thenReturn(departamento);

        mockMvc.perform(get("/api/departamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Recursos Humanos")))
                .andExpect(jsonPath("$.ubicacion", is("Piso 3")));

        verify(departamentoService, times(1)).buscarPorId(1L);
    }

    @Test
    public void getDepartamentoById_whenNotFound_shouldReturnNotFound() throws Exception {
        when(departamentoService.buscarPorId(99L)).thenThrow(new RuntimeException("Departamento no encontrado"));

        mockMvc.perform(get("/api/departamentos/99"))
                .andExpect(status().isNotFound());

        verify(departamentoService, times(1)).buscarPorId(99L);
    }

    @Test
    public void getDepartamentoByNombre_shouldReturnDepartamento() throws Exception {
        when(departamentoService.buscarPorNombre("Recursos Humanos")).thenReturn(Optional.of(departamento));

        mockMvc.perform(get("/api/departamentos/nombre/Recursos Humanos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Recursos Humanos")))
                .andExpect(jsonPath("$.ubicacion", is("Piso 3")));

        verify(departamentoService, times(1)).buscarPorNombre("Recursos Humanos");
    }

    @Test
    public void getDepartamentoByNombre_whenNotFound_shouldReturnNotFound() throws Exception {
        when(departamentoService.buscarPorNombre("No Existe")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/departamentos/nombre/No Existe"))
                .andExpect(status().isNotFound());

        verify(departamentoService, times(1)).buscarPorNombre("No Existe");
    }

    @Test
    public void getDepartamentosConMasEmpleadosQue_shouldReturnDepartamentos() throws Exception {
        List<Departamento> departamentos = Arrays.asList(departamento);
        when(departamentoService.buscarConMasEmpleadosQue(5)).thenReturn(departamentos);

        mockMvc.perform(get("/api/departamentos/con-empleados-mas-que/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Recursos Humanos")));

        verify(departamentoService, times(1)).buscarConMasEmpleadosQue(5);
    }

    @Test
    public void createDepartamento_shouldReturnCreatedDepartamento() throws Exception {
        when(departamentoService.guardar(any(Departamento.class))).thenReturn(departamento);

        mockMvc.perform(post("/api/departamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Recursos Humanos")))
                .andExpect(jsonPath("$.ubicacion", is("Piso 3")));

        verify(departamentoService, times(1)).guardar(any(Departamento.class));
    }

    @Test
    public void updateDepartamento_shouldReturnUpdatedDepartamento() throws Exception {
        Departamento departamentoActualizado = new Departamento();
        departamentoActualizado.setId(1L);
        departamentoActualizado.setNombre("Recursos Humanos y Administración");
        departamentoActualizado.setUbicacion("Piso 4");

        when(departamentoService.actualizar(eq(1L), any(Departamento.class))).thenReturn(departamentoActualizado);

        mockMvc.perform(put("/api/departamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departamentoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Recursos Humanos y Administración")))
                .andExpect(jsonPath("$.ubicacion", is("Piso 4")));

        verify(departamentoService, times(1)).actualizar(eq(1L), any(Departamento.class));
    }

    @Test
    public void updateDepartamento_whenNotFound_shouldReturnNotFound() throws Exception {
        when(departamentoService.actualizar(eq(99L), any(Departamento.class))).thenThrow(new RuntimeException("Departamento no encontrado"));

        mockMvc.perform(put("/api/departamentos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departamento)))
                .andExpect(status().isNotFound());

        verify(departamentoService, times(1)).actualizar(eq(99L), any(Departamento.class));
    }

    @Test
    public void deleteDepartamento_shouldReturnNoContent() throws Exception {
        doNothing().when(departamentoService).eliminar(1L);

        mockMvc.perform(delete("/api/departamentos/1"))
                .andExpect(status().isNoContent());

        verify(departamentoService, times(1)).eliminar(1L);
    }

    @Test
    public void deleteDepartamento_whenNotFound_shouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Departamento no encontrado")).when(departamentoService).eliminar(99L);

        mockMvc.perform(delete("/api/departamentos/99"))
                .andExpect(status().isNotFound());

        verify(departamentoService, times(1)).eliminar(99L);
    }
}