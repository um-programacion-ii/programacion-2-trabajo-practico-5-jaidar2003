package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.model.Empleado;
import ar.edu.um.gestion_empleados.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<Empleado>> getAllEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerTodos();
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> getEmpleadoById(@PathVariable Long id) {
        try {
            Empleado empleado = empleadoService.buscarPorId(id);
            return new ResponseEntity<>(empleado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Empleado> getEmpleadoByEmail(@PathVariable String email) {
        Optional<Empleado> empleado = empleadoService.buscarPorEmail(email);
        return empleado.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/departamento/{nombreDepartamento}")
    public ResponseEntity<List<Empleado>> getEmpleadosByDepartamento(@PathVariable String nombreDepartamento) {
        List<Empleado> empleados = empleadoService.buscarPorDepartamento(nombreDepartamento);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @GetMapping("/salario")
    public ResponseEntity<List<Empleado>> getEmpleadosBySalarioRange(
            @RequestParam Double min, @RequestParam Double max) {
        List<Empleado> empleados = empleadoService.buscarPorRangoSalario(min, max);
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    @GetMapping("/departamento/{id}/salario-promedio")
    public ResponseEntity<Double> getAverageSalarioByDepartamento(@PathVariable Long id) {
        Double promedio = empleadoService.obtenerSalarioPromedioPorDepartamento(id);
        return new ResponseEntity<>(promedio, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Empleado> createEmpleado(@Valid @RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoService.guardar(empleado);
        return new ResponseEntity<>(nuevoEmpleado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> updateEmpleado(@PathVariable Long id, @Valid @RequestBody Empleado empleado) {
        try {
            Empleado empleadoActualizado = empleadoService.actualizar(id, empleado);
            return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long id) {
        try {
            empleadoService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
