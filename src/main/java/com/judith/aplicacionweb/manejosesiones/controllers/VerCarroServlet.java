package com.judith.aplicacionweb.manejosesiones.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
 * Autor: Judith Piedra
 * Fecha: 14/11/2025
 * Descripción: Servlet encargado de reenviar (forward) la solicitud
 * a la vista JSP del carrito de compras (carro.jsp).
 */
@WebServlet("/ver-carro")
public class VerCarroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Simplemente reenvía la solicitud a la vista JSP donde se maneja la lógica de la sesión
        getServletContext().getRequestDispatcher("/carro.jsp").forward(req, resp);
    }
}
