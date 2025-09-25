package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.model.Proyecto;
import ar.edu.um.gestion_empleados.service.ProyectoService;
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
@RequestMapping("/api/proyectos")
@Tag(name = "Proyecto", description = "API para la gestión de proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    @Autowired
    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @Operation(summary = "Obtener todos los proyectos", description = "Retorna una lista de todos los proyectos registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de proyectos obtenida correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class)))
    })
    @GetMapping
    public ResponseEntity<List<Proyecto>> getAllProyectos() {
        List<Proyecto> proyectos = proyectoService.obtenerTodos();
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un proyecto por ID", description = "Retorna un proyecto según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyecto encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class))),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> getProyectoById(
            @Parameter(description = "ID del proyecto a buscar") @PathVariable Long id) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(id);
            return new ResponseEntity<>(proyecto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Obtener un proyecto por nombre", description = "Retorna un proyecto según el nombre proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyecto encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class))),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado",
                content = @Content)
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Proyecto> getProyectoByNombre(
            @Parameter(description = "Nombre del proyecto a buscar") @PathVariable String nombre) {
        Optional<Proyecto> proyecto = proyectoService.buscarPorNombre(nombre);
        return proyecto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtener proyectos activos", description = "Retorna una lista de proyectos que están actualmente activos (fecha de finalización posterior a hoy)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de proyectos activos obtenida correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class)))
    })
    @GetMapping("/activos")
    public ResponseEntity<List<Proyecto>> getProyectosActivos() {
        List<Proyecto> proyectos = proyectoService.buscarProyectosActivos();
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @Operation(summary = "Crear un nuevo proyecto", description = "Crea un nuevo proyecto con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Proyecto creado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de proyecto inválidos",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<Proyecto> createProyecto(
            @Parameter(description = "Datos del proyecto a crear", required = true) 
            @Valid @RequestBody Proyecto proyecto) {
        Proyecto nuevoProyecto = proyectoService.guardar(proyecto);
        return new ResponseEntity<>(nuevoProyecto, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un proyecto existente", description = "Actualiza los datos de un proyecto existente según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyecto actualizado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Proyecto.class))),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Datos de proyecto inválidos",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> updateProyecto(
            @Parameter(description = "ID del proyecto a actualizar", required = true) @PathVariable Long id, 
            @Parameter(description = "Datos actualizados del proyecto", required = true) 
            @Valid @RequestBody Proyecto proyecto) {
        try {
            Proyecto proyectoActualizado = proyectoService.actualizar(id, proyecto);
            return new ResponseEntity<>(proyectoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un proyecto", description = "Elimina un proyecto según el ID proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Proyecto eliminado correctamente",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProyecto(
            @Parameter(description = "ID del proyecto a eliminar", required = true) @PathVariable Long id) {
        try {
            proyectoService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Asignar un empleado a un proyecto", description = "Asigna un empleado existente a un proyecto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado asignado correctamente al proyecto",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Proyecto o empleado no encontrado",
                content = @Content)
    })
    @PostMapping("/{proyectoId}/empleados/{empleadoId}")
    public ResponseEntity<Void> asignarEmpleado(
            @Parameter(description = "ID del proyecto", required = true) @PathVariable Long proyectoId, 
            @Parameter(description = "ID del empleado a asignar", required = true) @PathVariable Long empleadoId) {
        try {
            proyectoService.asignarEmpleado(proyectoId, empleadoId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Desasignar un empleado de un proyecto", description = "Elimina la asignación de un empleado a un proyecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empleado desasignado correctamente del proyecto",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Proyecto o empleado no encontrado",
                content = @Content)
    })
    @DeleteMapping("/{proyectoId}/empleados/{empleadoId}")
    public ResponseEntity<Void> desasignarEmpleado(
            @Parameter(description = "ID del proyecto", required = true) @PathVariable Long proyectoId, 
            @Parameter(description = "ID del empleado a desasignar", required = true) @PathVariable Long empleadoId) {
        try {
            proyectoService.desasignarEmpleado(proyectoId, empleadoId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
