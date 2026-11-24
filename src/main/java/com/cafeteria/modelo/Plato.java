package com.cafeteria.modelo;

public class Plato extends ProductoBase {

    private final int minutosPreparacion;

    public Plato(String codigo, String nombre, int precio, int minutosPreparacion) {
        super(codigo, nombre, precio);
        this.minutosPreparacion = minutosPreparacion;
    }

    public int getMinutosPreparacion() {
        return minutosPreparacion;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.PLATO;
    }
}
