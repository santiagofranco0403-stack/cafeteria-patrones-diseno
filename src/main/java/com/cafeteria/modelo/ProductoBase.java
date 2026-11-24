package com.cafeteria.modelo;

/**
 * Producto del menú sin adiciones. Las subclases agregan los atributos propios
 * de cada tipo (bebida, plato, postre).
 */
public abstract class ProductoBase implements Producto {

    private final String codigo;
    private final String nombre;
    private final int precio;

    protected ProductoBase(String codigo, String nombre, int precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public int getPrecio() {
        return precio;
    }

    @Override
    public String getDescripcion() {
        return nombre;
    }

    @Override
    public String toString() {
        return getDescripcion() + " - $" + getPrecio();
    }
}
