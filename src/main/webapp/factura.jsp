<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.judith.aplicacionweb.manejosesiones.models.DetalleCarro" %>
<%@ page import="com.judith.aplicacionweb.manejosesiones.models.ItemCarro" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.List" %>

<%--
    Autor: Judith Piedra (Estilizado por Gemini)
    Fecha: 14/11/2025
    Descripción: Vista JSP que muestra la factura final de la compra,
    utilizando Tailwind CSS para un diseño limpio y profesional.
--%>

<%!
    // Genera un número de factura aleatorio simulado
    private String generateFacturaNumber() {
        return "FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Nueva utilidad para formatear números a moneda (reemplaza fmt:formatNumber)
    private String formatCurrency(double value) {
        // Usamos Locale para el formato de moneda estándar en español
        // Se puede cambiar a un Locale específico si es necesario (ej: new Locale("es", "EC"))
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);
        return currencyFormat.format(value);
    }
%>

<%
    // Obtener la fecha y hora actual
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String fechaFactura = now.format(formatter);

    // Generar el número de factura
    String numeroFactura = generateFacturaNumber();

    // Obtener el objeto DetalleCarro del request (que fue puesto por GenerarFacturaServlet)
    DetalleCarro factura = (DetalleCarro) request.getAttribute("facturaCarro");
    List<ItemCarro> items = factura.getItems();

    // Obtener el nombre de usuario de la sesión
    String username = (String) session.getAttribute("username");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Factura de Compra</title>
    <!-- Incluir Tailwind CSS para estilizar -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Incluir FontAwesome para iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap');
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f4f7f9;
        }
        .invoice-card {
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body class="min-h-screen p-4 sm:p-8">

<div class="max-w-4xl mx-auto bg-white rounded-xl p-6 md:p-10 invoice-card">

    <%-- 1. Cabecera y Metadatos de la Factura --%>
    <div class="flex flex-col sm:flex-row justify-between border-b pb-4 mb-6">
        <div class="flex items-center mb-4 sm:mb-0">
            <i class="fas fa-store-alt text-indigo-600 text-4xl mr-3"></i>
            <h1 class="text-3xl font-bold text-gray-800">E-Commerce Judith</h1>
        </div>
        <div class="text-sm text-right">
            <p class="font-semibold text-gray-700">Factura #: <span class="text-indigo-600"><%= numeroFactura %></span></p>
            <p class="text-gray-500">Generada: <%= fechaFactura %></p>
        </div>
    </div>

    <%-- 2. Información del Cliente --%>
    <div class="bg-indigo-50 p-4 rounded-lg mb-6 border border-indigo-200">
        <h2 class="text-lg font-semibold text-indigo-800 mb-2">Detalles del Cliente</h2>
        <p class="text-gray-700">Cliente: <span class="font-medium"><%= username != null ? username : "Anónimo (sesión no iniciada)" %></span></p>
        <p class="text-gray-500 text-sm">Estado: Compra Finalizada y Procesada</p>
    </div>

    <%-- 3. Detalle de los Productos --%>
    <h2 class="text-xl font-bold text-gray-800 mb-4">Artículos Comprados</h2>

    <div class="overflow-x-auto mb-6">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-100">
            <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider rounded-tl-lg">Descripción</th>
                <th scope="col" class="px-6 py-3 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider">Cantidad</th>
                <th scope="col" class="px-6 py-3 text-right text-xs font-semibold text-gray-700 uppercase tracking-wider">P. Unitario</th>
                <th scope="col" class="px-6 py-3 text-right text-xs font-semibold text-gray-700 uppercase tracking-wider rounded-tr-lg">Total Item</th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            <% for (ItemCarro item : items) { %>
            <tr>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    <%= item.getProducto().getNombre() %>
                    <span class="text-xs text-gray-500 block">Tipo: <%= item.getProducto().getCategoria() %></span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700 text-center"><%= item.getCantidad() %></td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 text-right">
                    <%= formatCurrency(item.getProducto().getPrecio()) %>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700 font-semibold text-right">
                    <%= formatCurrency(item.getSubtotal()) %>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <%-- 4. Resumen y Totales --%>
    <div class="flex justify-end">
        <div class="w-full md:w-1/2 p-4 bg-gray-50 rounded-lg border">
            <div class="flex justify-between items-center mb-2">
                <span class="text-gray-600">Subtotal Neto:</span>
                <span class="text-lg font-medium text-gray-800"><%= formatCurrency(factura.getSubtotalPuro()) %></span>
            </div>
            <div class="flex justify-between items-center mb-2">
                <span class="text-gray-600">IVA (<%= String.format("%.0f", factura.getIvaRate() * 100) %>%):</span>
                <span class="text-lg font-medium text-red-600"><%= formatCurrency(factura.getIva()) %></span>
            </div>
            <div class="border-t pt-3 mt-3 flex justify-between items-center">
                <span class="text-xl font-bold text-gray-900">TOTAL A PAGAR:</span>
                <span class="text-2xl font-extrabold text-indigo-700"><%= formatCurrency(factura.getTotal()) %></span>
            </div>
        </div>
    </div>

    <%-- 5. Mensaje y Botón de Acción --%>
    <div class="mt-8 text-center">
        <div class="p-4 bg-green-100 text-green-800 rounded-lg mb-4 font-semibold">
            <i class="fas fa-check-circle me-2"></i> ¡El proceso de pago ha sido exitoso! Gracias por tu compra.
        </div>

        <a href="<%= request.getContextPath() %>/productos"
           class="inline-flex items-center px-6 py-3 bg-indigo-600 text-white font-bold rounded-lg hover:bg-indigo-700 transition duration-300 shadow-xl shadow-indigo-400/50">
            <i class="fas fa-store me-2"></i> Regresar al Catálogo
        </a>
    </div>
</div>

</body>
</html>