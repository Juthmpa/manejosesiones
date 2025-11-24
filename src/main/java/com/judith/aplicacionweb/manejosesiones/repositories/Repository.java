package com.judith.aplicacionweb.manejosesiones.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional; // ¡Importante!

/**
 * Interfaz genérica que define el contrato de operaciones básicas (CRUD)
 * y de gestión de estado para cualquier entidad que interactúe con la base de datos.
 *
 * @param <T> El tipo de la entidad (e.g., Producto, Categoria).
 */
public interface Repository <T>{

    List<T> listar() throws SQLException;

    /**
     * La clase define un método para buscar un elemento por su ID.
     *
     * @param id El ID del elemento a buscar.
     * @return Un Optional que contiene el objeto de tipo T si se encuentra, o Optional.empty() si no.
     * @throws SQLException Si ocurre un error al acceder a la BBDD.
     */
    // Debe retornar Optional<T>
    Optional<T> porId(Long id) throws SQLException;

    void guardar(T t) throws SQLException;

    void eliminar(Long id) throws SQLException;

    void desactivar(Long id) throws SQLException;

    void activar(Long id) throws SQLException;
}
