package com.cafeteria.promocion;

import com.cafeteria.pedido.Pedido;

import java.time.LocalDateTime;

/** Estrategia concreta nula: siempre aplica y no descuenta nada. */
public class SinPromocion implements Promocion {

    @Override
    public String getNombre() {
        return "Ninguna";
    }

    @Override
    public boolean aplicaEn(LocalDateTime momento) {
        return true;
    }

    @Override
    public int calcularDescuento(Pedido pedido) {
        return 0;
    }
}
