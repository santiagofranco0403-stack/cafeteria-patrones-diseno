package com.cafeteria.decorador;

import com.cafeteria.modelo.Categoria;
import com.cafeteria.modelo.Producto;

import java.util.function.Function;

/**
 * Catálogo de adiciones disponibles para que la consola y la GUI
 * las ofrezcan sin conocer las clases decoradoras concretas.
 */
public enum TipoAdicion {
    SHOT_EXTRA("Shot extra", Categoria.BEBIDA, ShotExtra::new),
    LECHE_ALMENDRAS("Leche de almendras", Categoria.BEBIDA, LecheAlmendras::new),
    CREMA("Crema batida", Categoria.BEBIDA, Crema::new),
    QUESO_EXTRA("Queso extra", Categoria.PLATO, QuesoExtra::new),
    TOCINETA_EXTRA("Tocineta extra", Categoria.PLATO, TocinetaExtra::new);

    private final String etiqueta;
    private final Categoria aplicaA;
    private final Function<Producto, Producto> decorar;

    TipoAdicion(String etiqueta, Categoria aplicaA, Function<Producto, Producto> decorar) {
        this.etiqueta = etiqueta;
        this.aplicaA = aplicaA;
        this.decorar = decorar;
    }

    public boolean aplicaA(Producto producto) {
        return producto.getCategoria() == aplicaA;
    }

    public Producto aplicar(Producto producto) {
        if (!aplicaA(producto)) {
            throw new IllegalArgumentException(etiqueta + " no aplica a " + producto.getDescripcion());
        }
        return decorar.apply(producto);
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
