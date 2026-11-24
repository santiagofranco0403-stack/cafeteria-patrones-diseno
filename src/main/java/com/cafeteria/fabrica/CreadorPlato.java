package com.cafeteria.fabrica;

import com.cafeteria.modelo.Plato;
import com.cafeteria.modelo.ProductoBase;

/** Creador concreto: el dato extra son los minutos de preparación. */
public class CreadorPlato extends CreadorProducto {

    @Override
    protected ProductoBase crearProducto(ItemMenu item) {
        int minutos = Integer.parseInt(item.extra());
        return new Plato(item.codigo(), item.nombre(), item.precio(), minutos);
    }
}
