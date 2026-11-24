package com.cafeteria.fabrica;

import com.cafeteria.modelo.ProductoBase;

/**
 * Patrón Factory Method (creacional) - rol: Creador.
 * Define el paso común (validar el ítem del menú) y deja a cada subclase
 * decidir qué tipo concreto de producto se instancia en crearProducto().
 */
public abstract class CreadorProducto {

    public final ProductoBase crear(ItemMenu item) {
        if (item.nombre().isBlank()) {
            throw new IllegalArgumentException("El producto " + item.codigo() + " no tiene nombre");
        }
        if (item.precio() <= 0) {
            throw new IllegalArgumentException("El producto " + item.codigo() + " debe tener precio positivo");
        }
        return crearProducto(item);
    }

    /** Factory method: cada creador concreto devuelve su propio tipo de producto. */
    protected abstract ProductoBase crearProducto(ItemMenu item);
}
