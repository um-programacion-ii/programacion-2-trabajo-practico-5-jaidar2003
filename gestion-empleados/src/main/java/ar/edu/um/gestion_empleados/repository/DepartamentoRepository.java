package ar.edu.um.gestion_empleados.repository;

import ar.edu.um.gestion_empleados.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    
    /**
     * Busca un departamento por su nombre
     * @param nombre Nombre del departamento
     * @return Departamento encontrado (opcional)
     */
    Optional<Departamento> findByNombre(String nombre);
    
    /**
     * Busca departamentos que tengan más de una cantidad específica de empleados
     * @param cantidad Cantidad mínima de empleados
     * @return Lista de departamentos con más de la cantidad especificada de empleados
     */
    @Query("SELECT d FROM Departamento d WHERE SIZE(d.empleados) > :cantidad")
    List<Departamento> findByEmpleadosCountGreaterThan(@Param("cantidad") int cantidad);
}