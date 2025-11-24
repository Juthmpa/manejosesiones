package com.judith.aplicacionweb.manejosesiones.controllers;

import com.judith.aplicacionweb.manejosesiones.models.Categoria;
import com.judith.aplicacionweb.manejosesiones.services.CategoriaService;
import com.judith.aplicacionweb.manejosesiones.services.CategoriaServiceJdbcImplement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/categoria/form")
public class CategoriaFormServlet extends HttpServlet {

    /**
     * Muestra el formulario para crear o editar una categoría.
     * Si se recibe un 'id', carga la categoría correspondiente.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. OBTENER Y CONFIGURAR LA CONEXIÓN (Inyección de Dependencia)
        // CRÍTICO: Obtener la conexión del atributo de la PETICIÓN (Request)
        Connection conn = (Connection) req.getAttribute("conn");

        if (conn == null) {
            throw new ServletException("Error: La conexión JDBC no está disponible en la petición.");
        }

        CategoriaService service = new CategoriaServiceJdbcImplement(conn);
        Long id = 0L;

        try{
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                id = Long.valueOf(idParam);
            }
        } catch (NumberFormatException ignored){
            // Si el ID es inválido, permanece en 0L (crear nueva categoría)
        }

        Categoria categoria = new Categoria();

        if (id > 0) {
            Optional<Categoria> o = service.porId(id);
            if(o.isPresent()){
                categoria = o.get();
            }
        }

        // Se configura la categoría (nueva o existente) para el formulario
        req.setAttribute("categoria", categoria);

        // Se usa forward para pasar el control al JSP del formulario de categoría
        getServletContext().getRequestDispatcher("/form-categoria.jsp").forward(req, resp);
    }

    /**
     * Procesa los datos del formulario (guardar o actualizar una categoría).
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. OBTENER Y CONFIGURAR LA CONEXIÓN
        Connection conn = (Connection) req.getAttribute("conn");

        if (conn == null) {
            throw new ServletException("Error: La conexión JDBC no está disponible en la petición.");
        }

        CategoriaService service = new CategoriaServiceJdbcImplement(conn);

        // 2. CAPTURA Y VALIDACIÓN DE PARÁMETROS
        String nombre = req.getParameter("nombreCategoria");
        String descripcion = req.getParameter("descripcion");

        Long id = 0L;
        Integer condicion = 1; // Por defecto es 1 (activo)

        Map<String, String> errores = new HashMap<>();

        // Procesamiento del ID
        try {
            String idParam = req.getParameter("id");
            if(idParam != null && !idParam.isBlank()) {
                id = Long.valueOf(idParam);
            }
        } catch (NumberFormatException ignored){}

        // Procesamiento de la Condición
        try {
            String condicionParam = req.getParameter("condicion");
            if(condicionParam != null && !condicionParam.isBlank()) {
                condicion = Integer.valueOf(condicionParam);
            }
        } catch (NumberFormatException ignored){}

        // Validación de Nombre
        if (nombre == null || nombre.isBlank()) {
            errores.put("nombreCategoria", "El nombre de la categoría no puede estar vacío");
        }

        // 3. CREACIÓN DEL OBJETO Y ASIGNACIÓN DE VALORES
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNombreCategoria(nombre);
        categoria.setDescripcion(descripcion);
        categoria.setCondicion(condicion);

        // 4. PROCESAMIENTO
        if (errores.isEmpty()) {
            service.guardar(categoria);
            // REDIRECCIÓN POST-EXITOSA a la lista de categorías
            resp.sendRedirect(req.getContextPath() + "/categoria");
        } else {
            // VOLVER AL FORMULARIO CON ERRORES Y DATOS
            req.setAttribute("errores", errores);
            req.setAttribute("categoria", categoria);

            // Usamos el mismo dispatcher que en doGet para volver a mostrar el formulario
            getServletContext().getRequestDispatcher("/form-categoria.jsp").forward(req, resp);
        }
    }
}
