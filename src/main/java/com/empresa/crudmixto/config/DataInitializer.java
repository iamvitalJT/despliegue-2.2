package com.empresa.crudmixto.config;

import com.empresa.crudmixto.entity.Rol;
import com.empresa.crudmixto.entity.Usuario;
import com.empresa.crudmixto.entity.Empleado;
import com.empresa.crudmixto.entity.Proyecto;
import com.empresa.crudmixto.service.UsuarioService;
import com.empresa.crudmixto.service.EmpleadoService;
import com.empresa.crudmixto.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Inicializador de datos por defecto para el sistema
 * Crea usuarios, empleados, proyectos y relaciones de ejemplo
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EmpleadoService empleadoService;
    
    @Autowired
    private ProyectoService proyectoService;
    
    @Override
    public void run(String... args) throws Exception {        // Crear usuario administrador por defecto si no existe ningún usuario
        if (usuarioService.contarUsuarios() == 0) {
            logger.info("No se encontraron usuarios en el sistema. Creando usuario administrador por defecto...");
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setNombre("Administrador del Sistema");
            admin.setRol(Rol.ADMIN);
            admin.setActivo(true);
            
            usuarioService.crear(admin);
            logger.info("Usuario administrador creado exitosamente:");
            logger.info("Usuario: admin");
            logger.info("Contraseña: admin123");
            
            // Crear usuario supervisor de ejemplo
            Usuario supervisor = new Usuario();
            supervisor.setUsername("supervisor");
            supervisor.setPassword("super123");
            supervisor.setNombre("Supervisor de Proyectos");
            supervisor.setRol(Rol.SUPERVISOR);
            supervisor.setActivo(true);
            
            usuarioService.crear(supervisor);
            logger.info("Usuario supervisor creado: supervisor / super123");
        } else {
            logger.info("Se encontraron {} usuarios en el sistema.", usuarioService.contarUsuarios());
        }
        
        // Crear empleados de ejemplo si no existen
        if (empleadoService.obtenerTodos().isEmpty()) {
            logger.info("Creando empleados de ejemplo...");
            
            Empleado[] empleadosEjemplo = {
                new Empleado("Ana García López", "Líder de Proyecto", new BigDecimal("85000"), "ana.garcia@empresa.com"),
                new Empleado("Carlos Rodríguez", "Desarrollador Senior", new BigDecimal("70000"), "carlos.rodriguez@empresa.com"),
                new Empleado("María José Silva", "Analista de Sistemas", new BigDecimal("60000"), "maria.silva@empresa.com"),
                new Empleado("Pedro Martínez", "Desarrollador Junior", new BigDecimal("45000"), "pedro.martinez@empresa.com"),
                new Empleado("Laura Fernández", "Diseñadora UX/UI", new BigDecimal("55000"), "laura.fernandez@empresa.com"),
                new Empleado("Diego Morales", "Tester QA", new BigDecimal("50000"), "diego.morales@empresa.com"),
                new Empleado("Isabella Torres", "Analista de Negocio", new BigDecimal("65000"), "isabella.torres@empresa.com"),
                new Empleado("Andrés Vargas", "Desarrollador Full Stack", new BigDecimal("72000"), "andres.vargas@empresa.com")
            };
            
            for (Empleado empleado : empleadosEjemplo) {
                empleadoService.guardar(empleado);
            }
            
            logger.info("Creados {} empleados de ejemplo", empleadosEjemplo.length);
        }
        
        // Crear proyectos de ejemplo si no existen
        if (proyectoService.obtenerTodos().isEmpty()) {
            logger.info("Creando proyectos de ejemplo...");
            
            // Obtener primer empleado para compatibilidad temporal
            Long primerEmpleadoId = empleadoService.obtenerTodos().get(0).getId();
            
            Proyecto[] proyectosEjemplo = {
                new Proyecto("Sistema de Gestión Empresarial", 
                           "Desarrollo de plataforma integral para gestión de recursos humanos y proyectos", 
                           primerEmpleadoId),
                new Proyecto("App Móvil Corporativa", 
                           "Aplicación móvil para empleados con funcionalidades de comunicación y gestión", 
                           primerEmpleadoId),
                new Proyecto("Dashboard Analítico", 
                           "Panel de control con métricas y reportes avanzados para la toma de decisiones", 
                           primerEmpleadoId),
                new Proyecto("Sistema de Facturación", 
                           "Plataforma de facturación electrónica integrada con contabilidad", 
                           primerEmpleadoId),
                new Proyecto("Portal Web Corporativo", 
                           "Sitio web institucional con área de clientes y servicios en línea", 
                           primerEmpleadoId)
            };
            
            for (Proyecto proyecto : proyectosEjemplo) {
                // Agregar tareas de ejemplo
                proyecto.agregarTarea("Análisis de requerimientos", "completo");
                proyecto.agregarTarea("Diseño de arquitectura", "completo");
                proyecto.agregarTarea("Desarrollo del módulo principal", "en_progreso");
                proyecto.agregarTarea("Implementación de interfaz", "en_progreso");
                proyecto.agregarTarea("Pruebas unitarias", "pendiente");
                proyecto.agregarTarea("Pruebas de integración", "pendiente");
                proyecto.agregarTarea("Documentación técnica", "pendiente");
                proyecto.agregarTarea("Despliegue en producción", "pendiente");
                
                proyectoService.guardar(proyecto);
            }
            
            logger.info("Creados {} proyectos de ejemplo", proyectosEjemplo.length);
        }
    }
}
