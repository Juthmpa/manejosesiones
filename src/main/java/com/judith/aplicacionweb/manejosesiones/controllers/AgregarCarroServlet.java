package com.judith.aplicacionweb.manejosesiones.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.judith.aplicacionweb.manejosesiones.models.DetalleCarro;
import com.judith.aplicacionweb.manejosesiones.models.ItemCarro;
import com.judith.aplicacionweb.manejosesiones.models.Producto;
import com.judith.aplicacionweb.manejosesiones.services.ProductoService;
// Usaremos la implementación JDBC ya que usa 'conn'
import com.judith.aplicacionweb.manejosesiones.services.ProductoServiceJdbcImplement;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

/**
 * Servlet encargado de añadir un producto al carro de compras,
 * utilizando la conexión a la base de datos para obtener el producto.
 */
@WebServlet("/agregar-carro")
public class AgregarCarroServlet extends HttpServlet {

    /**
     * Procesa la petición GET para añadir un producto al carro de la sesión.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // La clase obtiene el ID del producto de la petición y lo convierte a Long.
        Long id = Long.parseLong(req.getParameter("id"));

        // La clase obtiene una conexión a la BBDD del request.
        Connection conn = (Connection) req.getAttribute("conn");

        // La clase inicializa el servicio de productos con la conexión obtenida.
        ProductoService service = new ProductoServiceJdbcImplement(conn);

        // La clase busca el producto por su ID.
        Optional<Producto> producto = service.porId(id);

        // La clase verifica si el producto existe.
        if (producto.isPresent()) {
            // La clase crea un nuevo ItemCarro con cantidad 1.
            ItemCarro item = new ItemCarro(1, producto.get());

            // La clase obtiene la sesión actual (o crea una nueva si no existe).
            HttpSession session = req.getSession();

            // La clase intenta obtener el objeto DetalleCarro de la sesión.
            DetalleCarro detalleCarro;
            if (session.getAttribute("carro") == null) {
                // Si no existe, la clase crea un nuevo DetalleCarro.
                detalleCarro = new DetalleCarro();
                // La clase lo guarda en la sesión.
                session.setAttribute("carro", detalleCarro);
            } else {
                // Si ya existe, la clase lo recupera de la sesión.
                detalleCarro = (DetalleCarro) session.getAttribute("carro");
            }

            // La clase añade el item al DetalleCarro, manejando la lógica de si ya existe.
            detalleCarro.addItemCarro(item);
        }

        // La clase redirige al listado de productos después de añadir el item.
        resp.sendRedirect(req.getContextPath() + "/productos");
    }
}