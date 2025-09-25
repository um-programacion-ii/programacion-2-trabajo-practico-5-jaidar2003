package ar.edu.um.gestion_empleados.repository;

import ar.edu.um.gestion_empleados.model.Proyecto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProyectoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Test
    public void whenFindById_thenReturnProyecto() {
        // given
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Sistema de Gestión");
        proyecto.setDescripcion("Sistema para gestionar recursos empresariales");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFinalizacion(LocalDate.now().plusMonths(6));
        entityManager.persist(proyecto);
        entityManager.flush();

        // when
        Optional<Proyecto> found = proyectoRepository.findById(proyecto.getId());

        // then
        assertTrue(found.isPresent());
        assertEquals(proyecto.getNombre(), found.get().getNombre());
        assertEquals(proyecto.getDescripcion(), found.get().getDescripcion());
    }

    @Test
    public void whenFindByNombre_thenReturnProyecto() {
        // given
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Aplicación Móvil");
        proyecto.setDescripcion("Aplicación para clientes");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFinalizacion(LocalDate.now().plusMonths(3));
        entityManager.persist(proyecto);
        entityManager.flush();

        // when
        Optional<Proyecto> found = proyectoRepository.findByNombre("Aplicación Móvil");

        // then
        assertTrue(found.isPresent());
        assertEquals(proyecto.getId(), found.get().getId());
        assertEquals(proyecto.getDescripcion(), found.get().getDescripcion());
    }

    @Test
    public void whenFindActiveProjects_thenReturnActiveProyectos() {
        // given
        // Proyecto activo 1 (fecha de finalización en el futuro)
        Proyecto proyectoActivo1 = new Proyecto();
        proyectoActivo1.setNombre("Proyecto Web");
        proyectoActivo1.setDescripcion("Desarrollo de sitio web");
        proyectoActivo1.setFechaInicio(LocalDate.now().minusMonths(1));
        proyectoActivo1.setFechaFinalizacion(LocalDate.now().plusMonths(2));
        entityManager.persist(proyectoActivo1);

        // Proyecto activo 2 (fecha de finalización en el futuro)
        Proyecto proyectoActivo2 = new Proyecto();
        proyectoActivo2.setNombre("Migración de Datos");
        proyectoActivo2.setDescripcion("Migración a nueva plataforma");
        proyectoActivo2.setFechaInicio(LocalDate.now());
        proyectoActivo2.setFechaFinalizacion(LocalDate.now().plusMonths(1));
        entityManager.persist(proyectoActivo2);

        entityManager.flush();

        // when
        List<Proyecto> found = proyectoRepository.findActiveProjects();

        // then
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(p -> p.getNombre().equals("Proyecto Web")));
        assertTrue(found.stream().anyMatch(p -> p.getNombre().equals("Migración de Datos")));
    }

    @Test
    public void whenFindByFechaFinalizacionAfter_thenReturnProyectos() {
        // given
        LocalDate fechaReferencia = LocalDate.now().plusMonths(1);

        // Proyecto con fecha de finalización después de la referencia
        Proyecto proyecto1 = new Proyecto();
        proyecto1.setNombre("Proyecto Largo");
        proyecto1.setDescripcion("Proyecto de larga duración");
        proyecto1.setFechaInicio(LocalDate.now());
        proyecto1.setFechaFinalizacion(LocalDate.now().plusMonths(3));
        entityManager.persist(proyecto1);

        // Proyecto con fecha de finalización antes de la referencia
        Proyecto proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto Corto");
        proyecto2.setDescripcion("Proyecto de corta duración");
        proyecto2.setFechaInicio(LocalDate.now());
        proyecto2.setFechaFinalizacion(LocalDate.now().plusDays(15));
        entityManager.persist(proyecto2);

        entityManager.flush();

        // when
        List<Proyecto> found = proyectoRepository.findByFechaFinalizacionAfter(fechaReferencia);

        // then
        assertEquals(1, found.size());
        assertEquals("Proyecto Largo", found.get(0).getNombre());
    }

    @Test
    public void whenSaveProyecto_thenProyectoIsSaved() {
        // given
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Nuevo Sistema");
        proyecto.setDescripcion("Desarrollo de nuevo sistema");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFinalizacion(LocalDate.now().plusMonths(4));

        // when
        Proyecto saved = proyectoRepository.save(proyecto);
        entityManager.flush();

        // then
        assertNotNull(saved.getId());
        assertEquals("Nuevo Sistema", saved.getNombre());
        assertEquals("Desarrollo de nuevo sistema", saved.getDescripcion());

        // Verify it's in the database
        Proyecto fromDb = entityManager.find(Proyecto.class, saved.getId());
        assertNotNull(fromDb);
        assertEquals(proyecto.getNombre(), fromDb.getNombre());
    }

    @Test
    public void whenDeleteProyecto_thenProyectoIsRemoved() {
        // given
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto a Eliminar");
        proyecto.setDescripcion("Este proyecto será eliminado");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFinalizacion(LocalDate.now().plusMonths(2));
        entityManager.persist(proyecto);
        entityManager.flush();

        // when
        proyectoRepository.deleteById(proyecto.getId());
        entityManager.flush();

        // then
        Proyecto fromDb = entityManager.find(Proyecto.class, proyecto.getId());
        assertNull(fromDb);
    }
}
