package com.empresa.crudmixto.service;

import com.empresa.crudmixto.entity.Empleado;
import com.empresa.crudmixto.entity.Proyecto;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
// Importaciones de Apache POI - usando nombres completos para evitar conflictos
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    /**
     * Exporta la lista de empleados a formato Excel
     */
    public byte[] exportEmpleadosToExcel(List<Empleado> empleados) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Empleados");

            // Estilo para el encabezado
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nombre", "Cargo", "Salario", "Email"};
            
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Estilo para datos
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT);

            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("$#,##0.00"));            // Llenar datos
            int rowNum = 1;
            for (Empleado empleado : empleados) {
                Row row = sheet.createRow(rowNum++);
                
                org.apache.poi.ss.usermodel.Cell idCell = row.createCell(0);
                idCell.setCellValue(empleado.getId());
                idCell.setCellStyle(dataStyle);
                
                org.apache.poi.ss.usermodel.Cell nombreCell = row.createCell(1);
                nombreCell.setCellValue(empleado.getNombre());
                nombreCell.setCellStyle(dataStyle);
                
                org.apache.poi.ss.usermodel.Cell cargoCell = row.createCell(2);
                cargoCell.setCellValue(empleado.getCargo());
                cargoCell.setCellStyle(dataStyle);
                
                org.apache.poi.ss.usermodel.Cell salarioCell = row.createCell(3);
                salarioCell.setCellValue(empleado.getSalario().doubleValue());
                salarioCell.setCellStyle(currencyStyle);
                
                org.apache.poi.ss.usermodel.Cell emailCell = row.createCell(4);
                emailCell.setCellValue(empleado.getEmail());
                emailCell.setCellStyle(dataStyle);
            }            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Agregar información de exportación
            Row infoRow = sheet.createRow(rowNum + 1);
            org.apache.poi.ss.usermodel.Cell infoCell = infoRow.createCell(0);
            infoCell.setCellValue("Exportado el: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Exporta la lista de empleados a formato PDF
     */
    public byte[] exportEmpleadosToPdf(List<Empleado> empleados) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título
            Paragraph title = new Paragraph("Lista de Empleados")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Fecha de exportación
            Paragraph fecha = new Paragraph("Exportado el: " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(fecha);

            // Crear tabla
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 2, 2, 3}))
                    .setWidth(UnitValue.createPercentValue(100));            // Encabezados
            String[] headers = {"ID", "Nombre", "Cargo", "Salario", "Email"};
            for (String header : headers) {
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph(header))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER);
                table.addHeaderCell(headerCell);
            }            // Datos
            for (Empleado empleado : empleados) {
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(empleado.getId()))));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(empleado.getNombre())));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(empleado.getCargo())));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.format("$%.2f", empleado.getSalario()))));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(empleado.getEmail())));
            }

            document.add(table);

            // Resumen
            Paragraph resumen = new Paragraph("\nTotal de empleados: " + empleados.size())
                    .setFontSize(12)
                    .setBold();
            document.add(resumen);

            document.close();
            return out.toByteArray();
        }
    }

    /**
     * Exporta la lista de proyectos a formato Excel
     */
    public byte[] exportProyectosToExcel(List<Proyecto> proyectos, List<Empleado> empleados) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Proyectos");

            // Estilo para el encabezado
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nombre", "Descripción", "Empleado Asignado", "Total Tareas", "Tareas Completas"};
              for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Estilo para datos
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setWrapText(true);            // Llenar datos
            int rowNum = 1;
            for (Proyecto proyecto : proyectos) {
                Row row = sheet.createRow(rowNum++);
                
                org.apache.poi.ss.usermodel.Cell idCell = row.createCell(0);
                idCell.setCellValue(proyecto.getId());
                idCell.setCellStyle(dataStyle);
                
                org.apache.poi.ss.usermodel.Cell nombreCell = row.createCell(1);
                nombreCell.setCellValue(proyecto.getNombre());
                nombreCell.setCellStyle(dataStyle);
                
                org.apache.poi.ss.usermodel.Cell descripcionCell = row.createCell(2);
                descripcionCell.setCellValue(proyecto.getDescripcion());
                descripcionCell.setCellStyle(dataStyle);
                
                // Buscar nombre del empleado
                String nombreEmpleado = empleados.stream()
                        .filter(emp -> emp.getId().equals(proyecto.getEmpleadoId()))
                        .map(Empleado::getNombre)
                        .findFirst()
                        .orElse("No asignado");
                
                org.apache.poi.ss.usermodel.Cell empleadoCell = row.createCell(3);
                empleadoCell.setCellValue(nombreEmpleado);
                empleadoCell.setCellStyle(dataStyle);                
                org.apache.poi.ss.usermodel.Cell totalTareasCell = row.createCell(4);
                totalTareasCell.setCellValue(proyecto.getTareas() != null ? proyecto.getTareas().size() : 0);
                totalTareasCell.setCellStyle(dataStyle);
                
                // Contar tareas completadas
                long tareasCompletas = proyecto.getTareas() != null ? 
                        proyecto.getTareas().stream()
                                .mapToLong(tarea -> "completo".equals(tarea.getEstado()) ? 1 : 0)
                                .sum() : 0;
                
                org.apache.poi.ss.usermodel.Cell tareasCompletasCell = row.createCell(5);
                tareasCompletasCell.setCellValue(tareasCompletas);
                tareasCompletasCell.setCellStyle(dataStyle);
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                if (i == 2) { // Descripción
                    sheet.setColumnWidth(i, 15000);
                }
            }

            // Agregar información de exportación
            Row infoRow = sheet.createRow(rowNum + 1);
            org.apache.poi.ss.usermodel.Cell infoCell = infoRow.createCell(0);
            infoCell.setCellValue("Exportado el: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Exporta la lista de proyectos a formato PDF
     */
    public byte[] exportProyectosToPdf(List<Proyecto> proyectos, List<Empleado> empleados) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título
            Paragraph title = new Paragraph("Lista de Proyectos")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Fecha de exportación
            Paragraph fecha = new Paragraph("Exportado el: " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(fecha);

            // Crear tabla
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 3, 2, 1, 1}))
                    .setWidth(UnitValue.createPercentValue(100));            // Encabezados
            String[] headers = {"ID", "Nombre", "Descripción", "Empleado", "Tareas", "Completas"};
            for (String header : headers) {
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph(header))
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER);
                table.addHeaderCell(headerCell);
            }

            // Datos
            for (Proyecto proyecto : proyectos) {
                // Buscar nombre del empleado
                String nombreEmpleado = empleados.stream()
                        .filter(emp -> emp.getId().equals(proyecto.getEmpleadoId()))
                        .map(Empleado::getNombre)
                        .findFirst()
                        .orElse("No asignado");

                // Contar tareas completadas
                long tareasCompletas = proyecto.getTareas() != null ? 
                        proyecto.getTareas().stream()
                                .mapToLong(tarea -> "completo".equals(tarea.getEstado()) ? 1 : 0)
                                .sum() : 0;                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(proyecto.getId())));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(proyecto.getNombre())));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(proyecto.getDescripcion())));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(nombreEmpleado)));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(proyecto.getTareas() != null ? proyecto.getTareas().size() : 0))));
                table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(tareasCompletas))));
            }

            document.add(table);

            // Resumen
            Paragraph resumen = new Paragraph("\nTotal de proyectos: " + proyectos.size())
                    .setFontSize(12)
                    .setBold();
            document.add(resumen);

            document.close();
            return out.toByteArray();
        }
    }
}
