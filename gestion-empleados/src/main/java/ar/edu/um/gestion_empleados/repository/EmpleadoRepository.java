package ar.edu.um.gestion_empleados.repository;

import ar.edu.um.gestion_empleados.model.Departamento;
import ar.edu.um.gestion_empleados.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
    /**
     * Busca un empleado por su email
     * @param email Email del empleado
     * @return Empleado encontrado (opcional)
     */
    Optional<Empleado> findByEmail(String email);
    
    /**
     * Busca empleados por departamento
     * @param departamento Departamento al que pertenecen
     * @return Lista de empleados del departamento
     */
    List<Empleado> findByDepartamento(Departamento departamento);
    
    /**
     * Busca empleados por rango de salario
     * @param salarioMin Salario mínimo
     * @param salarioMax Salario máximo
     * @return Lista de empleados con salario en el rango especificado
     */
    List<Empleado> findBySalarioBetween(Double salarioMin, Double salarioMax);
    
    /**
     * Busca empleados nacidos después de una fecha
     * @param fecha Fecha de referencia
     * @return Lista de empleados nacidos después de la fecha
     */
    List<Empleado> findByFechaNacimientoAfter(LocalDate fecha);
    
    /**
     * Busca empleados por nombre de departamento
     * @param nombreDepartamento Nombre del departamento
     * @return Lista de empleados del departamento especificado
     */
    @Query("SELECT e FROM Empleado e WHERE e.departamento.nombre = :nombreDepartamento")
    List<Empleado> findByNombreDepartamento(@Param("nombreDepartamento") String nombreDepartamento);
    
    /**
     * Calcula el salario promedio de los empleados de un departamento
     * @param departamentoId ID del departamento
     * @return Salario promedio (opcional)
     */
    @Query("SELECT AVG(e.salario) FROM Empleado e WHERE e.departamento.id = :departamentoId")
    Optional<Double> findAverageSalarioByDepartamento(@Param("departamentoId") Long departamentoId);
}