package com.cafeteria.fabrica;

import com.cafeteria.modelo.Bebida;
import com.cafeteria.modelo.ProductoBase;

/** Creador concreto: el dato extra indica "caliente" o "fria". */
public class CreadorBebida extends CreadorProducto {

    @Override
    protected ProductoBase crearProducto(ItemMenu item) {
        boolean caliente = item.extra().equalsIgnoreCase("caliente");
        return new Bebida(item.codigo(), item.nombre(), item.precio(), caliente);
    }
}
