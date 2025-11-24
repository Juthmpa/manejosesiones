package com.judith.aplicacionweb.manejosesiones.controllers;

import com.judith.aplicacionweb.manejosesiones.models.Categoria;
import com.judith.aplicacionweb.manejosesiones.models.Producto;
import com.judith.aplicacionweb.manejosesiones.services.ProductoService;
import com.judith.aplicacionweb.manejosesiones.services.ProductoServiceJdbcImplement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet ("/producto/form")
public class ProductoFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Obtenemos la conexión del atributo de la PETICIÓN (Request)
        Connection conn = (Connection) req.getAttribute("conn");

        // Verificamos que la conexión exista antes de usarla (aunque el filtro ya debería asegurar esto)
        if (conn == null) {
            throw new ServletException("Error: La conexión JDBC no está disponible en la petición.");
        }

        ProductoService service = new ProductoServiceJdbcImplement(conn);
        Long id = 0L;

        try{
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                id = Long.valueOf(idParam);
            }
        } catch (NumberFormatException e){
            // Si el ID es inválido, permanece en 0L
        }

        Producto producto = new Producto();
        producto.setCategoria(new Categoria()); // Inicializar la categoría para evitar NullPointerException en el JSP.

        if (id > 0) {
            Optional<Producto> o = service.porId(id);
            if(o.isPresent()){
                producto = o.get();
            }
        }

        // Se configuran los atributos para el JSP
        req.setAttribute("categorias", service.listarCategoria());
        req.setAttribute("producto", producto);

        // Se usa forward para pasar el control al JSP
        getServletContext().getRequestDispatcher("/form-producto.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // FIX CRÍTICO: Obtenemos la conexión del atributo de la PETICIÓN (Request)
        Connection conn = (Connection) req.getAttribute("conn");

        // Verificamos que la conexión exista antes de usarla
        if (conn == null) {
            throw new ServletException("Error: La conexión JDBC no está disponible en la petición.");
        }

        ProductoService service = new ProductoServiceJdbcImplement(conn);

        // 1. CAPTURA Y VALIDACIÓN DE PARÁMETROS
        String nombre = req.getParameter("nombre");
        String descripcion = req.getParameter("descripcion");
        String codigo = req.getParameter("codigo");
        String fecha_elaboracion = req.getParameter("fecha_elaboracion");
        String fecha_caducidad = req.getParameter("fecha_caducidad");

        Long id = 0L;
        Long categoriaId = 0L;
        Integer stock = 0;
        Double precio = null;
        String precioParam = req.getParameter("precio");

        Map<String, String> errores = new HashMap<>();

        // Procesamiento del ID
        try {
            String idParam = req.getParameter("id");
            if(idParam != null && !idParam.isBlank()) {
                id = Long.valueOf(idParam);
            }
        } catch (NumberFormatException ignored){} // Ignorado ya que el ID es interno

        // Validación de Categoria ID
        try{
            String catIdParam = req.getParameter("categoriaId");
            if(catIdParam != null && !catIdParam.isBlank()) {
                categoriaId = Long.valueOf(catIdParam);
            }
        }catch (NumberFormatException ignored){}

        if (categoriaId.equals(0L)) {
            errores.put("categoria", "La categoría no puede estar vacía");
        }

        // Validación de Stock
        try{
            String stockParam = req.getParameter("stock");
            if(stockParam != null && !stockParam.isBlank()) {
                stock = Integer.valueOf(stockParam);
            }
        }catch (NumberFormatException e){
            errores.put("stock", "El stock debe ser un número válido");
        }
        if (stock <= 0) {
            errores.put("stock", "El stock debe ser mayor a cero");
        }

        // Validación de Nombre y Código
        if (nombre == null || nombre.isBlank()) {
            errores.put("nombre", "El nombre no puede estar vacío");
        }
        if (codigo == null || codigo.isBlank()) {
            errores.put("codigo","El código no puede estar vacío");
        }

        // Validación de Precio
        if(precioParam == null || precioParam.trim().isEmpty()){
            errores.put("precio","El precio no puede estar vacío");
        } else {
            precioParam = precioParam.trim().replace(",",".");
            try{
                precio = Double.valueOf(precioParam);
                if(precio < 0){
                    errores.put("precio","El precio debe ser mayor a cero");
                }
            } catch (NumberFormatException e){
                errores.put("precio", "El precio debe ser un número válido");
            }
        }

        // Transformación y Validación de la Fecha
        LocalDate fechaElaboracion = null;
        LocalDate fechaCaducidad = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(fecha_elaboracion == null || fecha_elaboracion.isBlank()){
            errores.put("fecha_elaboracion", "La fecha de elaboración no puede estar vacía");
        } else {
            try{
                fechaElaboracion = LocalDate.parse(fecha_elaboracion, formatter);
            } catch (DateTimeParseException e){
                errores.put("fecha_elaboracion", "Formato de fecha de elaboración inválido");
            }
        }

        if(fecha_caducidad == null || fecha_caducidad.isBlank()){
            errores.put("fecha_caducidad","La fecha de caducidad no puede estar vacía");
        } else {
            try{
                fechaCaducidad = LocalDate.parse(fecha_caducidad, formatter);
            } catch (DateTimeParseException e){
                errores.put("fecha_caducidad", "Formato de fecha de caducidad inválido");
            }
        }

        // Validación Lógica de Fechas
        if (fechaElaboracion != null && fechaCaducidad != null && fechaCaducidad.isBefore(fechaElaboracion)) {
            errores.put("fecha_caducidad", "La fecha de caducidad no puede ser anterior a la de elaboración");
        }

        // 2. CREACIÓN DEL OBJETO Y ASIGNACIÓN DE VALORES
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombreProducto(nombre);
        producto.setStock(stock);
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion);
        producto.setCodigo(codigo);
        producto.setFechaElaboracion(fechaElaboracion);
        producto.setFechaCaducidad(fechaCaducidad);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        producto.setCategoria(categoria);

        // 3. PROCESAMIENTO
        if (errores.isEmpty()) {
            service.guardar(producto);
            // REDIRECCIÓN POST-EXITOSA
            resp.sendRedirect(req.getContextPath() + "/producto");
        } else {
            // VOLVER AL FORMULARIO CON ERRORES Y DATOS
            req.setAttribute("errores", errores);
            req.setAttribute("categorias", service.listarCategoria());
            req.setAttribute("producto", producto);
            getServletContext().getRequestDispatcher("/form-producto.jsp").forward(req, resp);
        }
    }
}