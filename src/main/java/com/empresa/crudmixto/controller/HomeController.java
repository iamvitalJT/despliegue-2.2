package com.empresa.crudmixto.controller;

import com.empresa.crudmixto.service.EmpleadoService;
import com.empresa.crudmixto.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private EmpleadoService empleadoService;
    
    @Autowired
    private ProyectoService proyectoService;
    
    @GetMapping("/")
    public String home(Model model) {
        try {
            long totalEmpleados = empleadoService.obtenerTodos().size();
            long totalProyectos = proyectoService.obtenerTodos().size();
            
            model.addAttribute("totalEmpleados", totalEmpleados);
            model.addAttribute("totalProyectos", totalProyectos);
            
            return "home";
        } catch (Exception e) {
            // En caso de error, redirigir a empleados
            return "redirect:/empleados";
        }
    }
    
    @GetMapping("/home")
    public String homeAlternative(Model model) {
        return home(model);
    }
}
