package com.cafeteria.decorador;

import com.cafeteria.modelo.Producto;

/** Decorador concreto: crema batida. */
public class Crema extends AdicionDecorator {

    public Crema(Producto producto) {
        super(producto);
    }

    @Override
    protected String nombreAdicion() {
        return "crema batida";
    }

    @Override
    protected int precioAdicion() {
        return 1500;
    }
}
