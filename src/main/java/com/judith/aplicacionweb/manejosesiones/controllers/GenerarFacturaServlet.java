package com.judith.aplicacionweb.manejosesiones.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.judith.aplicacionweb.manejosesiones.models.DetalleCarro;
import com.judith.aplicacionweb.manejosesiones.models.ItemCarro;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants; // Importación corregida

/*
 * Autor: Judith Piedra
 * Fecha: 15/11/2025
 * Descripción: Servlet encargado de finalizar la compra, generar el archivo PDF de la
 * factura usando iText 7 y forzar su descarga.
 */
@WebServlet("/generar-factura")
public class GenerarFacturaServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(GenerarFacturaServlet.class.getName());

    private String generateFacturaNumber() {
        return "FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Formatea un valor double a la moneda local
     */
    private String formatCurrency(double value) {
        // Usamos Locale para el formato de moneda estándar en español
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);
        return currencyFormat.format(value);
    }

    /**
     * Formatea un número a dos decimales sin el símbolo de moneda
     */
    private String formatDouble(double value) {
        // Usamos String.format para el formato numérico sin símbolo de moneda
        return String.format(new Locale("es", "ES"), "%,.2f", value);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        DetalleCarro detalleCarro = (DetalleCarro) session.getAttribute("carro");
        String username = (String) session.getAttribute("username");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFactura = now.format(formatter);


        // 1. Validación de carrito
        if (detalleCarro == null || detalleCarro.getItems().isEmpty()) {
            // Si el carrito está vacío, redirigir al listado de productos
            resp.sendRedirect(req.getContextPath() + "/productos?info=carroVacio");
            return;
        }

        // 2. CONFIGURACIÓN DE LA RESPUESTA PARA DESCARGA PDF
        String numeroFactura = generateFacturaNumber();
        String filename = "factura_" + numeroFactura + ".pdf";

        resp.setContentType("application/pdf");
        // Header crucial para forzar la descarga en el navegador
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        // 3. GENERACIÓN DEL CONTENIDO PDF CON ITEXt

        try (OutputStream out = resp.getOutputStream()) {

            // Inicializar el escritor de PDF y el documento base
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Usar una fuente estándar para evitar problemas de compatibilidad
            document.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));

            // --- CABECERA DE LA FACTURA ---
            document.add(new Paragraph("FACTURA DE VENTA - E-Commerce Judith")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
            );

            // --- DETALLES DE LA TRANSACCIÓN ---
            document.add(new Paragraph("--------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Número de Factura: " + numeroFactura)
                    .setBold());
            document.add(new Paragraph("Fecha de Emisión: " + fechaFactura));
            document.add(new Paragraph("Cliente: " + (username != null ? username : "Anónimo")));

            document.add(new Paragraph("\n"));

            // --- TABLA DE ARTÍCULOS ---
            // CORRECCIÓN: Agregar 'f' a los números para que sean tratados como float (3f, 2f, etc.)
            Table table = new Table(UnitValue.createPercentArray(new float[]{3f, 2f, 1f, 1f, 1f}));
            table.setWidth(UnitValue.createPercentValue(100));

            // Encabezados de la tabla
            table.addHeaderCell(new Cell().add(new Paragraph("Producto")).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Tipo")).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Cant.")).setBold().setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("P. Unitario")).setBold().setTextAlignment(TextAlignment.RIGHT).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Subtotal")).setBold().setTextAlignment(TextAlignment.RIGHT).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            // Llenar la tabla con los items
            for (ItemCarro item : detalleCarro.getItems()) {
                table.addCell(new Cell().add(new Paragraph(item.getProducto().getNombre())));
                table.addCell(new Cell().add(new Paragraph(item.getProducto().getCategoria())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getCantidad()))).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(formatDouble(item.getProducto().getPrecio()))).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph(formatDouble(item.getSubtotal()))).setTextAlignment(TextAlignment.RIGHT).setBold());
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            // --- RESUMEN DE TOTALES ---

            // CORRECCIÓN: Agregar 'f' a los números para que sean tratados como float (3f, 1.5f)
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{3f, 1.5f}));
            summaryTable.setWidth(UnitValue.createPercentValue(50))
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT);

            // Subtotal Neto
            summaryTable.addCell(new Cell().add(new Paragraph("Subtotal Neto")).setTextAlignment(TextAlignment.RIGHT));
            summaryTable.addCell(new Cell().add(new Paragraph(formatCurrency(detalleCarro.getSubtotalPuro()))).setTextAlignment(TextAlignment.RIGHT));

            // IVA
            summaryTable.addCell(new Cell().add(new Paragraph("IVA (" + String.format("%.0f", detalleCarro.getIvaRate() * 100) + "%)")).setTextAlignment(TextAlignment.RIGHT));
            summaryTable.addCell(new Cell().add(new Paragraph(formatCurrency(detalleCarro.getIva()))).setTextAlignment(TextAlignment.RIGHT));

            // TOTAL FINAL
            summaryTable.addCell(new Cell().add(new Paragraph("TOTAL A PAGAR"))
                    .setBold().setFontSize(14).setBackgroundColor(ColorConstants.YELLOW));
            summaryTable.addCell(new Cell().add(new Paragraph(formatCurrency(detalleCarro.getTotal())))
                    .setBold().setFontSize(14).setTextAlignment(TextAlignment.RIGHT).setBackgroundColor(ColorConstants.YELLOW));

            document.add(summaryTable);

            // Pie de página de cortesía
            document.add(new Paragraph("\n\nGracias por su compra.")
                    .setTextAlignment(TextAlignment.CENTER));


            // 4. Cerrar el documento (fuerza la escritura y el PDF se descarga)
            document.close();

            LOGGER.info("PDF de factura generado con iText: " + filename);

        } catch (Exception e) {
            LOGGER.severe("Error al intentar generar PDF: " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno al generar la factura PDF: " + e.getMessage());
            return;
        }

        // 4. Vaciar el carrito de la sesión SOLO si el PDF se generó exitosamente
        session.removeAttribute("carro");
    }
}