package com.empresa.crudmixto.controller;

import com.empresa.crudmixto.entity.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    /**
     * Mostrar página de login
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Email o contraseña incorrectos. Por favor, inténtelo de nuevo.");
        }
        
        if (logout != null) {
            model.addAttribute("logout", "Ha cerrado sesión exitosamente.");
        }
        
        return "auth/login";
    }
    
    /**
     * Cerrar sesión (GET)
     */
    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login?logout";
    }
    
    /**
     * Obtener el usuario actualmente autenticado
     */
    public static Usuario getUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getPrincipal().equals("anonymousUser")) {
            return null; // Por ahora retornamos null, luego implementaremos esto completamente
        }
        return null;
    }
}
