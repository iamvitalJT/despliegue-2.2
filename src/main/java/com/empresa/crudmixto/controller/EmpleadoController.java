package com.empresa.crudmixto.controller;

import com.empresa.crudmixto.entity.Empleado;
import com.empresa.crudmixto.entity.Proyecto;
import com.empresa.crudmixto.service.EmpleadoService;
import com.empresa.crudmixto.service.ProyectoService;
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
@RequestMapping("/empleados")
public class EmpleadoController {
    
    @Autowired
    private EmpleadoService empleadoService;
    
    @Autowired
    private ProyectoService proyectoService;
    
    // Listar todos los empleados
    @GetMapping
    public String listar(Model model, @RequestParam(value = "buscar", required = false) String termino) {
        List<Empleado> empleados;
        
        if (termino != null && !termino.trim().isEmpty()) {
            empleados = empleadoService.buscarPorNombreOCargo(termino);
            model.addAttribute("termino", termino);
        } else {
            empleados = empleadoService.obtenerTodos();
        }
        
        model.addAttribute("empleados", empleados);
        return "empleados/lista";
    }
    
    // Mostrar formulario para crear empleado
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("empleado", new Empleado());
        model.addAttribute("titulo", "Crear Empleado");
        return "empleados/formulario";
    }
    
    // Mostrar formulario para editar empleado
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Empleado> empleado = empleadoService.obtenerPorId(id);
        if (empleado.isPresent()) {
            model.addAttribute("empleado", empleado.get());
            model.addAttribute("titulo", "Editar Empleado");
            return "empleados/formulario";
        } else {
            return "redirect:/empleados?error=Empleado no encontrado";
        }
    }
    
    // Procesar formulario (crear o actualizar)
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Empleado empleado, 
                         BindingResult result, 
                         Model model, 
                         RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("titulo", empleado.getId() == null ? "Crear Empleado" : "Editar Empleado");
            return "empleados/formulario";
        }
        
        try {
            if (empleado.getId() == null) {
                empleadoService.guardar(empleado);
                redirectAttributes.addFlashAttribute("exito", "Empleado creado exitosamente");
            } else {
                empleadoService.actualizar(empleado.getId(), empleado);
                redirectAttributes.addFlashAttribute("exito", "Empleado actualizado exitosamente");
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", empleado.getId() == null ? "Crear Empleado" : "Editar Empleado");
            return "empleados/formulario";
        }
        
        return "redirect:/empleados";
    }
    
    // Eliminar empleado
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si el empleado tiene proyectos asignados
            if (proyectoService.empleadoTieneProyectos(id)) {
                redirectAttributes.addFlashAttribute("error", 
                    "No se puede eliminar el empleado porque tiene proyectos asignados");
            } else {
                empleadoService.eliminar(id);
                redirectAttributes.addFlashAttribute("exito", "Empleado eliminado exitosamente");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/empleados";
    }
    
    // Ver detalles del empleado y sus proyectos
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Optional<Empleado> empleado = empleadoService.obtenerPorId(id);
        if (empleado.isPresent()) {
            List<Proyecto> proyectos = proyectoService.obtenerPorEmpleadoId(id);
            long cantidadProyectos = proyectoService.contarPorEmpleado(id);
            
            model.addAttribute("empleado", empleado.get());
            model.addAttribute("proyectos", proyectos);
            model.addAttribute("cantidadProyectos", cantidadProyectos);
            return "empleados/detalle";
        } else {
            return "redirect:/empleados?error=Empleado no encontrado";
        }
    }
    
    // Búsqueda AJAX (opcional para mejorar UX)
    @GetMapping("/buscar")
    public String buscar(@RequestParam String termino, Model model) {
        List<Empleado> empleados = empleadoService.buscarPorNombreOCargo(termino);
        model.addAttribute("empleados", empleados);
        model.addAttribute("termino", termino);
        return "empleados/lista :: tabla-empleados";
    }
}
