package com.judith.aplicacionweb.manejosesiones.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Autor: Judith Piedra
 * Fecha: 14/11/2025
 * Descripción: Modelo que representa el carrito de compras completo,
 * conteniendo una lista de ItemCarro y la lógica para gestionar los totales.
 */
public class DetalleCarro {

    // Lista de items en el carrito
    private final List<ItemCarro> items;

    // Tasa de IVA fija para toda la compra
    private static final double IVA_RATE = 0.15;

    public DetalleCarro() {
        this.items = new ArrayList<>();
    }

    public List<ItemCarro> getItems() {
        return items;
    }

    // --- Métodos de Gestión del Carrito ---

    /*
     * Agrega un item al carrito. Si el producto ya existe, incrementa la cantidad.
     * Esta es la lógica que utiliza AgregarCarroServlet.
     */
    public void addItemCarro(ItemCarro nuevoItem) {
        // Busca si el producto ya existe en la lista usando el método correcto getIdProducto()
        Optional<ItemCarro> existingItem = items.stream()
                .filter(i -> i.getProducto().getIdProducto().equals(nuevoItem.getProducto().getIdProducto()))
                .findAny();

        if (existingItem.isPresent()) {
            // Si ya existe, incrementa la cantidad
            existingItem.get().incrementarCantidad();
        } else {
            // Si es un producto nuevo, lo agrega a la lista
            this.items.add(nuevoItem);
        }
    }

    /*
     * Método para eliminar una unidad de un producto.
     * Si la cantidad llega a cero, elimina el item de la lista.
     */
    public void removeOneItem(Long idProducto) {
        // Busca el item usando el método correcto getIdProducto()
        Optional<ItemCarro> existingItem = items.stream()
                .filter(i -> i.getProducto().getIdProducto().equals(idProducto))
                .findAny();

        if (existingItem.isPresent()) {
            ItemCarro item = existingItem.get();
            item.decrementarCantidad(); // Decrementa la unidad

            // Si la cantidad llega a cero, lo elimina completamente de la lista
            if (item.getCantidad() <= 0) {
                this.items.remove(item);
            }
        }
    }

    /*
     * Elimina completamente el item del carrito (quita todas las unidades).
     */
    public void removeItem(Long idProducto) {
        items.removeIf(i -> i.getProducto().getIdProducto().equals(idProducto));
    }


    // --- Métodos de Cálculo (Evitando el error 'effectively final') ---

    /*
     * Calcula el subtotal neto de toda la compra (suma de subtotales de items).
     * Usa mapToDouble().sum() para evitar modificar una variable externa.
     */
    public double getSubtotalPuro() {
        // Se utiliza ItemCarro::getSubtotal como referencia de método
        return items.stream()
                .mapToDouble(ItemCarro::getSubtotal)
                .sum();
    }

    /*
     * Calcula el monto total de IVA.
     */
    public double getIva() {
        return getSubtotalPuro() * IVA_RATE;
    }

    /*
     * Calcula el monto total a pagar (Neto + IVA).
     */
    public double getTotal() {
        // Podría ser getSubtotalPuro() + getIva(), pero esta es más robusta
        return items.stream()
                .mapToDouble(ItemCarro::getTotal)
                .sum();
    }

    // Getter para la tasa de IVA (útil para la JSP)
    public double getIvaRate() {
        return IVA_RATE;
    }
}