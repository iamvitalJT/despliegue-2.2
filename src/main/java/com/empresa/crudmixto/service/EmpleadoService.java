package com.empresa.crudmixto.service;

import com.empresa.crudmixto.entity.Empleado;
import com.empresa.crudmixto.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpleadoService {
      @Autowired
    private EmpleadoRepository empleadoRepository;
    
    // Obtener todos los empleados ordenados por ID
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAllOrderById();
    }
    
    // Obtener empleado por ID
    public Optional<Empleado> obtenerPorId(Long id) {
        return empleadoRepository.findById(id);
    }
    
    // Guardar empleado con validación de email único
    public Empleado guardar(Empleado empleado) {
        // Validar email único para nuevos empleados
        if (empleado.getId() == null && empleadoRepository.existsByEmail(empleado.getEmail())) {
            throw new RuntimeException("Ya existe un empleado con el email: " + empleado.getEmail());
        }
        
        // Validar email único para actualizaciones
        if (empleado.getId() != null && empleadoRepository.existsByEmailAndIdNot(empleado.getEmail(), empleado.getId())) {
            throw new RuntimeException("Ya existe otro empleado con el email: " + empleado.getEmail());
        }
        
        return empleadoRepository.save(empleado);
    }
    
    // Actualizar empleado
    public Empleado actualizar(Long id, Empleado empleadoActualizado) {
        return empleadoRepository.findById(id)
                .map(empleado -> {
                    // Validar email único excluyendo el empleado actual
                    if (!empleado.getEmail().equals(empleadoActualizado.getEmail()) && 
                        empleadoRepository.existsByEmailAndIdNot(empleadoActualizado.getEmail(), id)) {
                        throw new RuntimeException("Ya existe otro empleado con el email: " + empleadoActualizado.getEmail());
                    }
                    
                    empleado.setNombre(empleadoActualizado.getNombre());
                    empleado.setCargo(empleadoActualizado.getCargo());
                    empleado.setSalario(empleadoActualizado.getSalario());
                    empleado.setEmail(empleadoActualizado.getEmail());
                    return empleadoRepository.save(empleado);
                })
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
    }
    
    // Eliminar empleado
    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado con ID: " + id);
        }
        empleadoRepository.deleteById(id);
    }
    
    // Buscar empleados por nombre
    public List<Empleado> buscarPorNombre(String nombre) {
        return empleadoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Buscar empleados por cargo
    public List<Empleado> buscarPorCargo(String cargo) {
        return empleadoRepository.findByCargoContainingIgnoreCase(cargo);
    }
    
    // Búsqueda combinada por nombre o cargo
    public List<Empleado> buscarPorNombreOCargo(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return obtenerTodos();
        }
        return empleadoRepository.buscarPorNombreOCargo(termino.trim());
    }
    
    // Verificar si existe un empleado por email
    public boolean existePorEmail(String email) {
        return empleadoRepository.existsByEmail(email);
    }
    
    // Obtener empleado por email
    public Optional<Empleado> obtenerPorEmail(String email) {
        return empleadoRepository.findByEmail(email);
    }
    
    // Verificar si existe un empleado por ID
    public boolean existe(Long id) {
        return empleadoRepository.existsById(id);
    }
}
