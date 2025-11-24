package com.judith.aplicacionweb.manejosesiones.services;
/*
 * Autor: Judith Piedra
 * Fecha: 11/11/2025
 * Descripción: Esta clase denominada LoginService
 *
 */
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Interfaz que define el contrato para un servicio de gestión de inicio de sesión.
 * Se utiliza para obtener información del usuario, típicamente el nombre de usuario,
 * de la sesión HTTP o de las cookies de la solicitud.
 */
public interface LoginService {

    /**
     * Obtiene el nombre de usuario de la solicitud HTTP actual.
     * El nombre de usuario puede estar almacenado en la sesión o en alguna otra parte de la solicitud
     * después de un inicio de sesión exitoso.
     *
     * @param req La solicitud HTTP actual (HttpServletRequest).
     * @return Un objeto Optional<String> que contiene el nombre de usuario si está presente,
     * o un Optional vacío si no se encuentra (indicando que el usuario no ha iniciado sesión o no se puede identificar).
     */    Optional<String> getUsername(HttpServletRequest req);
}

