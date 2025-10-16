package com.empresa.crudmixto.repository;

import com.empresa.crudmixto.entity.Usuario;
import com.empresa.crudmixto.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Buscar usuario por username (para autenticación)
     */
    Optional<Usuario> findByUsername(String username);
    
    /**
     * Verificar si existe un usuario con el username dado
     */
    boolean existsByUsername(String username);
    
    /**
     * Buscar usuarios activos
     */
    List<Usuario> findByActivoTrue();
    
    /**
     * Buscar usuarios por rol
     */
    List<Usuario> findByRol(Rol rol);
    
    /**
     * Buscar usuarios activos por rol
     */
    List<Usuario> findByRolAndActivoTrue(Rol rol);
    
    /**
     * Buscar usuarios por nombre (búsqueda parcial)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Usuario> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
}
