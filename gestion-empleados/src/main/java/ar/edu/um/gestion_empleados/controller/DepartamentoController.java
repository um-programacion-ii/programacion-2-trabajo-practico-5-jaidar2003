package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.exception.RecursoNoEncontradoException;
import ar.edu.um.gestion_empleados.exception.ValidacionException;
import ar.edu.um.gestion_empleados.model.Departamento;
import ar.edu.um.gestion_empleados.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @Autowired
    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public ResponseEntity<List<Departamento>> obtenerTodos() {
        return ResponseEntity.ok(departamentoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamento> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(departamentoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Departamento> crear(@Valid @RequestBody Departamento departamento) {
        Departamento nuevoDepartamento = departamentoService.guardar(departamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDepartamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> actualizar(@PathVariable Long id, @Valid @RequestBody Departamento departamento) {
        Departamento departamentoActualizado = departamentoService.actualizar(id, departamento);
        return ResponseEntity.ok(departamentoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        departamentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/con-mas-empleados/{cantidad}")
    public ResponseEntity<List<Departamento>> obtenerConMasEmpleadosQue(@PathVariable int cantidad) {
        return ResponseEntity.ok(departamentoService.buscarConMasEmpleadosQue(cantidad));
    }
}