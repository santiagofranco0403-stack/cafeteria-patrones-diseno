package com.cafeteria.decorador;

import com.cafeteria.modelo.Categoria;
import com.cafeteria.modelo.Producto;

/**
 * Patrón Decorator (estructural) - rol: Decorador abstracto.
 * Envuelve un Producto y le suma precio y texto sin modificar su clase.
 * Las adiciones se pueden apilar: new Crema(new ShotExtra(cafe)).
 */
public abstract class AdicionDecorator implements Producto {

    protected final Producto producto;

    protected AdicionDecorator(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("La adición necesita un producto");
        }
        this.producto = producto;
    }

    protected abstract String nombreAdicion();

    protected abstract int precioAdicion();

    @Override
    public String getDescripcion() {
        return producto.getDescripcion() + " + " + nombreAdicion();
    }

    @Override
    public int getPrecio() {
        return producto.getPrecio() + precioAdicion();
    }

    @Override
    public Categoria getCategoria() {
        return producto.getCategoria();
    }

    @Override
    public String toString() {
        return getDescripcion() + " - $" + getPrecio();
    }
}
