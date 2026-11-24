package com.cafeteria.builder;

import com.cafeteria.fabrica.MenuConfigurable;
import com.cafeteria.modelo.Producto;

/**
 * Patrón Builder - rol: Director.
 * Conoce las recetas de los combos fijos de la carta y las arma con el ComboBuilder.
 */
public class DirectorCombos {

    private final MenuConfigurable menu;

    public DirectorCombos(MenuConfigurable menu) {
        this.menu = menu;
    }

    public Combo comboDesayuno() {
        return new ComboBuilder()
                .nombre("Desayuno")
                .platoPrincipal(producto("P02"))
                .bebida(producto("B01"))
                .descuento(10)
                .build();
    }

    public Combo comboAlmuerzo() {
        return new ComboBuilder()
                .nombre("Almuerzo")
                .platoPrincipal(producto("P03"))
                .bebida(producto("B04"))
                .postre(producto("D01"))
                .descuento(15)
                .build();
    }

    private Producto producto(String codigo) {
        return menu.buscar(codigo)
                .orElseThrow(() -> new IllegalStateException("El menú no tiene el producto " + codigo));
    }
}
