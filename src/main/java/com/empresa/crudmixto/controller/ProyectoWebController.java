package com.empresa.crudmixto.controller;

import com.empresa.crudmixto.entity.Proyecto;
import com.empresa.crudmixto.entity.Empleado;
import com.empresa.crudmixto.service.ProyectoService;
import com.empresa.crudmixto.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proyectos")
public class ProyectoWebController {
    
    @Autowired
    private ProyectoService proyectoService;
    
    @Autowired
    private EmpleadoService empleadoService;
    
    // Listar todos los proyectos
    @GetMapping
    public String listar(Model model, 
                        @RequestParam(value = "buscar", required = false) String termino,
                        @RequestParam(value = "empleadoId", required = false) Long empleadoId) {
        
        List<Proyecto> proyectos;
        List<Empleado> empleados = empleadoService.obtenerTodos();
        
        try {
            if (empleadoId != null) {
                // Filtrar por empleado específico
                proyectos = proyectoService.obtenerPorEmpleadoId(empleadoId);
                model.addAttribute("empleadoSeleccionado", empleadoId);
                
                // Obtener nombre del empleado para mostrar
                Optional<Empleado> empleado = empleadoService.obtenerPorId(empleadoId);
                if (empleado.isPresent()) {
                    model.addAttribute("nombreEmpleado", empleado.get().getNombre());
                }
            } else if (termino != null && !termino.trim().isEmpty()) {
                // Búsqueda por nombre de proyecto
                proyectos = proyectoService.buscarPorNombre(termino.trim());
                model.addAttribute("termino", termino);
            } else {
                // Obtener todos los proyectos
                proyectos = proyectoService.obtenerTodos();
            }
            
            model.addAttribute("proyectos", proyectos);
            model.addAttribute("empleados", empleados);
            
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("proyectos", List.of());
            model.addAttribute("empleados", empleados);
        }
        
        return "proyectos/lista";
    }
    
    // Mostrar formulario para crear proyecto
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("empleados", empleadoService.obtenerTodos());
        model.addAttribute("titulo", "Crear Proyecto");
        return "proyectos/formulario";
    }
    
    // Mostrar formulario para editar proyecto
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable String id, Model model) {
        Optional<Proyecto> proyecto = proyectoService.obtenerPorId(id);
        if (proyecto.isPresent()) {
            model.addAttribute("proyecto", proyecto.get());
            model.addAttribute("empleados", empleadoService.obtenerTodos());
            model.addAttribute("titulo", "Editar Proyecto");
            return "proyectos/formulario";
        } else {
            return "redirect:/proyectos?error=Proyecto no encontrado";
        }
    }
    
    // Mostrar detalle del proyecto
    @GetMapping("/detalle/{id}")
    public String mostrarDetalle(@PathVariable String id, Model model) {
        Optional<Proyecto> proyectoOpt = proyectoService.obtenerPorId(id);
        
        if (proyectoOpt.isPresent()) {
            Proyecto proyecto = proyectoOpt.get();
            model.addAttribute("proyecto", proyecto);
            
            // Obtener información del empleado asignado
            Optional<Empleado> empleado = empleadoService.obtenerPorId(proyecto.getEmpleadoId());
            if (empleado.isPresent()) {
                model.addAttribute("empleado", empleado.get());
            }
            
            return "proyectos/detalle";
        } else {
            return "redirect:/proyectos?error=Proyecto no encontrado";
        }
    }
    
    // Procesar formulario (crear o actualizar)
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Proyecto proyecto, 
                         BindingResult result, 
                         Model model, 
                         RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("empleados", empleadoService.obtenerTodos());
            model.addAttribute("titulo", proyecto.getId() == null ? "Crear Proyecto" : "Editar Proyecto");
            return "proyectos/formulario";
        }
        
        try {
            if (proyecto.getId() == null || proyecto.getId().isEmpty()) {
                proyectoService.guardar(proyecto);
                redirectAttributes.addFlashAttribute("exito", "Proyecto creado exitosamente");
            } else {
                proyectoService.actualizar(proyecto.getId(), proyecto);
                redirectAttributes.addFlashAttribute("exito", "Proyecto actualizado exitosamente");
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("empleados", empleadoService.obtenerTodos());
            model.addAttribute("titulo", proyecto.getId() == null ? "Crear Proyecto" : "Editar Proyecto");
            return "proyectos/formulario";
        }
        
        return "redirect:/proyectos";
    }
    
    // Eliminar proyecto
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            proyectoService.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "Proyecto eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/proyectos";
    }
    
    // Agregar tarea a proyecto
    @PostMapping("/{id}/tareas")
    public String agregarTarea(@PathVariable String id, 
                              @RequestParam String titulo, 
                              @RequestParam String estado,
                              RedirectAttributes redirectAttributes) {
        try {
            if (titulo == null || titulo.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El título de la tarea es obligatorio");
            } else if (estado == null || estado.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El estado de la tarea es obligatorio");
            } else {
                proyectoService.agregarTarea(id, titulo.trim(), estado);
                redirectAttributes.addFlashAttribute("exito", "Tarea agregada exitosamente");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/proyectos/detalle/" + id;
    }
    
    // Actualizar estado de tarea
    @PostMapping("/{id}/tareas/{indiceTarea}/estado")
    public String actualizarEstadoTarea(@PathVariable String id, 
                                       @PathVariable int indiceTarea,
                                       @RequestParam String estado,
                                       RedirectAttributes redirectAttributes) {
        try {
            if (estado == null || estado.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El estado es obligatorio");
            } else {
                proyectoService.actualizarEstadoTarea(id, indiceTarea, estado);
                redirectAttributes.addFlashAttribute("exito", "Estado de tarea actualizado exitosamente");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/proyectos/detalle/" + id;
    }
    
    /**
     * Método utilitario para formatear estados de tareas de forma amigable al usuario
     */
    public static String formatearEstado(String estado) {
        if (estado == null) return "";
        
        switch (estado.toLowerCase()) {
            case "en_progreso":
                return "En Progreso";
            case "completo":
                return "Completo";
            case "pendiente":
                return "Pendiente";
            default:
                return estado;
        }
    }
    
    /**
     * ModelAttribute para hacer disponible el método formatearEstado en todas las vistas
     */
    @ModelAttribute("formatearEstado")
    public java.util.function.Function<String, String> getFormatearEstado() {
        return ProyectoWebController::formatearEstado;
    }
}
