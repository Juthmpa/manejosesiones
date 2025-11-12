package com.judith.aplicacionweb.manejosesiones.controllers;

import com.judith.aplicacionweb.manejosesiones.services.LoginService;
import com.judith.aplicacionweb.manejosesiones.services.LoginServiceSessionImplement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    // Método auxiliar para manejar la lógica de cerrar sesión (GET y POST)
    protected void processLogout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> username =  auth.getUsername(req);

        if(username.isPresent()) {
            // Usamos getSession(false) para no crear una nueva sesión si no existe
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate(); // Invalida la sesión actual
            }
        }

        // Redirige siempre al index después de intentar el logout
        resp.sendRedirect(req.getContextPath() + "/index.html");
    }

    // CORRECCIÓN: El enlace directo del LoginServlet y el index usan GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processLogout(req, resp);
    }

    // CORRECCIÓN: El botón "Cerrar sesión" en ProductoServlet usa POST
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processLogout(req, resp);
    }
}