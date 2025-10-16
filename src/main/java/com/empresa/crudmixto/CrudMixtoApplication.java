package com.empresa.crudmixto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.empresa.crudmixto.repository")
@EnableMongoRepositories(basePackages = "com.empresa.crudmixto.repository")
public class CrudMixtoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudMixtoApplication.class, args);        System.out.println("\n" +
            "===========================    H====================\n" +
            "🏢 GESTIÓN EMPRESARIAL - SISTEMA INICIADO 🏢\n" +
            "===============================================\n" +
            "📱 Portal Corporativo: http://localhost:8080/\n" +
            "👥 Recursos Humanos: http://localhost:8080/empleados\n" +
            "📋 Gestión de Proyectos: http://localhost:8080/proyectos\n" +
            "💼 Sistema de gestión integrado empresarial\n" +
            "🔒 Acceso seguro y centralizado a la información\n" +
            "===============================================\n"
        );
    }
}
