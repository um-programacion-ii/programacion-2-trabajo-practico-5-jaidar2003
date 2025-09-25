package ar.edu.um.gestion_empleados.repository;

import ar.edu.um.gestion_empleados.model.Departamento;
import ar.edu.um.gestion_empleados.model.Empleado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EmpleadoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Test
    public void whenFindById_thenReturnEmpleado() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Recursos Humanos");
        departamento.setUbicacion("Piso 3");
        entityManager.persist(departamento);
        
        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleado.setEmail("juan.perez@empresa.com");
        empleado.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        empleado.setSalario(50000.0);
        empleado.setDepartamento(departamento);
        entityManager.persist(empleado);
        entityManager.flush();

        // when
        Optional<Empleado> found = empleadoRepository.findById(empleado.getId());

        // then
        assertTrue(found.isPresent());
        assertEquals(empleado.getNombre(), found.get().getNombre());
        assertEquals(empleado.getApellido(), found.get().getApellido());
        assertEquals(empleado.getEmail(), found.get().getEmail());
    }

    @Test
    public void whenFindByEmail_thenReturnEmpleado() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Tecnología");
        departamento.setUbicacion("Piso 2");
        entityManager.persist(departamento);
        
        Empleado empleado = new Empleado();
        empleado.setNombre("Ana");
        empleado.setApellido("Martínez");
        empleado.setEmail("ana.martinez@empresa.com");
        empleado.setFechaNacimiento(LocalDate.of(1985, 8, 20));
        empleado.setSalario(60000.0);
        empleado.setDepartamento(departamento);
        entityManager.persist(empleado);
        entityManager.flush();

        // when
        Optional<Empleado> found = empleadoRepository.findByEmail("ana.martinez@empresa.com");

        // then
        assertTrue(found.isPresent());
        assertEquals(empleado.getNombre(), found.get().getNombre());
        assertEquals(empleado.getApellido(), found.get().getApellido());
    }

    @Test
    public void whenFindByDepartamento_thenReturnEmpleados() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Finanzas");
        departamento.setUbicacion("Piso 1");
        entityManager.persist(departamento);
        
        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Carlos");
        empleado1.setApellido("López");
        empleado1.setEmail("carlos.lopez@empresa.com");
        empleado1.setFechaNacimiento(LocalDate.of(1988, 3, 10));
        empleado1.setSalario(55000.0);
        empleado1.setDepartamento(departamento);
        entityManager.persist(empleado1);
        
        Empleado empleado2 = new Empleado();
        empleado2.setNombre("María");
        empleado2.setApellido("González");
        empleado2.setEmail("maria.gonzalez@empresa.com");
        empleado2.setFechaNacimiento(LocalDate.of(1992, 7, 25));
        empleado2.setSalario(52000.0);
        empleado2.setDepartamento(departamento);
        entityManager.persist(empleado2);
        
        entityManager.flush();

        // when
        List<Empleado> found = empleadoRepository.findByDepartamento(departamento);

        // then
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(e -> e.getNombre().equals("Carlos")));
        assertTrue(found.stream().anyMatch(e -> e.getNombre().equals("María")));
    }

    @Test
    public void whenFindBySalarioBetween_thenReturnEmpleados() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Marketing");
        departamento.setUbicacion("Piso 4");
        entityManager.persist(departamento);
        
        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Pedro");
        empleado1.setApellido("Sánchez");
        empleado1.setEmail("pedro.sanchez@empresa.com");
        empleado1.setFechaNacimiento(LocalDate.of(1987, 11, 5));
        empleado1.setSalario(45000.0);
        empleado1.setDepartamento(departamento);
        entityManager.persist(empleado1);
        
        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Laura");
        empleado2.setApellido("Rodríguez");
        empleado2.setEmail("laura.rodriguez@empresa.com");
        empleado2.setFechaNacimiento(LocalDate.of(1991, 4, 15));
        empleado2.setSalario(55000.0);
        empleado2.setDepartamento(departamento);
        entityManager.persist(empleado2);
        
        Empleado empleado3 = new Empleado();
        empleado3.setNombre("Roberto");
        empleado3.setApellido("Fernández");
        empleado3.setEmail("roberto.fernandez@empresa.com");
        empleado3.setFechaNacimiento(LocalDate.of(1985, 9, 30));
        empleado3.setSalario(65000.0);
        empleado3.setDepartamento(departamento);
        entityManager.persist(empleado3);
        
        entityManager.flush();

        // when
        List<Empleado> found = empleadoRepository.findBySalarioBetween(50000.0, 60000.0);

        // then
        assertEquals(1, found.size());
        assertEquals("Laura", found.get(0).getNombre());
    }

    @Test
    public void whenFindByFechaNacimientoAfter_thenReturnEmpleados() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Ventas");
        departamento.setUbicacion("Piso 5");
        entityManager.persist(departamento);
        
        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Javier");
        empleado1.setApellido("Díaz");
        empleado1.setEmail("javier.diaz@empresa.com");
        empleado1.setFechaNacimiento(LocalDate.of(1985, 6, 12));
        empleado1.setSalario(48000.0);
        empleado1.setDepartamento(departamento);
        entityManager.persist(empleado1);
        
        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Sofía");
        empleado2.setApellido("Torres");
        empleado2.setEmail("sofia.torres@empresa.com");
        empleado2.setFechaNacimiento(LocalDate.of(1990, 8, 22));
        empleado2.setSalario(52000.0);
        empleado2.setDepartamento(departamento);
        entityManager.persist(empleado2);
        
        entityManager.flush();

        // when
        List<Empleado> found = empleadoRepository.findByFechaNacimientoAfter(LocalDate.of(1988, 1, 1));

        // then
        assertEquals(1, found.size());
        assertEquals("Sofía", found.get(0).getNombre());
    }

    @Test
    public void whenFindByNombreDepartamento_thenReturnEmpleados() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Logística");
        departamento.setUbicacion("Piso 6");
        entityManager.persist(departamento);
        
        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Miguel");
        empleado1.setApellido("Ruiz");
        empleado1.setEmail("miguel.ruiz@empresa.com");
        empleado1.setFechaNacimiento(LocalDate.of(1986, 2, 18));
        empleado1.setSalario(49000.0);
        empleado1.setDepartamento(departamento);
        entityManager.persist(empleado1);
        
        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Elena");
        empleado2.setApellido("Vargas");
        empleado2.setEmail("elena.vargas@empresa.com");
        empleado2.setFechaNacimiento(LocalDate.of(1989, 10, 5));
        empleado2.setSalario(51000.0);
        empleado2.setDepartamento(departamento);
        entityManager.persist(empleado2);
        
        entityManager.flush();

        // when
        List<Empleado> found = empleadoRepository.findByNombreDepartamento("Logística");

        // then
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(e -> e.getNombre().equals("Miguel")));
        assertTrue(found.stream().anyMatch(e -> e.getNombre().equals("Elena")));
    }

    @Test
    public void whenFindAverageSalarioByDepartamento_thenReturnAverage() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Investigación");
        departamento.setUbicacion("Piso 7");
        entityManager.persist(departamento);
        
        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Daniel");
        empleado1.setApellido("Castro");
        empleado1.setEmail("daniel.castro@empresa.com");
        empleado1.setFechaNacimiento(LocalDate.of(1984, 7, 14));
        empleado1.setSalario(60000.0);
        empleado1.setDepartamento(departamento);
        entityManager.persist(empleado1);
        
        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Carmen");
        empleado2.setApellido("Ortega");
        empleado2.setEmail("carmen.ortega@empresa.com");
        empleado2.setFechaNacimiento(LocalDate.of(1987, 5, 28));
        empleado2.setSalario(70000.0);
        empleado2.setDepartamento(departamento);
        entityManager.persist(empleado2);
        
        entityManager.flush();

        // when
        Optional<Double> average = empleadoRepository.findAverageSalarioByDepartamento(departamento.getId());

        // then
        assertTrue(average.isPresent());
        assertEquals(65000.0, average.get(), 0.01);
    }
}