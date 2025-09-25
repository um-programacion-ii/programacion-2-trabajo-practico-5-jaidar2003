package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.model.Empleado;
import ar.edu.um.gestion_empleados.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
@Tag(name = "Empleado", description = "API para la gestión de empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @Operation(summary = "Obtener todos los empleados", description = "Retorna una lista de todos los empleados registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empleado.class)))
    })
    @GetMapping
    public ResponseEntity<List<Empleado>> getAllEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerTodos();
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un empleado por ID", description = "Retorna un empleado según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empleado.class))),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Empleado> getEmpleadoById(
            @Parameter(description = "ID del empleado a buscar") @PathVariable Long id) {
        try {
            Empleado empleado = empleadoService.buscarPorId(id);
            return new ResponseEntity<>(empleado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Obtener un empleado por email", description = "Retorna un empleado según el email proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empleado.class))),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
                content = @Content)
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<Empleado> getEmpleadoByEmail(
            @Parameter(description = "Email del empleado a buscar") @PathVariable String email) {
        Optional<Empleado> empleado = empleadoService.buscarPorEmail(email);
        return empleado.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtener empleados por departamento", description = "Retorna una lista de empleados que pertenecen al departamento especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empleado.class)))
    })
    @GetMapping("/departamento/{nombreDepartamento}")
    public ResponseEntity<List<Empleado>> getEmpleadosByDepartamento(
            @Parameter(description = "Nombre del departamento") @PathVariable String nombreDepartamento) {
        List<Empleado> empleados = empleadoService.buscarPorDepartamento(nombreDepartamento);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @Operation(summary = "Obtener empleados por rango de salario", description = "Retorna una lista de empleados cuyo salario está dentro del rango especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empleado.class)))
    })
    @GetMapping("/salario")
    public ResponseEntity<List<Empleado>> getEmpleadosBySalarioRange(
            @Parameter(description = "Salario mínimo") @RequestParam Double min, 
            @Parameter(description = "Salario máximo") @RequestParam Double max) {
        List<Empleado> empleados = empleadoService.buscarPorRangoSalario(min, max);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @Operation(summary = "Obtener salario promedio por departamento", description = "Retorna el salario promedio de los empleados de un departamento específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salario promedio calculado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(type = "number", format = "double")))
    })
    @GetMapping("/departamento/{id}/salario-promedio")
    public ResponseEntity<Double> getAverageSalarioByDepartamento(
            @Parameter(description = "ID del departamento") @PathVariable Long id) {
        Double promedio = empleadoService.obtenerSalarioPromedioPorDepartamento(id);
        return new ResponseEntity<>(promedio, HttpStatus.OK);
    }

    @Operation(summary = "Crear un nuevo empleado", description = "Crea un nuevo empleado con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Empleado creado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empleado.class))),
        @ApiResponse(responseCode = "400", description = "Datos de empleado inválidos",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<Empleado> createEmpleado(
            @Parameter(description = "Datos del empleado a crear", required = true) 
            @Valid @RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoService.guardar(empleado);
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un empleado existente", description = "Actualiza los datos de un empleado existente según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado actualizado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Empleado.class))),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos de empleado inválidos",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> updateEmpleado(
            @Parameter(description = "ID del empleado a actualizar", required = true) @PathVariable Long id, 
            @Parameter(description = "Datos actualizados del empleado", required = true) 
            @Valid @RequestBody Empleado empleado) {
        try {
            Empleado empleadoActualizado = empleadoService.actualizar(id, empleado);
            return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un empleado", description = "Elimina un empleado según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Empleado eliminado correctamente",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleado(
            @Parameter(description = "ID del empleado a eliminar", required = true) @PathVariable Long id) {
        try {
            empleadoService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
