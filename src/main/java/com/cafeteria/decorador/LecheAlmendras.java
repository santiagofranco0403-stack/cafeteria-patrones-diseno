package com.cafeteria.decorador;

import com.cafeteria.modelo.Producto;

/** Decorador concreto: cambio a leche de almendras. */
public class LecheAlmendras extends AdicionDecorator {

    public LecheAlmendras(Producto producto) {
        super(producto);
    }

    @Override
    protected String nombreAdicion() {
        return "leche de almendras";
    }

    @Override
    protected int precioAdicion() {
        return 3000;
    }
}
