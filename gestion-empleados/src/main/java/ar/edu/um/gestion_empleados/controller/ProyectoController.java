package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.model.Proyecto;
import ar.edu.um.gestion_empleados.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    @Autowired
    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<List<Proyecto>> getAllProyectos() {
        List<Proyecto> proyectos = proyectoService.obtenerTodos();
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> getProyectoById(@PathVariable Long id) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(id);
            return new ResponseEntity<>(proyecto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Proyecto> getProyectoByNombre(@PathVariable String nombre) {
        Optional<Proyecto> proyecto = proyectoService.buscarPorNombre(nombre);
        return proyecto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Proyecto>> getProyectosActivos() {
        List<Proyecto> proyectos = proyectoService.buscarProyectosActivos();
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Proyecto> createProyecto(@Valid @RequestBody Proyecto proyecto) {
        Proyecto nuevoProyecto = proyectoService.guardar(proyecto);
        return new ResponseEntity<>(nuevoProyecto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> updateProyecto(@PathVariable Long id, @Valid @RequestBody Proyecto proyecto) {
        try {
            Proyecto proyectoActualizado = proyectoService.actualizar(id, proyecto);
            return new ResponseEntity<>(proyectoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProyecto(@PathVariable Long id) {
        try {
            proyectoService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{proyectoId}/empleados/{empleadoId}")
    public ResponseEntity<Void> asignarEmpleado(@PathVariable Long proyectoId, @PathVariable Long empleadoId) {
        try {
            proyectoService.asignarEmpleado(proyectoId, empleadoId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{proyectoId}/empleados/{empleadoId}")
    public ResponseEntity<Void> desasignarEmpleado(@PathVariable Long proyectoId, @PathVariable Long empleadoId) {
        try {
            proyectoService.desasignarEmpleado(proyectoId, empleadoId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}