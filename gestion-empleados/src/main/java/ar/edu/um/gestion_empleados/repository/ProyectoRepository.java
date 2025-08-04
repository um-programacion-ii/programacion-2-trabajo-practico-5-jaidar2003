package ar.edu.um.gestion_empleados.repository;

import ar.edu.um.gestion_empleados.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    
    /**
     * Busca un proyecto por su nombre
     * @param nombre Nombre del proyecto
     * @return Proyecto encontrado (opcional)
     */
    Optional<Proyecto> findByNombre(String nombre);
    
    /**
     * Busca proyectos activos (con fecha de finalización posterior a hoy)
     * @return Lista de proyectos activos
     */
    @Query("SELECT p FROM Proyecto p WHERE p.fechaFinalizacion > CURRENT_DATE")
    List<Proyecto> findActiveProjects();
    
    /**
     * Busca proyectos con fecha de finalización posterior a una fecha específica
     * @param fecha Fecha de referencia
     * @return Lista de proyectos con fecha de finalización posterior a la fecha especificada
     */
    List<Proyecto> findByFechaFinalizacionAfter(LocalDate fecha);
}