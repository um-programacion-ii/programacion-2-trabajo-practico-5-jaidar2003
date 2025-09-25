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
public class DepartamentoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Test
    public void whenFindById_thenReturnDepartamento() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Recursos Humanos");
        departamento.setUbicacion("Piso 3");
        entityManager.persist(departamento);
        entityManager.flush();

        // when
        Optional<Departamento> found = departamentoRepository.findById(departamento.getId());

        // then
        assertTrue(found.isPresent());
        assertEquals(departamento.getNombre(), found.get().getNombre());
        assertEquals(departamento.getUbicacion(), found.get().getUbicacion());
    }

    @Test
    public void whenFindByNombre_thenReturnDepartamento() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Tecnología");
        departamento.setUbicacion("Piso 2");
        entityManager.persist(departamento);
        entityManager.flush();

        // when
        Optional<Departamento> found = departamentoRepository.findByNombre("Tecnología");

        // then
        assertTrue(found.isPresent());
        assertEquals(departamento.getId(), found.get().getId());
        assertEquals(departamento.getUbicacion(), found.get().getUbicacion());
    }

    @Test
    public void whenFindByEmpleadosCountGreaterThan_thenReturnDepartamentos() {
        // given
        // Departamento 1 con 3 empleados
        Departamento departamento1 = new Departamento();
        departamento1.setNombre("Finanzas");
        departamento1.setUbicacion("Piso 1");
        entityManager.persist(departamento1);

        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Carlos");
        empleado1.setApellido("López");
        empleado1.setEmail("carlos.lopez@empresa.com");
        empleado1.setFechaNacimiento(LocalDate.of(1988, 3, 10));
        empleado1.setSalario(55000.0);
        empleado1.setDepartamento(departamento1);
        entityManager.persist(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("María");
        empleado2.setApellido("González");
        empleado2.setEmail("maria.gonzalez@empresa.com");
        empleado2.setFechaNacimiento(LocalDate.of(1992, 7, 25));
        empleado2.setSalario(52000.0);
        empleado2.setDepartamento(departamento1);
        entityManager.persist(empleado2);

        Empleado empleado3 = new Empleado();
        empleado3.setNombre("Pedro");
        empleado3.setApellido("Sánchez");
        empleado3.setEmail("pedro.sanchez@empresa.com");
        empleado3.setFechaNacimiento(LocalDate.of(1987, 11, 5));
        empleado3.setSalario(58000.0);
        empleado3.setDepartamento(departamento1);
        entityManager.persist(empleado3);

        // Departamento 2 con 1 empleado
        Departamento departamento2 = new Departamento();
        departamento2.setNombre("Marketing");
        departamento2.setUbicacion("Piso 4");
        entityManager.persist(departamento2);

        Empleado empleado4 = new Empleado();
        empleado4.setNombre("Laura");
        empleado4.setApellido("Rodríguez");
        empleado4.setEmail("laura.rodriguez@empresa.com");
        empleado4.setFechaNacimiento(LocalDate.of(1991, 4, 15));
        empleado4.setSalario(54000.0);
        empleado4.setDepartamento(departamento2);
        entityManager.persist(empleado4);

        // Departamento 3 con 2 empleados
        Departamento departamento3 = new Departamento();
        departamento3.setNombre("Ventas");
        departamento3.setUbicacion("Piso 5");
        entityManager.persist(departamento3);

        Empleado empleado5 = new Empleado();
        empleado5.setNombre("Javier");
        empleado5.setApellido("Díaz");
        empleado5.setEmail("javier.diaz@empresa.com");
        empleado5.setFechaNacimiento(LocalDate.of(1985, 6, 12));
        empleado5.setSalario(56000.0);
        empleado5.setDepartamento(departamento3);
        entityManager.persist(empleado5);

        Empleado empleado6 = new Empleado();
        empleado6.setNombre("Sofía");
        empleado6.setApellido("Torres");
        empleado6.setEmail("sofia.torres@empresa.com");
        empleado6.setFechaNacimiento(LocalDate.of(1990, 8, 22));
        empleado6.setSalario(53000.0);
        empleado6.setDepartamento(departamento3);
        entityManager.persist(empleado6);

        entityManager.flush();

        // when
        List<Departamento> found = departamentoRepository.findByEmpleadosCountGreaterThan(1);

        // then
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(d -> d.getNombre().equals("Finanzas")));
        assertTrue(found.stream().anyMatch(d -> d.getNombre().equals("Ventas")));
        assertFalse(found.stream().anyMatch(d -> d.getNombre().equals("Marketing")));
    }

    @Test
    public void whenSaveDepartamento_thenDepartamentoIsSaved() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Investigación");
        departamento.setUbicacion("Piso 7");

        // when
        Departamento saved = departamentoRepository.save(departamento);
        entityManager.flush();

        // then
        assertNotNull(saved.getId());
        assertEquals("Investigación", saved.getNombre());
        assertEquals("Piso 7", saved.getUbicacion());

        // Verify it's in the database
        Departamento fromDb = entityManager.find(Departamento.class, saved.getId());
        assertNotNull(fromDb);
        assertEquals(departamento.getNombre(), fromDb.getNombre());
    }

    @Test
    public void whenDeleteDepartamento_thenDepartamentoIsRemoved() {
        // given
        Departamento departamento = new Departamento();
        departamento.setNombre("Logística");
        departamento.setUbicacion("Piso 6");
        entityManager.persist(departamento);
        entityManager.flush();

        // when
        departamentoRepository.deleteById(departamento.getId());
        entityManager.flush();

        // then
        Departamento fromDb = entityManager.find(Departamento.class, departamento.getId());
        assertNull(fromDb);
    }
}