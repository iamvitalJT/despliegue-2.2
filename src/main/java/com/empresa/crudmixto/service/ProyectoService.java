package com.empresa.crudmixto.service;

import com.empresa.crudmixto.entity.Proyecto;
import com.empresa.crudmixto.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {
    
    @Autowired
    private ProyectoRepository proyectoRepository;
    
    @Autowired
    private EmpleadoService empleadoService;
    
    // Obtener todos los proyectos
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }
    
    // Obtener proyecto por ID
    public Optional<Proyecto> obtenerPorId(String id) {
        return proyectoRepository.findById(id);
    }
    
    // Guardar proyecto con validación de empleado existente
    public Proyecto guardar(Proyecto proyecto) {
        // Validar que el empleado existe
        if (!empleadoService.existe(proyecto.getEmpleadoId())) {
            throw new RuntimeException("No existe un empleado con ID: " + proyecto.getEmpleadoId());
        }
        return proyectoRepository.save(proyecto);
    }
    
    // Actualizar proyecto
    public Proyecto actualizar(String id, Proyecto proyectoActualizado) {
        return proyectoRepository.findById(id)
                .map(proyecto -> {
                    // Validar que el nuevo empleado existe
                    if (!empleadoService.existe(proyectoActualizado.getEmpleadoId())) {
                        throw new RuntimeException("No existe un empleado con ID: " + proyectoActualizado.getEmpleadoId());
                    }
                    
                    proyecto.setNombre(proyectoActualizado.getNombre());
                    proyecto.setDescripcion(proyectoActualizado.getDescripcion());
                    proyecto.setEmpleadoId(proyectoActualizado.getEmpleadoId());
                    proyecto.setTareas(proyectoActualizado.getTareas());
                    return proyectoRepository.save(proyecto);
                })
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
    }
    
    // Eliminar proyecto
    public void eliminar(String id) {
        if (!proyectoRepository.existsById(id)) {
            throw new RuntimeException("Proyecto no encontrado con ID: " + id);
        }
        proyectoRepository.deleteById(id);
    }
    
    // Obtener proyectos por empleado ID
    public List<Proyecto> obtenerPorEmpleadoId(Long empleadoId) {
        if (!empleadoService.existe(empleadoId)) {
            throw new RuntimeException("No existe un empleado con ID: " + empleadoId);
        }
        return proyectoRepository.findByEmpleadoId(empleadoId);
    }
    
    // Buscar proyectos por nombre
    public List<Proyecto> buscarPorNombre(String nombre) {
        return proyectoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Buscar proyectos por descripción
    public List<Proyecto> buscarPorDescripcion(String descripcion) {
        return proyectoRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }
    
    // Agregar tarea a un proyecto
    public Proyecto agregarTarea(String proyectoId, String titulo, String estado) {
        return proyectoRepository.findById(proyectoId)
                .map(proyecto -> {
                    proyecto.agregarTarea(titulo, estado);
                    return proyectoRepository.save(proyecto);
                })
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + proyectoId));
    }
    
    // Actualizar estado de una tarea específica
    public Proyecto actualizarEstadoTarea(String proyectoId, int indiceTarea, String nuevoEstado) {
        return proyectoRepository.findById(proyectoId)
                .map(proyecto -> {
                    if (indiceTarea >= 0 && indiceTarea < proyecto.getTareas().size()) {
                        proyecto.getTareas().get(indiceTarea).setEstado(nuevoEstado);
                        return proyectoRepository.save(proyecto);
                    } else {
                        throw new RuntimeException("Índice de tarea inválido: " + indiceTarea);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + proyectoId));
    }
    
    // Contar proyectos por empleado
    public long contarPorEmpleado(Long empleadoId) {
        return proyectoRepository.countByEmpleadoId(empleadoId);
    }
    
    // Obtener proyectos por estado de tareas
    public List<Proyecto> obtenerPorEstadoTareas(String estado) {
        return proyectoRepository.findByTareasEstado(estado);
    }
    
    // Verificar si un empleado tiene proyectos asignados
    public boolean empleadoTieneProyectos(Long empleadoId) {
        return proyectoRepository.existsByEmpleadoId(empleadoId);
    }
}
