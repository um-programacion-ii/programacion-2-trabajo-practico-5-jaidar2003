package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.model.Departamento;
import ar.edu.um.gestion_empleados.service.DepartamentoService;
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
@RequestMapping("/api/departamentos")
@Tag(name = "Departamento", description = "API para la gestión de departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @Autowired
    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @Operation(summary = "Obtener todos los departamentos", description = "Retorna una lista de todos los departamentos registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de departamentos obtenida correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class)))
    })
    @GetMapping
    public ResponseEntity<List<Departamento>> getAllDepartamentos() {
        List<Departamento> departamentos = departamentoService.obtenerTodos();
        return new ResponseEntity<>(departamentos, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un departamento por ID", description = "Retorna un departamento según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Departamento encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class))),
        @ApiResponse(responseCode = "404", description = "Departamento no encontrado",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Departamento> getDepartamentoById(
            @Parameter(description = "ID del departamento a buscar") @PathVariable Long id) {
        try {
            Departamento departamento = departamentoService.buscarPorId(id);
            return new ResponseEntity<>(departamento, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Obtener un departamento por nombre", description = "Retorna un departamento según el nombre proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Departamento encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class))),
        @ApiResponse(responseCode = "404", description = "Departamento no encontrado",
                content = @Content)
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Departamento> getDepartamentoByNombre(
            @Parameter(description = "Nombre del departamento a buscar") @PathVariable String nombre) {
        Optional<Departamento> departamento = departamentoService.buscarPorNombre(nombre);
        return departamento.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtener departamentos con más de cierta cantidad de empleados", 
               description = "Retorna una lista de departamentos que tienen más de la cantidad especificada de empleados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de departamentos obtenida correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class)))
    })
    @GetMapping("/con-empleados-mas-que/{cantidad}")
    public ResponseEntity<List<Departamento>> getDepartamentosConMasEmpleadosQue(
            @Parameter(description = "Cantidad mínima de empleados") @PathVariable int cantidad) {
        List<Departamento> departamentos = departamentoService.buscarConMasEmpleadosQue(cantidad);
        return new ResponseEntity<>(departamentos, HttpStatus.OK);
    }

    @Operation(summary = "Crear un nuevo departamento", description = "Crea un nuevo departamento con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Departamento creado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class))),
        @ApiResponse(responseCode = "400", description = "Datos de departamento inválidos",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<Departamento> createDepartamento(
            @Parameter(description = "Datos del departamento a crear", required = true) 
            @Valid @RequestBody Departamento departamento) {
        Departamento nuevoDepartamento = departamentoService.guardar(departamento);
        return new ResponseEntity<>(nuevoDepartamento, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un departamento existente", description = "Actualiza los datos de un departamento existente según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Departamento actualizado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class))),
        @ApiResponse(responseCode = "404", description = "Departamento no encontrado",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos de departamento inválidos",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Departamento> updateDepartamento(
            @Parameter(description = "ID del departamento a actualizar", required = true) @PathVariable Long id, 
            @Parameter(description = "Datos actualizados del departamento", required = true) 
            @Valid @RequestBody Departamento departamento) {
        try {
            Departamento departamentoActualizado = departamentoService.actualizar(id, departamento);
            return new ResponseEntity<>(departamentoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un departamento", description = "Elimina un departamento según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Departamento eliminado correctamente",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Departamento no encontrado",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamento(
            @Parameter(description = "ID del departamento a eliminar", required = true) @PathVariable Long id) {
        try {
            departamentoService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
