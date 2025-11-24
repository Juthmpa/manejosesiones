package com.judith.aplicacionweb.manejosesiones.models;

import java.util.Objects;

/**
 * Clase de modelo que representa una categoría de producto en la base de datos.
 */
public class Categoria {
    // Campo que almacena el identificador único de la categoría.
    private Long id;

    // Campo que almacena el nombre descriptivo de la categoría.
    private String nombreCategoria;

    // Campo que almacena la descripción detallada de la categoría.
    private String descripcion;

    // Campo que indica el estado lógico de la categoría (1: activo, 0: inactivo).
    private Integer condicion;

    /**
     * Constructor vacío por defecto.
     */
    public Categoria() {
    }

    /**
     * Constructor completo que inicializa todos los campos.
     * @param id Identificador de la categoría.
     * @param nombreCategoria Nombre de la categoría.
     */
    public Categoria(Long id, String nombreCategoria) {
        this.id = id;
        this.nombreCategoria = nombreCategoria;
    }

    // Métodos Getter y Setter

    /**
     * Obtiene el identificador de la categoría.
     * @return El ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la categoría.
     * @param id El nuevo ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la categoría.
     * @return El nombre.
     */
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    /**
     * Establece el nombre de la categoría.
     * @param nombreCategoria El nuevo nombre.
     */
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    /**
     * Obtiene la descripción de la categoría.
     * @return La descripción.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la categoría.
     * @param descripcion La nueva descripción.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la condición (estado) de la categoría (1 o 0).
     * @return La condición.
     */
    public Integer getCondicion() {
        return condicion;
    }

    /**
     * Establece la condición (estado) de la categoría.
     * @param condicion La nueva condición.
     */
    public void setCondicion(Integer condicion) {
        this.condicion = condicion;
    }

    /**
     * Implementación del método equals. La clase determina que dos categorías
     * son iguales si tienen el mismo ID.
     * @param o El objeto a comparar.
     * @return true si son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        // La clase compara si los IDs son iguales, ignorando el resto de campos.
        return Objects.equals(id, categoria.id);
    }
}