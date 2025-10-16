package com.empresa.crudmixto.controller;

import com.empresa.crudmixto.entity.Proyecto;
import com.empresa.crudmixto.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "*")
public class ProyectoController {
    
    @Autowired
    private ProyectoService proyectoService;
    
    // Obtener todos los proyectos
    @GetMapping
    public ResponseEntity<List<Proyecto>> obtenerTodos() {
        try {
            List<Proyecto> proyectos = proyectoService.obtenerTodos();
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Obtener proyecto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtenerPorId(@PathVariable String id) {
        try {
            Optional<Proyecto> proyecto = proyectoService.obtenerPorId(id);
            return proyecto.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Crear nuevo proyecto
    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody Proyecto proyecto) {
        try {
            Proyecto proyectoGuardado = proyectoService.guardar(proyecto);
            return ResponseEntity.status(HttpStatus.CREATED).body(proyectoGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
    
    // Actualizar proyecto
    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizar(@PathVariable String id, 
                                           @Valid @RequestBody Proyecto proyecto) {
        try {
            Proyecto proyectoActualizado = proyectoService.actualizar(id, proyecto);
            return ResponseEntity.ok(proyectoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
    
    // Eliminar proyecto
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable String id) {
        try {
            proyectoService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Proyecto eliminado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
    
    // Obtener proyectos por empleado ID
    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<Object> obtenerPorEmpleado(@PathVariable Long empleadoId) {
        try {
            List<Proyecto> proyectos = proyectoService.obtenerPorEmpleadoId(empleadoId);
            return ResponseEntity.ok(proyectos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
    
    // Buscar proyectos por nombre
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Proyecto>> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<Proyecto> proyectos = proyectoService.buscarPorNombre(nombre);
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Buscar proyectos por descripción
    @GetMapping("/buscar/descripcion")
    public ResponseEntity<List<Proyecto>> buscarPorDescripcion(@RequestParam String descripcion) {
        try {
            List<Proyecto> proyectos = proyectoService.buscarPorDescripcion(descripcion);
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Agregar tarea a un proyecto
    @PostMapping("/{id}/tareas")
    public ResponseEntity<Object> agregarTarea(@PathVariable String id, 
                                             @RequestBody Map<String, String> tarea) {
        try {
            String titulo = tarea.get("titulo");
            String estado = tarea.get("estado");
            
            if (titulo == null || estado == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Título y estado son obligatorios"));
            }
            
            Proyecto proyectoActualizado = proyectoService.agregarTarea(id, titulo, estado);
            return ResponseEntity.ok(proyectoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
    
    // Actualizar estado de una tarea
    @PutMapping("/{id}/tareas/{indiceTarea}")
    public ResponseEntity<Object> actualizarEstadoTarea(@PathVariable String id, 
                                                       @PathVariable int indiceTarea,
                                                       @RequestBody Map<String, String> datos) {
        try {
            String nuevoEstado = datos.get("estado");
            
            if (nuevoEstado == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El estado es obligatorio"));
            }
            
            Proyecto proyectoActualizado = proyectoService.actualizarEstadoTarea(id, indiceTarea, nuevoEstado);
            return ResponseEntity.ok(proyectoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
    
    // Obtener estadísticas de proyectos por empleado
    @GetMapping("/estadisticas/empleado/{empleadoId}")
    public ResponseEntity<Object> obtenerEstadisticasPorEmpleado(@PathVariable Long empleadoId) {
        try {
            long cantidadProyectos = proyectoService.contarPorEmpleado(empleadoId);
            List<Proyecto> proyectos = proyectoService.obtenerPorEmpleadoId(empleadoId);
            
            long tareasCompletas = proyectos.stream()
                    .flatMap(p -> p.getTareas().stream())
                    .mapToLong(t -> "completo".equals(t.getEstado()) ? 1 : 0)
                    .sum();
            
            long tareasPendientes = proyectos.stream()
                    .flatMap(p -> p.getTareas().stream())
                    .mapToLong(t -> "pendiente".equals(t.getEstado()) ? 1 : 0)
                    .sum();
            
            Map<String, Object> estadisticas = Map.of(
                "cantidadProyectos", cantidadProyectos,
                "tareasCompletas", tareasCompletas,
                "tareasPendientes", tareasPendientes
            );
            
            return ResponseEntity.ok(estadisticas);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
    
    // Obtener proyectos por estado de tareas
    @GetMapping("/buscar/estado-tareas")
    public ResponseEntity<List<Proyecto>> obtenerPorEstadoTareas(@RequestParam String estado) {
        try {
            List<Proyecto> proyectos = proyectoService.obtenerPorEstadoTareas(estado);
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
