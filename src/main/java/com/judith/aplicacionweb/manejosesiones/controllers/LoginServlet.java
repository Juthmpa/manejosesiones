// Define el paquete donde se encuentra la clase
package com.judith.aplicacionweb.manejosesiones.controllers;

/*
 * Autor: Judith Piedra
 * Fecha: 11/11/2025
 * Descripción: Esta clase denominada LoginServlet,
 * modela el servlet de Producto, clase hija de HttpServlet
 * que va a manejar el inicio de sesión
 */
// Importa la clase ServletException para manejar errores de servlet
import com.judith.aplicacionweb.manejosesiones.services.LoginService;
import com.judith.aplicacionweb.manejosesiones.services.LoginServiceSessionImplement;
import jakarta.servlet.ServletException;
// Importa la anotación WebServlet para mapear la URL
import jakarta.servlet.annotation.WebServlet;
// Importa la clase Cookie para manejar las cookies HTTP
import jakarta.servlet.http.*;
// Importa la clase IOException para manejar errores de entrada/salida
import java.io.IOException;
// Importa la clase PrintWriter para enviar la respuesta al cliente
import java.io.PrintWriter;
// Importa la interfaz Optional para manejar valores que pueden ser nulos
import java.util.Optional;

// Anota el servlet con los paths de URL a los que responde
@WebServlet({"/login", "/login.html"})
// Define la clase LoginServlet que extiende HttpServlet
public class LoginServlet extends HttpServlet {
    // Declaramos e inicializamos una variable constante para el nombre de usuario
    final static String USERNAME = "admin";
    // Declaramos e inicializamos una variable constante para la contraseña
    final static String PASSWORD = "12345";

    // Sobreescribe el métodos doGet para manejar peticiones GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        LoginService auth = new LoginServiceSessionImplement();
        // Obtenemos la información que está dentro de la Session usando un auth
        Optional<String> usernameOptional = auth.getUsername(req);

        // Creamos una condicional preguntando si en la variable usernameOptional
        // Existe información (si el usuario ya ha iniciado sesión previamente)
        if(usernameOptional.isPresent()) {
            // Establece el tipo de contenido y la codificación de la respuesta
            resp.setContentType("text/html;charset=UTF-8");
            // Abre un bloque try-with-resources para obtener el objeto PrintWriter
            try(PrintWriter out = resp.getWriter()){
                // Establece nuevamente el tipo de contenido y codificación (redundante pero incluido)
                resp.setContentType("text/html;charset=UTF-8");
                // Comienza la generación del documento HTML
                out.print("<!DOCTYPE html>");
                // Escribe la etiqueta de apertura del elemento html
                out.println("<html lang=\"es\">");
                // Escribe la etiqueta de apertura del elemento head
                out.println("<head>");
                // Define la codificación de caracteres
                out.println("<meta charset=\"UTF-8\">");
                // Asegura la correcta visualización en dispositivos móviles (viewport)
                out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                // Agrega el enlace a la hoja de estilos de Bootstrap 5
                out.println("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">");
                // Escribe el título de la página, incluyendo el usuario de la cookie
                out.println("<title> Hola: " + usernameOptional.get() +"</title>");
                // Escribe la etiqueta de cierre del elemento head
                out.println("</head>");
                // Escribe la etiqueta de apertura del elemento body con estilo Bootstrap para centrar
                out.println("<body class=\"bg-light d-flex align-items-center justify-content-center\" style=\"min-height: 100vh;\">");
                // Inicia el contenedor principal centrado
                out.println("<div class=\"container text-center p-5 bg-white shadow-lg rounded-3\">");
                // Escribe un encabezado principal con un estilo de texto de éxito
                out.println("<h1 class=\"mb-4 text-success\">Bienvenido de vuelta a mi sistema</h1>");
                // Escribe un encabezado secundario con el mensaje de éxito de inicio de sesión
                out.println("<h3 class=\"alert alert-success\"> Login exitoso: " + usernameOptional.get() + ", has iniciado sesión con éxito. </h3>" );

                // Botones de navegación
                out.println("<div class=\"mt-4\">");
                out.println("<a href = '"+ req.getContextPath() + "/index.html' class='btn btn-secondary me-2'> Volver al inicio </a>");
                out.println("<a href = '"+ req.getContextPath() + "/logout' class='btn btn-danger'> Cerrar Sesión </a>");
                out.println("</div>");

                // Escribe la etiqueta de cierre del div contenedor
                out.println("</div>");

                // Escribe la etiqueta de cierre del elemento body
                out.println("</body>");
                // Escribe la etiqueta de cierre del elemento html
                out.println("</html>");
                // Cierra el bloque try-with-resources
            }
            // Si no existe la cookie (no ha iniciado sesión)
        } else{
            // Redirigimos la petición a login.jsp
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    // Sobreescribe el métodos doPost para manejar peticiones POST (envío de formulario)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // CORRECCIÓN: Usamos "user" que es el nombre del input en login.jsp
        String username = req.getParameter("user");
        // Creamos la variable para procesar la contraseña del formulario
        String password = req.getParameter("password");

        // Implementamos una condicional para saber si las credenciales son correctas
        // Se añade una pequeña comprobación de que el nombre de usuario no es nulo/vacío
        if(username != null && USERNAME.equals(username) && PASSWORD.equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("username", username);

            // Redirección corregida al índice
            resp.sendRedirect(req.getContextPath() + "/productos");

            // Si las credenciales son incorrectas
        } else{
            // Envía un código de error de No Autorizado (401) con un mensaje
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Lo sentimos, no está autorizado para ingresar a esta página. Revisa tus credenciales.");
        }
    }
}
