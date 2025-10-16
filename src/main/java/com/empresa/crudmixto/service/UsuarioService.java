package com.empresa.crudmixto.service;

import com.empresa.crudmixto.entity.Usuario;
import com.empresa.crudmixto.entity.Rol;
import com.empresa.crudmixto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Obtener todos los usuarios
     */
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    
    /**
     * Obtener todos los usuarios activos
     */
    public List<Usuario> obtenerActivos() {
        return usuarioRepository.findByActivoTrue();
    }
    
    /**
     * Obtener usuario por ID
     */
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }
      /**
     * Obtener usuario por username
     */
    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
    
    /**
     * Contar total de usuarios en el sistema
     */
    public long contarUsuarios() {
        return usuarioRepository.count();
    }
    
    /**
     * Verificar si existe un usuario con el username dado
     */
    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
    
    /**
     * Crear nuevo usuario con contraseña encriptada
     */
    public Usuario crear(Usuario usuario) {
        // Verificar que el username no exista
        if (existeUsername(usuario.getUsername())) {
            throw new RuntimeException("Ya existe un usuario con el username: " + usuario.getUsername());
        }
        
        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Actualizar usuario existente
     */
    public Usuario actualizar(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
        if (usuarioExistente.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuario.getId());
        }
          Usuario usuarioActual = usuarioExistente.get();
        
        // Verificar si el username cambió y no existe en otro usuario
        if (!usuarioActual.getUsername().equals(usuario.getUsername()) && existeUsername(usuario.getUsername())) {
            throw new RuntimeException("Ya existe un usuario con el username: " + usuario.getUsername());
        }
        
        // Si la contraseña cambió, encriptarla
        if (!usuario.getPassword().equals(usuarioActual.getPassword()) && 
            !passwordEncoder.matches(usuario.getPassword(), usuarioActual.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Cambiar contraseña de usuario
     */
    public void cambiarPassword(Long usuarioId, String nuevaPassword) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
        
        Usuario usuario = usuarioOpt.get();
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }
    
    /**
     * Activar/Desactivar usuario
     */
    public void cambiarEstado(Long usuarioId, boolean activo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
        
        Usuario usuario = usuarioOpt.get();
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }
    
    /**
     * Eliminar usuario (soft delete - desactivar)
     */
    public void eliminar(Long usuarioId) {
        cambiarEstado(usuarioId, false);
    }
    
    /**
     * Buscar usuarios por nombre
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Obtener usuarios por rol
     */
    public List<Usuario> obtenerPorRol(Rol rol) {
        return usuarioRepository.findByRolAndActivoTrue(rol);
    }
      /**
     * Crear usuario administrador por defecto
     */
    public Usuario crearAdminPorDefecto() {
        String usernameAdmin = "admin";
        
        if (!existeUsername(usernameAdmin)) {
            Usuario admin = new Usuario();
            admin.setUsername(usernameAdmin);
            admin.setPassword("admin123"); // Se encriptará automáticamente
            admin.setNombre("Administrador del Sistema");
            admin.setRol(Rol.ADMIN);
            admin.setActivo(true);
            
            return crear(admin);
        }
        
        return obtenerPorUsername(usernameAdmin).orElse(null);
    }
}
