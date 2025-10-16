package com.empresa.crudmixto.controller;

import com.empresa.crudmixto.entity.Empleado;
import com.empresa.crudmixto.entity.Proyecto;
import com.empresa.crudmixto.service.EmpleadoService;
import com.empresa.crudmixto.service.ExportService;
import com.empresa.crudmixto.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private ExportService exportService;
    
    @Autowired
    private EmpleadoService empleadoService;
    
    @Autowired
    private ProyectoService proyectoService;

    /**
     * Exportar empleados a Excel
     */
    @GetMapping("/empleados/excel")
    public ResponseEntity<byte[]> exportEmpleadosExcel(
            @RequestParam(required = false) String termino) {
        try {
            List<Empleado> empleados;
            
            // Aplicar filtro si existe
            if (termino != null && !termino.trim().isEmpty()) {
                empleados = empleadoService.buscarPorNombre(termino);
            } else {
                empleados = empleadoService.obtenerTodos();
            }

            byte[] excelData = exportService.exportEmpleadosToExcel(empleados);

            String fileName = "empleados_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(excelData.length);

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Exportar empleados a PDF
     */
    @GetMapping("/empleados/pdf")
    public ResponseEntity<byte[]> exportEmpleadosPdf(
            @RequestParam(required = false) String termino) {
        try {
            List<Empleado> empleados;
            
            // Aplicar filtro si existe
            if (termino != null && !termino.trim().isEmpty()) {
                empleados = empleadoService.buscarPorNombre(termino);
            } else {
                empleados = empleadoService.obtenerTodos();
            }

            byte[] pdfData = exportService.exportEmpleadosToPdf(empleados);

            String fileName = "empleados_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(pdfData.length);

            return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Exportar proyectos a Excel
     */
    @GetMapping("/proyectos/excel")
    public ResponseEntity<byte[]> exportProyectosExcel(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) Long empleadoId) {
        try {
            List<Proyecto> proyectos;
            List<Empleado> empleados = empleadoService.obtenerTodos();
            
            // Aplicar filtros si existen
            if (buscar != null && !buscar.trim().isEmpty()) {
                proyectos = proyectoService.buscarPorNombre(buscar);
            } else if (empleadoId != null) {
                proyectos = proyectoService.obtenerPorEmpleadoId(empleadoId);
            } else {
                proyectos = proyectoService.obtenerTodos();
            }

            byte[] excelData = exportService.exportProyectosToExcel(proyectos, empleados);

            String fileName = "proyectos_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(excelData.length);

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Exportar proyectos a PDF
     */
    @GetMapping("/proyectos/pdf")
    public ResponseEntity<byte[]> exportProyectosPdf(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) Long empleadoId) {
        try {
            List<Proyecto> proyectos;
            List<Empleado> empleados = empleadoService.obtenerTodos();
            
            // Aplicar filtros si existen
            if (buscar != null && !buscar.trim().isEmpty()) {
                proyectos = proyectoService.buscarPorNombre(buscar);
            } else if (empleadoId != null) {
                proyectos = proyectoService.obtenerPorEmpleadoId(empleadoId);
            } else {
                proyectos = proyectoService.obtenerTodos();
            }

            byte[] pdfData = exportService.exportProyectosToPdf(proyectos, empleados);

            String fileName = "proyectos_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(pdfData.length);

            return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
