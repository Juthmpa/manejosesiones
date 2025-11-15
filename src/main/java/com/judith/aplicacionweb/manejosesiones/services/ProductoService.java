// Define el paquete donde se encuentra esta interfaz
package com.judith.aplicacionweb.manejosesiones.services;
/*
 * Autor: Judith Piedra
 * Fecha: 10/11/2025
 * Descripción: Esta clase denominada ProductoService
 * es una clase abstracta que sirve como
 * plantilla que va a utilizar Producto
 * para el uso en el Servlet
 */
// Importa la clase Producto del paquete models
import com.judith.aplicacionweb.manejosesiones.models.Producto;
// Importa la interfaz List de java.util
import java.util.List;
import java.util.Optional;

// Define la interfaz pública ProductoService
public interface ProductoService {
    // Declara el métodos 'listar' que debe ser implementado
    // Este métodos no recibe argumentos y retorna una lista de objetos Producto
    List<Producto> listar();

    // Métodos para buscar un producto por ID, envuelto en Optional para manejar nulls
    Optional<Producto> porId(Long id);

}