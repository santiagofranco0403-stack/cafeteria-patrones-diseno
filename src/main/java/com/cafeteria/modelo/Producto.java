package com.cafeteria.modelo;

/**
 * Componente común de todo lo que se puede vender en la cafetería.
 * Lo implementan los productos base, las adiciones (Decorator) y los combos (Builder).
 */
public interface Producto {

    String getDescripcion();

    /** Precio en pesos colombianos. */
    int getPrecio();

    Categoria getCategoria();
}
