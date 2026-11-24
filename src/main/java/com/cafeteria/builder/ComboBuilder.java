package com.cafeteria.builder;

import com.cafeteria.modelo.Categoria;
import com.cafeteria.modelo.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Builder (creacional) - rol: Constructor.
 * Arma un combo paso a paso. Las partes opcionales se pueden omitir y
 * las reglas (plato y bebida obligatorios, descuento máximo) se validan en build().
 */
public class ComboBuilder {

    public static final int DESCUENTO_MAXIMO = 30;

    private String nombre = "Personalizado";
    private Producto platoPrincipal;
    private Producto bebida;
    private final List<Producto> acompanamientos = new ArrayList<>();
    private Producto postre;
    private int porcentajeDescuento;

    public ComboBuilder nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ComboBuilder platoPrincipal(Producto plato) {
        exigirCategoria(plato, Categoria.PLATO);
        this.platoPrincipal = plato;
        return this;
    }

    public ComboBuilder bebida(Producto bebida) {
        exigirCategoria(bebida, Categoria.BEBIDA);
        this.bebida = bebida;
        return this;
    }

    public ComboBuilder acompanamiento(Producto acompanamiento) {
        this.acompanamientos.add(acompanamiento);
        return this;
    }

    public ComboBuilder postre(Producto postre) {
        exigirCategoria(postre, Categoria.POSTRE);
        this.postre = postre;
        return this;
    }

    public ComboBuilder descuento(int porcentaje) {
        if (porcentaje < 0 || porcentaje > DESCUENTO_MAXIMO) {
            throw new IllegalArgumentException("El descuento de un combo debe estar entre 0 y " + DESCUENTO_MAXIMO);
        }
        this.porcentajeDescuento = porcentaje;
        return this;
    }

    public Combo build() {
        if (platoPrincipal == null) {
            throw new IllegalStateException("Un combo necesita plato principal");
        }
        if (bebida == null) {
            throw new IllegalStateException("Un combo necesita bebida");
        }
        List<Producto> componentes = new ArrayList<>();
        componentes.add(platoPrincipal);
        componentes.add(bebida);
        componentes.addAll(acompanamientos);
        if (postre != null) {
            componentes.add(postre);
        }
        return new Combo(nombre, componentes, porcentajeDescuento);
    }

    private static void exigirCategoria(Producto producto, Categoria esperada) {
        if (producto == null || producto.getCategoria() != esperada) {
            throw new IllegalArgumentException("Se esperaba un producto de tipo " + esperada);
        }
    }
}
