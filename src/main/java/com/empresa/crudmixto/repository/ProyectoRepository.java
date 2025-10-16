package com.empresa.crudmixto.repository;

import com.empresa.crudmixto.entity.Proyecto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProyectoRepository extends MongoRepository<Proyecto, String> {
    
    // Buscar todos los proyectos asignados a un empleado específico
    List<Proyecto> findByEmpleadoId(Long empleadoId);
    
    // Buscar proyectos por nombre (insensible a mayúsculas/minúsculas)
    List<Proyecto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar proyectos por descripción
    List<Proyecto> findByDescripcionContainingIgnoreCase(String descripcion);
    
    // Buscar proyectos que contengan tareas con un estado específico
    @Query("{ 'tareas.estado' : ?0 }")
    List<Proyecto> findByTareasEstado(String estado);
    
    // Contar proyectos por empleado
    long countByEmpleadoId(Long empleadoId);
    
    // Buscar proyectos por empleado y estado de tareas
    @Query("{ 'empleadoId' : ?0, 'tareas.estado' : ?1 }")
    List<Proyecto> findByEmpleadoIdAndTareasEstado(Long empleadoId, String estado);
    
    // Verificar si un empleado tiene proyectos asignados
    boolean existsByEmpleadoId(Long empleadoId);
}
