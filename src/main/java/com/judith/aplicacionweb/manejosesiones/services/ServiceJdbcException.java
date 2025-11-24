package com.judith.aplicacionweb.manejosesiones.services;

/**
 * Clase de excepción personalizada que hereda de RuntimeException.
 * Se utiliza para encapsular errores de la capa de acceso a datos (SQLException)
 * y evitar exponerlos a capas superiores (Servlets/JSP), manteniendo el código limpio.
 */
public class ServiceJdbcException extends RuntimeException {

    /**
     * Constructor que implementa un constructor de la clase padre
     * para lanzar la excepción con un mensaje.
     *
     * @param mensaje El mensaje de error.
     */
    public ServiceJdbcException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor que implementa un constructor de la clase padre
     * para lanzar la excepción con un mensaje y la causa técnica.
     *
     * @param mensaje El mensaje de error.
     * @param cause La causa de la excepción (por ejemplo, la SQLException original).
     */
    public ServiceJdbcException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}