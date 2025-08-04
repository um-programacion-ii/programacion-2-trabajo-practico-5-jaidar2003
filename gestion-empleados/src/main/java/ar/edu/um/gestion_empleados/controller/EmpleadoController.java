package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.exception.EmailDuplicadoException;
import ar.edu.um.gestion_empleados.exception.RecursoNoEncontradoException;
import ar.edu.um.gestion_empleados.model.Empleado;
import ar.edu.um.gestion_empleados.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<Empleado>> obtenerTodos() {
        return ResponseEntity.ok(empleadoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Empleado> crear(@Valid @RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoService.guardar(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEmpleado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @Valid @RequestBody Empleado empleado) {
        Empleado empleadoActualizado = empleadoService.actualizar(id, empleado);
        return ResponseEntity.ok(empleadoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/departamento/{nombreDepartamento}")
    public ResponseEntity<List<Empleado>> obtenerPorDepartamento(@PathVariable String nombreDepartamento) {
        return ResponseEntity.ok(empleadoService.buscarPorDepartamento(nombreDepartamento));
    }

    @GetMapping("/salario")
    public ResponseEntity<List<Empleado>> obtenerPorRangoSalario(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(empleadoService.buscarPorRangoSalario(min, max));
    }

    @GetMapping("/departamento/{departamentoId}/salario-promedio")
    public ResponseEntity<Map<String, Double>> obtenerSalarioPromedioPorDepartamento(@PathVariable Long departamentoId) {
        Double salarioPromedio = empleadoService.obtenerSalarioPromedioPorDepartamento(departamentoId);
        return ResponseEntity.ok(Map.of("salarioPromedio", salarioPromedio));
    }
}