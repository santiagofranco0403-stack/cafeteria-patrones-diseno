package com.cafeteria.modelo;

public class Bebida extends ProductoBase {

    private final boolean caliente;

    public Bebida(String codigo, String nombre, int precio, boolean caliente) {
        super(codigo, nombre, precio);
        this.caliente = caliente;
    }

    public boolean isCaliente() {
        return caliente;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.BEBIDA;
    }

    @Override
    public String getDescripcion() {
        return getNombre() + (caliente ? " (caliente)" : " (fría)");
    }
}
