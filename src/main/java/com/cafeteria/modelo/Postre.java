package com.cafeteria.modelo;

public class Postre extends ProductoBase {

    private final boolean sinAzucar;

    public Postre(String codigo, String nombre, int precio, boolean sinAzucar) {
        super(codigo, nombre, precio);
        this.sinAzucar = sinAzucar;
    }

    public boolean isSinAzucar() {
        return sinAzucar;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.POSTRE;
    }

    @Override
    public String getDescripcion() {
        return getNombre() + (sinAzucar ? " (sin azúcar)" : "");
    }
}
