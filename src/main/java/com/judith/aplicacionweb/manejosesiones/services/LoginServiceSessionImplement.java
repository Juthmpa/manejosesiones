package com.judith.aplicacionweb.manejosesiones.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class LoginServiceSessionImplement implements LoginService{
    @Override
    public Optional<String> getUsername(HttpServletRequest req) {
        // Obtengo la sesión
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        if(username!=null){
            return Optional.of(username);
        }
        return Optional.empty();
    }
}