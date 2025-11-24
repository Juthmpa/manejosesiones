// Define el paquete donde se encuentra esta clase
package com.judith.aplicacionweb.manejosesiones.models;
/*
 * Autor: Judith Piedra
 * Fecha: 11/11/2025
 * Descripción: Esta clase denominada Producto,
 * modela el producto que tiene sus atributos:
 * idProducto, nombre, categoria, precio
 * con su contructor sin parámetros y con parámetros
 * sus métodos Getter y Setter
 */
import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase de modelo que representa un producto en la base de datos,
 * incluyendo su categoría y fechas de control.
 */
public class Producto {
    // Campo que almacena el identificador único del producto.
    private Long id;

    // Campo que almacena el nombre del producto.
    private String nombreProducto;

    // Campo que almacena el precio de venta del producto.
    private double precio;

    // Campo que almacena el stock actual del producto.
    private int stock;

    // Campo que almacena la descripción detallada del producto.
    private String descripcion;

    // Campo que almacena la fecha de elaboración del producto.
    private LocalDate fechaElaboracion;

    // Campo que almacena la fecha de caducidad del producto.
    private LocalDate fechaCaducidad;

    // Campo que indica el estado lógico del producto (1: activo, 0: inactivo).
    private int condicion;

    // Campo que almacena la categoría a la que pertenece el producto (Relación).
    private Categoria categoria;

    // Campo que almacena el codigo
    private String codigo;

    /**
     * Constructor vacío.
     */
    public Producto() {
    }
    /**
     * Constructor con parámetros principales.
     */
    public Producto(Long id, String nombreProducto, double precio, int stock,
                    String descripcion, LocalDate fechaElaboracion,
                    LocalDate fechaCaducidad, String codigo,
                    String tipo, Integer condicion) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
        this.descripcion = descripcion;
        this.condicion = condicion;
        // Implementamos un objeto de tipo Categoria
        this.categoria = new Categoria();
        this.categoria.setNombreCategoria(tipo);
        this.fechaElaboracion = fechaElaboracion;
        this.fechaCaducidad = fechaCaducidad;
        this.codigo = codigo;
    }



    // Métodos Getter y Setter

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombreProducto() {
        return nombreProducto;
    }
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDate getFechaElaboracion() {
        return fechaElaboracion;
    }
    public void setFechaElaboracion(LocalDate fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }
    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }
    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }
    public Integer getCondicion() {return condicion;}
    public void setCondicion(Integer condicion) {this.condicion = condicion;}

    public Categoria getCategoria() {return categoria;}
    public void setCategoria(Categoria categoria) {this.categoria = categoria;}

    public String getCodigo() {return codigo; }
    public void setCodigo(String codigo) {this.codigo = codigo;}


    /**
     * Implementación del método equals. La clase determina que dos productos
     * son iguales si tienen el mismo ID, ignorando el resto de atributos.
     * @param o El objeto a comparar.
     * @return true si son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        // La clase compara si los IDs son iguales.
        return Objects.equals(id, producto.id);
    }
}