package com.judith.aplicacionweb.manejosesiones.models;

import java.util.Objects;

/*
 * Autor: Judith Piedra
 * Fecha: 14/11/2025
 * Descripción: Modelo que representa un solo item dentro del carrito de compras,
 * conteniendo la cantidad de un producto específico.
 */
public class ItemCarro {
    private int cantidad;
    private Producto producto;

    // Constante para la tasa de IVA (15% según el ejemplo en carro.jsp)
    public static final double IVA_RATE = 0.15;

    public ItemCarro() {
    }

    public ItemCarro(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    // --- Getters ---

    public int getCantidad() {
        return cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    // --- Métodos de Cálculo ---

    // Calcula el subtotal del item: Precio * Cantidad (NETO, sin IVA)
    public double getSubtotal() {
        return this.cantidad * this.producto.getPrecio();
    }

    // Calcula el IVA para este item
    public double getIva() {
        return getSubtotal() * IVA_RATE;
    }

    // Calcula el total del item (NETO + IVA)
    public double getTotal() {
        return getSubtotal() * (1 + IVA_RATE);
    }

    // --- Setters ---

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // Métodos para incrementar la cantidad
    public void incrementarCantidad() {
        this.cantidad++;
    }

    // Métodos para decrementar la cantidad
    public void decrementarCantidad() {
        this.cantidad--;
    }

    // Implementación de equals para asegurar que los items son únicos por producto.
    // Usamos getIdProducto() para coincidir con Producto.java.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCarro itemCarro = (ItemCarro) o;
        // La igualdad se basa en el ID del producto
        return Objects.equals(producto.getIdProducto(), itemCarro.producto.getIdProducto());
    }

    @Override
    public int hashCode() {
        // El hashcode se basa en el ID del producto
        return Objects.hash(producto.getIdProducto());
    }
}