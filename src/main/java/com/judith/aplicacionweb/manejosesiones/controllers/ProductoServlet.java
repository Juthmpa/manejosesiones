package com.judith.aplicacionweb.manejosesiones.controllers;
/*
 * Autor: Judith Piedra
 * Fecha: 11/11/2025
 * Descripción: Esta clase denominada ProductoServlet,
 * modela el servlet de Producto, clase hija de HttpServlet
 *
 */
// Importa la clase ServletException del API de Jakarta Servlet
import com.judith.aplicacionweb.manejosesiones.services.LoginService;
import com.judith.aplicacionweb.manejosesiones.services.LoginServiceSessionImplement;
import jakarta.servlet.ServletException;
// Importa la anotación WebServlet para mapear el servlet a una URL
import jakarta.servlet.annotation.WebServlet;
// Importa la clase base HttpServlet
import jakarta.servlet.http.HttpServlet;
// Importa la clase HttpServletRequest para manejar la solicitud HTTP
import jakarta.servlet.http.HttpServletRequest;
// Importa la clase HttpServletResponse para manejar la respuesta HTTP
import jakarta.servlet.http.HttpServletResponse;
// Importa la clase Cookie para manejar las cookies
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession; // Necesario para manejar la sesión
// Importa la clase Producto del paquete models
import com.judith.aplicacionweb.manejosesiones.models.Producto;
// Importa la interfaz ProductoService del paquete services
import com.judith.aplicacionweb.manejosesiones.services.ProductoService;
// Importa la implementación ProductoServiceImplement del paquete services
import com.judith.aplicacionweb.manejosesiones.services.ProductoServiceImplement;
// Importa la clase IOException para manejar errores de entrada/salida
import java.io.IOException;
// Importa la clase PrintWriter para enviar la respuesta al cliente
import java.io.PrintWriter;
// Importa la interfaz List de java.util
import java.util.List;
// Importa la interfaz Optional de java.util
import java.util.Optional;
// Importa la interfaz Arrays de java.util
import java.util.Arrays;

