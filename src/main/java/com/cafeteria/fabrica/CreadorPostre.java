package com.cafeteria.fabrica;

import com.cafeteria.modelo.Postre;
import com.cafeteria.modelo.ProductoBase;

/** Creador concreto: el dato extra indica "sin_azucar" o "normal". */
public class CreadorPostre extends CreadorProducto {

    @Override
    protected ProductoBase crearProducto(ItemMenu item) {
        boolean sinAzucar = item.extra().equalsIgnoreCase("sin_azucar");
        return new Postre(item.codigo(), item.nombre(), item.precio(), sinAzucar);
    }
}
