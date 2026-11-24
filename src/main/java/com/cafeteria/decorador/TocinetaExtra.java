package com.cafeteria.decorador;

import com.cafeteria.modelo.Producto;

/** Decorador concreto: tocineta extra para platos. */
public class TocinetaExtra extends AdicionDecorator {

    public TocinetaExtra(Producto producto) {
        super(producto);
    }

    @Override
    protected String nombreAdicion() {
        return "tocineta extra";
    }

    @Override
    protected int precioAdicion() {
        return 3500;
    }
}
