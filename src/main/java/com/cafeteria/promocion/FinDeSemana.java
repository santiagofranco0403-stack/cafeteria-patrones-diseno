package com.cafeteria.promocion;

import com.cafeteria.pedido.Pedido;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/** Estrategia concreta: sábados y domingos, 10% del total en pedidos desde $30.000. */
public class FinDeSemana implements Promocion {

    private static final int MINIMO = 30000;

    @Override
    public String getNombre() {
        return "Fin de semana (10% desde $30.000)";
    }

    @Override
    public boolean aplicaEn(LocalDateTime momento) {
        DayOfWeek dia = momento.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }

    @Override
    public int calcularDescuento(Pedido pedido) {
        int subtotal = pedido.getSubtotal();
        return subtotal >= MINIMO ? subtotal / 10 : 0;
    }
}
