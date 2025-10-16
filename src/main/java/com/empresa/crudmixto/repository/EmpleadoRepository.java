package com.empresa.crudmixto.repository;

import com.empresa.crudmixto.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
    // Obtener todos los empleados ordenados por ID ascendente
    @Query("SELECT e FROM Empleado e ORDER BY e.id ASC")
    List<Empleado> findAllOrderById();
    
    // Buscar empleado por email (para validar unicidad)
    Optional<Empleado> findByEmail(String email);
    
    // Verificar si existe un email (para validación)
    boolean existsByEmail(String email);
    
    // Verificar si existe un email excluyendo un ID específico (para actualizaciones)
    boolean existsByEmailAndIdNot(String email, Long id);
    
    // Búsqueda por nombre (insensible a mayúsculas/minúsculas)
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);
    
    // Búsqueda por cargo (insensible a mayúsculas/minúsculas)
    List<Empleado> findByCargoContainingIgnoreCase(String cargo);
      // Búsqueda combinada por nombre O cargo (ordenado por ID)
    @Query("SELECT e FROM Empleado e WHERE " +
           "LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(e.cargo) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "ORDER BY e.id ASC")
    List<Empleado> buscarPorNombreOCargo(@Param("termino") String termino);
    
    // Buscar empleados por rango de salario
    @Query("SELECT e FROM Empleado e WHERE e.salario BETWEEN :salarioMin AND :salarioMax")
    List<Empleado> findBySalarioBetween(@Param("salarioMin") java.math.BigDecimal salarioMin, 
                                       @Param("salarioMax") java.math.BigDecimal salarioMax);
}
