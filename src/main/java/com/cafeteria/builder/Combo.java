package com.cafeteria.builder;

import com.cafeteria.modelo.Categoria;
import com.cafeteria.modelo.Producto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Patrón Builder (creacional) - rol: Producto construido.
 * Solo se crea a través de ComboBuilder; es inmutable una vez armado.
 */
public final class Combo implements Producto {

    private final String nombre;
    private final List<Producto> componentes;
    private final int porcentajeDescuento;

    Combo(String nombre, List<Producto> componentes, int porcentajeDescuento) {
        this.nombre = nombre;
        this.componentes = List.copyOf(componentes);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public List<Producto> getComponentes() {
        return Collections.unmodifiableList(componentes);
    }

    public int getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public int getPrecioSinDescuento() {
        return componentes.stream().mapToInt(Producto::getPrecio).sum();
    }

    @Override
    public int getPrecio() {
        int bruto = getPrecioSinDescuento();
        return bruto - (bruto * porcentajeDescuento / 100);
    }

    @Override
    public String getDescripcion() {
        String detalle = componentes.stream()
                .map(Producto::getDescripcion)
                .collect(Collectors.joining(", "));
        return "Combo " + nombre + " [" + detalle + "]"
                + (porcentajeDescuento > 0 ? " -" + porcentajeDescuento + "%" : "");
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.COMBO;
    }

    @Override
    public String toString() {
        return getDescripcion() + " - $" + getPrecio();
    }
}