// Mapea este Servlet a la URL "/producto"
@WebServlet({"/productos.html", "/productos"})
public class ProductoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Instancia del servicio de productos
        ProductoService service = new ProductoServiceImplement();
        // Obtiene la lista de productos
        List<Producto> productos = service.listar();

        // Servicio de autenticación para verificar la sesión
        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> usernameOptional = auth.getUsername(req);

        // --- LÓGICA DEL CONTADOR DE HITS DE SESIÓN (MOVIDA AQUÍ) ---
        Integer counter = null;
        if(usernameOptional.isPresent()) {
            HttpSession session = req.getSession();
            // Obtener el contador de la sesión (puede ser null la primera vez)
            counter = (Integer) session.getAttribute("hitCounter");

            if (counter == null) {
                // Si es la primera vez, inicializamos en 1
                counter = 1;
            } else {
                // Si ya existe, incrementamos
                counter++;
            }
            // Guardamos el nuevo valor en la sesión
            session.setAttribute("hitCounter", counter);
        }
        // -----------------------------------------------------------

        // Establece tipo de contenido HTML con codificación UTF-8
        resp.setContentType("text/html; charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {

            // Comienza a generar la respuesta HTML con Bootstrap
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            // Configuración crucial para la adaptabilidad (responsive design)
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no'>");
            out.println("<title>Lista de Productos</title>");
            // Enlace a Bootstrap 5
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>");
            // Enlace a Font Awesome para el ícono de carrito
            out.println("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css'>");

            // CSS simple para posicionar el botón de carrito flotante
            out.println("<style>");
            out.println(".table td, .table th { vertical-align: middle; }");
            // Botón de carrito flotante y adaptable (más grande en escritorio, más pequeño en móvil)
            out.println(".btn-carro { position: fixed; bottom: 20px; right: 20px; z-index: 1000; padding: 15px; border-radius: 50%; box-shadow: 0 4px 8px rgba(0,0,0,0.4); }");
            out.println("@media (max-width: 576px) { .btn-carro { bottom: 10px; right: 10px; padding: 12px; font-size: 1.2rem; } }");
            out.println("</style>");

            out.println("</head>");
            out.println("<body class='bg-light'>");

            // Contenedor principal con margen superior
            out.println("<div class='container mt-5'>");
            out.println("<div class='card shadow-lg'>");
            out.println("<div class='card-header bg-success text-white text-center'>");
            out.println("<h1 class='mb-0'>Catálogo de Productos</h1>");
            out.println("</div>");
            out.println("<div class='card-body'>");

            // Mensaje de Bienvenida y Contador
            if(usernameOptional.isPresent()) {
                out.println("<div class='alert alert-success' role='alert'>");
                out.println("Hola <strong>" + usernameOptional.get() + "</strong>. ¡Bienvenido!");
                out.println("</div>");

                // --- MOSTRAR EL CONTADOR ---
                out.println("<p class='text-muted mt-3 text-center'>Esta página se ha recargado <strong>" + counter + "</strong> veces en esta sesión.</p>");
                // ---------------------------

                // --- BOTÓN FLOTANTE DE VER CARRO CON ÍCONO ---
                out.println("<a href='" + req.getContextPath() + "/ver-carro' class='btn btn-warning btn-carro' title='Ver Carrito'>");
                out.println("<i class='fas fa-shopping-cart fa-2x'></i>"); // Ícono de carrito
                out.println("</a>");
                // ---------------------------------------------

            } else {
                out.println("<div class='alert alert-warning' role='alert'>");
                out.println("Inicia sesión para ver los precios y las opciones de compra.");
                out.println("</div>");
            }

            // Tabla de productos adaptable
            out.println("<div class='table-responsive'>");
            out.println("<table class='table table-bordered table-hover align-middle'>");
            out.println("<thead class='table-success text-center'>");
            out.println("<tr>");
            out.println("<th>ID</th>"); // Se renombra a ID por simplicidad
            out.println("<th>NOMBRE</th>");
            out.println("<th>CATEGORÍA</th>");

            if (usernameOptional.isPresent()) {
                out.println("<th>PRECIO</th>");
                out.println("<th>STOCK</th>"); // Nueva columna para mostrar el stock
                out.println("<th>ACCIÓN</th>"); // Columna para el botón de agregar
            }
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            // Itera sobre la lista de productos
            productos.forEach(p->{
                out.println("<tr>");
                // Nota: se asume que los getters en Producto son: getId(), getNombre(), getTipo(), getPrecio(), getStock()
                out.println("<td class='text-center'>" + p.getIdProducto() + "</td>");
                out.println("<td>" + p.getNombre() + "</td>");
                out.println("<td>" + p.getCategoria() + "</td>"); // Asume getTipo() para la categoría

                if (usernameOptional.isPresent()) {
                    // Muestra el precio formateado a dos decimales
                    out.println("<td class='text-end'>$ " + String.format("%.2f", p.getPrecio()) + "</td>");

                    // Muestra el stock
                    out.println("<td class='text-center'>" + p.getStock() + "</td>");

                    // --- LÓGICA DE STOCK Y ACCIÓN ---
                    out.println("<td class='text-center'>");
                    if (p.getStock() > 0) { // Validación: solo muestra el botón si hay stock
                        out.println("<a href='"
                                + req.getContextPath()
                                + "/agregar-carro?id=" + p.getIdProducto() // Enlace al servlet de agregar
                                + "' class='btn btn-sm btn-primary'>Agregar <i class='fas fa-cart-plus'></i></a>");
                    } else {
                        out.println("<span class='text-danger fw-bold'>Agotado</span>");
                    }
                    out.println("</td>");
                    // ---------------------------------
                }
                out.println("</tr>");
            });

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");

            // Botones de sesión
            out.println("<div class='mt-4 text-center'>");

            if (usernameOptional.isPresent()) {
                // Formulario POST para cerrar sesión
                out.println("<form action='" + req.getContextPath() + "/logout' method='post' style='display:inline;'>");
                out.println("<button type='submit' class='btn btn-danger me-2'>Cerrar sesión</button>");
                out.println("</form>");
            } else {
                // Enlace para iniciar sesión
                out.println("<a href='" + req.getContextPath() + "/login.jsp' class='btn btn-primary me-2'>Iniciar sesión</a>");
            }

            // Botón Regresar (redirige correctamente al inicio)
            out.println("<a href='" + req.getContextPath() + "/index.html' class='btn btn-outline-secondary'>Regresar al inicio</a>");
            out.println("</div>");

            // Cierre de card y container
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");

            // Script de Bootstrap (necesario para componentes como el modal)
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}