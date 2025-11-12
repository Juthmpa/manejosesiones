// Define el paquete donde se encuentra esta clase
package com.judith.aplicacionweb.manejosesiones.services;
/*
 * Autor: Judith Piedra
 * Fecha: 10/11/2025
 * Descripción: Esta clase denominada ProductoServiceImplement,
 * implementa  la interfaz de ProductoService
 *
 */
// Importa la clase Producto del paquete models
import com.judith.aplicacionweb.manejosesiones.models.Producto;
// Importa la clase Arrays de java.util para crear listas rápidamente
import java.util.Arrays;
// Importa la interfaz List de java.util
import java.util.List;

// Define la clase pública ProductoServiceImplement que implementa la interfaz ProductoService
public class ProductoServiceImplement implements ProductoService {
    // Indica que el siguiente métodos sobrescribe un métodos de la interfaz ProductoService
    @Override
    // Define la implementación del métodos listar()
    public List<Producto> listar() {
        // Retorna una lista inmutable creada a partir de los elementos proporcionados
        return Arrays.asList(
                // Crea e inicializa un nuevo objeto Producto con ID 1
                new Producto(1L, "Laptop", "computación", 250.25),
                // Crea e inicializa un nuevo objeto Producto con ID 2
                new Producto(2L, "Refrigeradora", "cocina", 745.13),
                // Crea e inicializa un nuevo objeto Producto con ID 3
                new Producto(3L, "Cama", "dormitorio", 350.12));
    }
}