package com.judith.aplicacionweb.manejosesiones.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/*
 * Autor: Judith Piedra
 * Fecha: 14/11/2025
 * Descripción: Servlet encargado de vaciar completamente el carrito de compras
 * eliminando el atributo "carro" de la sesión del usuario.
 */
@WebServlet("/vaciar-carro")
public class VaciarCarroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Obtener la sesión HTTP (usamos false para no crearla si no existe)
        HttpSession session = req.getSession(false);

        // Si la sesión existe, buscamos el atributo del carrito
        if (session != null) {
            // Eliminar el atributo "carro" de la sesión
            session.removeAttribute("carro");
            // Se puede agregar un mensaje flash aquí si se desea
        }

        // Redirigir al listado de productos o a la página del carrito
        // Redireccionamos a la lista de productos para ver el catálogo.
        resp.sendRedirect(req.getContextPath() + "/productos");
    }
}
