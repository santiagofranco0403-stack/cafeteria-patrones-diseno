package com.cafeteria.promocion;

import com.cafeteria.modelo.Categoria;
import com.cafeteria.pedido.Pedido;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/** Estrategia concreta: los martes, postres a mitad de precio. */
public class MartesDePostres implements Promocion {

    @Override
    public String getNombre() {
        return "Martes de postres (50% en postres)";
    }

    @Override
    public boolean aplicaEn(LocalDateTime momento) {
        return momento.getDayOfWeek() == DayOfWeek.TUESDAY;
    }

    @Override
    public int calcularDescuento(Pedido pedido) {
        int postres = pedido.getItems().stream()
                .filter(p -> p.getCategoria() == Categoria.POSTRE)
                .mapToInt(p -> p.getPrecio())
                .sum();
        return postres / 2;
    }
}
