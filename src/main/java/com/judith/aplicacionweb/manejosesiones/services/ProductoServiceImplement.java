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
import java.util.Optional;

// Define la clase pública ProductoServiceImplement que implementa la interfaz ProductoService
public class ProductoServiceImplement implements ProductoService {

    // Indica que el siguiente métodos sobrescribe un métodos de la interfaz ProductoService
    @Override
    // Define la única implementación válida del método listar()
    public List<Producto> listar() {
        // Retorna una lista inmutable creada a partir de los elementos proporcionados.
        // NOTA: Se ha añadido el valor de STOCK (quinto argumento) para que coincida con el constructor de Producto.java
        return Arrays.asList(
                // Producto(ID, Nombre, Tipo, Precio, Stock)
                new Producto(1L, "Laptop", "computación", 250.25, 5),
                // Producto(ID, Nombre, Tipo, Precio, Stock)
                new Producto(2L, "Refrigeradora", "cocina", 745.13, 10),
                // Producto(ID, Nombre, Tipo, Precio, Stock)
                new Producto(3L, "Cama", "dormitorio", 350.12, 8));
    }

    // Implementación del métodos porId(Long id)
    @Override
    public Optional<Producto> porId(Long id) {
        // Utiliza Streams para filtrar la lista por ID y encontrar el primero
        return listar().stream()
                .filter(p -> p.getIdProducto().equals(id)) // Filtra por el ID
                .findAny(); // Intenta encontrar cualquier elemento que cumpla la condición
    }
}