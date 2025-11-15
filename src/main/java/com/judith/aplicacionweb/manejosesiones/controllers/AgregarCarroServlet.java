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
import com.judith.aplicacionweb.manejosesiones.services.ProductoServiceImplement;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/agregar-carro")
public class AgregarCarroServlet extends HttpServlet {

    // Sobreescribe doGet para manejar la adición (basado en el enlace de ProductoServlet)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Obtener el ID del producto (capturado para ser final)
        Long id = 0L;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto inválido.");
            return;
        }

        // --- SOLUCIÓN: Declaramos una variable final (o efectivamente final) ---
        // Usamos esta variable inmutable en la expresión lambda.
        final Long productoId = id;

        // 2. Buscar el producto por ID
        ProductoService service = new ProductoServiceImplement();
        Optional<Producto> productoOptional = service.porId(productoId); // Usamos productoId

        // 3. Procesar si el producto existe
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();

            // --- VALIDACIÓN DE STOCK ---
            if (producto.getStock() <= 0) {
                resp.sendRedirect(req.getContextPath() + "/productos?error=sinStock");
                return;
            }
            // --- FIN VALIDACIÓN DE STOCK ---

            // Creamos un nuevo item con cantidad 1
            ItemCarro itemCarro = new ItemCarro(1, producto);

            // 4. Obtener o crear la sesión HTTP
            HttpSession session = req.getSession();

            // 5. Obtener el DetalleCarro de la sesión
            DetalleCarro detalleCarro = (DetalleCarro) session.getAttribute("carro");

            // 6. Si el carrito no existe en sesión, se crea uno nuevo
            if (detalleCarro == null) {
                detalleCarro = new DetalleCarro();
                session.setAttribute("carro", detalleCarro);
            }

            // 7. Verificar si ya existe en el carrito para validar stock acumulado
            Optional<ItemCarro> existingItem = detalleCarro.getItems().stream()
                    // Usamos la variable final 'productoId' dentro del lambda
                    .filter(i -> i.getProducto().getIdProducto().equals(productoId))
                    .findAny();

            int currentQuantityInCart = existingItem.map(ItemCarro::getCantidad).orElse(0);

            if (producto.getStock() > currentQuantityInCart) {
                // 8. Agregar el item al carrito (maneja la suma de cantidad si ya existe)
                detalleCarro.addItemCarro(itemCarro);
            } else {
                // Si la cantidad actual en el carrito más 1 excede el stock
                resp.sendRedirect(req.getContextPath() + "/productos?error=stockExcedido&nombre=" + producto.getNombre());
                return;
            }

            // 9. Redirigir al listado de productos
            resp.sendRedirect(req.getContextPath() + "/productos");
        } else {
            // Opcional: Manejar caso en que el producto no existe
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No existe el producto en el catálogo!");
        }
    }
}