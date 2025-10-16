package com.empresa.crudmixto.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "proyectos")
public class Proyecto {
    
    @Id
    private String id;
    
    @NotBlank(message = "El nombre del proyecto es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    
    @NotNull(message = "El empleado ID es obligatorio")
    private Long empleadoId;
    
    private List<Tarea> tareas = new ArrayList<>();
    
    // Clase interna para las tareas
    public static class Tarea {
        @NotBlank(message = "El título de la tarea es obligatorio")
        private String titulo;
        
        @NotBlank(message = "El estado de la tarea es obligatorio")
        private String estado; // "pendiente", "en_progreso", "completo"
        
        public Tarea() {}
        
        public Tarea(String titulo, String estado) {
            this.titulo = titulo;
            this.estado = estado;
        }
        
        // Getters y Setters
        public String getTitulo() {
            return titulo;
        }
        
        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }
        
        public String getEstado() {
            return estado;
        }
        
        public void setEstado(String estado) {
            this.estado = estado;
        }
        
        @Override
        public String toString() {
            return "Tarea{" +
                    "titulo='" + titulo + '\'' +
                    ", estado='" + estado + '\'' +
                    '}';
        }
    }
    
    // Constructores
    public Proyecto() {}
    
    public Proyecto(String nombre, String descripcion, Long empleadoId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.empleadoId = empleadoId;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Long getEmpleadoId() {
        return empleadoId;
    }
    
    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }
    
    public List<Tarea> getTareas() {
        return tareas;
    }
    
    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }
    
    // Métodos utilitarios para tareas
    public void agregarTarea(String titulo, String estado) {
        this.tareas.add(new Tarea(titulo, estado));
    }
    
    public void agregarTarea(Tarea tarea) {
        this.tareas.add(tarea);
    }
    
    @Override
    public String toString() {
        return "Proyecto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", empleadoId=" + empleadoId +
                ", tareas=" + tareas +
                '}';
    }
}
