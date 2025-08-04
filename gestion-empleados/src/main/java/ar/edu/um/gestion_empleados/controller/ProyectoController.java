package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.exception.RecursoNoEncontradoException;
import ar.edu.um.gestion_empleados.exception.ValidacionException;
import ar.edu.um.gestion_empleados.model.Proyecto;
import ar.edu.um.gestion_empleados.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    @Autowired
    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<List<Proyecto>> obtenerTodos() {
        return ResponseEntity.ok(proyectoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Proyecto> crear(@Valid @RequestBody Proyecto proyecto) {
        Proyecto nuevoProyecto = proyectoService.guardar(proyecto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProyecto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> actualizar(@PathVariable Long id, @Valid @RequestBody Proyecto proyecto) {
        Proyecto proyectoActualizado = proyectoService.actualizar(id, proyecto);
        return ResponseEntity.ok(proyectoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Proyecto>> obtenerProyectosActivos() {
        return ResponseEntity.ok(proyectoService.buscarProyectosActivos());
    }

    @PostMapping("/{proyectoId}/empleados/{empleadoId}")
    public ResponseEntity<Void> asignarEmpleado(
            @PathVariable Long proyectoId,
            @PathVariable Long empleadoId) {
        proyectoService.asignarEmpleado(proyectoId, empleadoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{proyectoId}/empleados/{empleadoId}")
    public ResponseEntity<Void> desasignarEmpleado(
            @PathVariable Long proyectoId,
            @PathVariable Long empleadoId) {
        proyectoService.desasignarEmpleado(proyectoId, empleadoId);
        return ResponseEntity.ok().build();
    }
}