package com.cafeteria.decorador;

import com.cafeteria.modelo.Producto;

/** Decorador concreto: porción extra de queso para platos. */
public class QuesoExtra extends AdicionDecorator {

    public QuesoExtra(Producto producto) {
        super(producto);
    }

    @Override
    protected String nombreAdicion() {
        return "queso extra";
    }

    @Override
    protected int precioAdicion() {
        return 2000;
    }
}
