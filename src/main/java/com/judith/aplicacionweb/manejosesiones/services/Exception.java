package com.judith.aplicacionweb.manejosesiones.services;

public class Exception extends RuntimeException {
    /* Implementamos un constructor sonde recibe como parámetro un mensaje
     * Luego llamamos a la clase consstructor de la clase padre para que
     *        lance la excepcion.
     */
    public Exception(String message) {
        super(message);
    }
}
