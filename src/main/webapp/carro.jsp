<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 14/11/2025
  Time: 16:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.judith.aplicacionweb.manejosesiones.models.DetalleCarro" %>
<%@ page import="com.judith.aplicacionweb.manejosesiones.models.ItemCarro" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--
    Autor: Judith Piedra (Adaptación por Gemini)
    Fecha: 14/11/2025
    Descripción: Vista JSP para mostrar el detalle del carrito de compras (DetalleCarro)
    usando scriptlets y expresiones, y aplicando estilos modernos con Tailwind CSS.
    Se asegura compatibilidad con DetalleCarro.java (usa getCategoria(), no getTipo()).
--%>

<%!
    // Utilidad para formatear números a dos decimales y usar formato de miles si aplica.
    private String formatDouble(double value) {
        // Usa el formato de Locale para garantizar el separador de miles y dos decimales
        return String.format("%,.2f", value);
    }
%>

<%
    // Obtener el carrito de la sesión
    DetalleCarro carro = (DetalleCarro) session.getAttribute("carro");

    // Inicializar el carrito si es nulo para evitar NullPointerException
    if (carro == null) {
        carro = new DetalleCarro();
    }
    List<ItemCarro> items = carro.getItems();
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tu Carrito de Compras</title>
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
        .card {
            box-shadow: 0 0px 15px rgba(0, 0, 0, 0.1);
        }
        /* Estilos específicos para la tabla y centrado en móvil */
        @media (max-width: 640px) {
            .table-mobile-center td, .table-mobile-center th {
                text-align: center !important;
            }
        }
    </style>
</head>
<body class="min-h-screen p-4 sm:p-8">

<div class="max-w-4xl mx-auto bg-white rounded-xl p-6 md:p-10 card">
    <h1 class="text-3xl font-bold text-gray-800 mb-6 border-b pb-2">
        <i class="fas fa-shopping-cart text-indigo-600 mr-3"></i> Detalle del Carrito
    </h1>

    <% if (items.isEmpty()) { %>
    <div class="text-center py-12 bg-gray-50 rounded-lg border border-dashed border-gray-300">
        <i class="fas fa-box-open text-gray-400 text-6xl mb-4"></i>
        <p class="text-xl text-gray-600 font-medium">Tu carrito está vacío.</p>
        <p class="text-gray-500 mt-2">¡Añade algunos productos de nuestro catálogo!</p>
        <a href="<%= request.getContextPath() %>/productos" class="mt-4 inline-block px-6 py-2 bg-indigo-600 text-white font-semibold rounded-lg hover:bg-indigo-700 transition duration-300">
            Ir a Productos
        </a>
    </div>
    <% } else { %>
    <div class="overflow-x-auto mb-8">
        <table class="min-w-full divide-y divide-gray-200 table-mobile-center">
            <thead class="bg-indigo-50">
            <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider rounded-tl-lg">Producto</th>
                <th scope="col" class="px-6 py-3 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider">Precio U.</th>
                <th scope="col" class="px-6 py-3 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider">Cantidad</th>
                <th scope="col" class="px-6 py-3 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider">Subtotal Neto</th>
                <th scope="col" class="px-6 py-3 text-center text-xs font-semibold text-gray-700 uppercase tracking-wider rounded-tr-lg">Acciones</th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            <% for (ItemCarro item : items) { %>
            <tr>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    <%= item.getProducto().getNombre() %>
                    <%-- USAMOS getCategoria() que está definido en Producto.java --%>
                    <span class="text-xs text-gray-500 block"><%= item.getProducto().getCategoria() %></span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 text-center">
                    $<%= formatDouble(item.getProducto().getPrecio()) %>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 text-center font-bold">
                    <%= item.getCantidad() %>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700 text-center font-semibold">
                    $<%= formatDouble(item.getSubtotal()) %>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-center text-sm font-medium">
                    <div class="flex justify-center space-x-2">
                        <!-- Botón para quitar 1 unidad (Servlet /eliminar-item) -->
                        <%-- Usamos getIdProducto() para obtener el ID --%>
                        <a href="<%= request.getContextPath() %>/eliminar-item?id=<%= item.getProducto().getIdProducto() %>"
                           class="text-red-500 hover:text-red-700 transition duration-150 p-1 rounded-full bg-red-50 hover:bg-red-100"
                           title="Quitar 1 unidad">
                            <i class="fas fa-minus w-4 h-4"></i>
                        </a>
                        <!-- Botón para añadir 1 unidad (Servlet /agregar-carro) -->
                        <%-- Usamos getIdProducto() para obtener el ID --%>
                        <a href="<%= request.getContextPath() %>/agregar-carro?id=<%= item.getProducto().getIdProducto() %>"
                           class="text-green-500 hover:text-green-700 transition duration-150 p-1 rounded-full bg-green-50 hover:bg-green-100"
                           title="Añadir 1 unidad">
                            <i class="fas fa-plus w-4 h-4"></i>
                        </a>
                    </div>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <!-- Sección de Totales -->
    <div class="flex justify-end">
        <div class="w-full md:w-1/2 p-4 bg-gray-50 rounded-lg border">
            <div class="flex justify-between items-center mb-2">
                <span class="text-gray-600">Subtotal Neto:</span>
                <span class="text-lg font-medium text-gray-800">$<%= formatDouble(carro.getSubtotalPuro()) %></span>
            </div>
            <div class="flex justify-between items-center mb-2">
                <!-- Usamos getIvaRate() de DetalleCarro para mostrar la tasa de IVA -->
                <span class="text-gray-600">IVA (<%= formatDouble(carro.getIvaRate() * 100) %>%) :</span>
                <span class="text-lg font-medium text-red-600">$<%= formatDouble(carro.getIva()) %></span>
            </div>
            <div class="border-t pt-3 mt-3 flex justify-between items-center">
                <span class="text-xl font-bold text-gray-900">TOTAL A PAGAR:</span>
                <span class="text-2xl font-extrabold text-indigo-700">$<%= formatDouble(carro.getTotal()) %></span>
            </div>
        </div>
    </div>

    <!-- Botones de Acción -->
    <div class="flex justify-between mt-8 flex-col sm:flex-row space-y-4 sm:space-y-0 sm:space-x-4">
        <a href="<%= request.getContextPath() %>/productos"
           class="px-6 py-3 bg-indigo-100 text-indigo-700 font-semibold rounded-lg hover:bg-indigo-200 transition duration-300 text-center">
            <i class="fas fa-arrow-left mr-2"></i> Seguir Comprando
        </a>
        <!-- Botón para Vaciar Carrito (usan el Servlet /vaciar-carro) -->
        <a href="<%= request.getContextPath() %>/vaciar-carro"
           class="px-6 py-3 bg-red-100 text-red-700 font-semibold rounded-lg hover:bg-red-200 transition duration-300 text-center">
            <i class="fas fa-trash-alt mr-2"></i> Vaciar Carrito
        </a>

        <!-- Botón para Generar Factura (usan el Servlet /generar-factura) -->
        <%-- El botón de finalizar compra tiene un estilo más prominente --%>
        <a href="<%= request.getContextPath() %>/generar-factura" class="px-8 py-3 bg-green-600 text-white font-bold rounded-lg hover:bg-green-700 transition duration-300 shadow-xl shadow-green-400/50 text-center">
            <i class="fas fa-file-invoice-dollar mr-2"></i> Generar Factura / Pagar
        </a>
    </div>
    <% } %>

</div>

</body>
</html>