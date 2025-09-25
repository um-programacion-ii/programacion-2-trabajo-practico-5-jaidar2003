package ar.edu.um.gestion_empleados.controller;

import ar.edu.um.gestion_empleados.model.Departamento;
import ar.edu.um.gestion_empleados.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @Autowired
    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public ResponseEntity<List<Departamento>> getAllDepartamentos() {
        List<Departamento> departamentos = departamentoService.obtenerTodos();
        return new ResponseEntity<>(departamentos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamento> getDepartamentoById(@PathVariable Long id) {
        try {
            Departamento departamento = departamentoService.buscarPorId(id);
            return new ResponseEntity<>(departamento, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Departamento> getDepartamentoByNombre(@PathVariable String nombre) {
        Optional<Departamento> departamento = departamentoService.buscarPorNombre(nombre);
        return departamento.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/con-empleados-mas-que/{cantidad}")
    public ResponseEntity<List<Departamento>> getDepartamentosConMasEmpleadosQue(@PathVariable int cantidad) {
        List<Departamento> departamentos = departamentoService.buscarConMasEmpleadosQue(cantidad);
        return new ResponseEntity<>(departamentos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Departamento> createDepartamento(@Valid @RequestBody Departamento departamento) {
        Departamento nuevoDepartamento = departamentoService.guardar(departamento);
        return new ResponseEntity<>(nuevoDepartamento, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> updateDepartamento(@PathVariable Long id, @Valid @RequestBody Departamento departamento) {
        try {
            Departamento departamentoActualizado = departamentoService.actualizar(id, departamento);
            return new ResponseEntity<>(departamentoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable Long id) {
        try {
            departamentoService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}