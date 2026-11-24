package com.cafeteria.decorador;

import com.cafeteria.modelo.Producto;

/** Decorador concreto: shot extra de espresso. */
public class ShotExtra extends AdicionDecorator {

    public ShotExtra(Producto producto) {
        super(producto);
    }

    @Override
    protected String nombreAdicion() {
        return "shot extra";
    }

    @Override
    protected int precioAdicion() {
        return 2500;
    }
}
