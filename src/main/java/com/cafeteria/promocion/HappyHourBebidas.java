package com.cafeteria.promocion;

import com.cafeteria.modelo.Categoria;
import com.cafeteria.pedido.Pedido;

import java.time.LocalDateTime;
import java.time.LocalTime;

/** Estrategia concreta: 20% en bebidas de 3:00 p. m. a 5:00 p. m. */
public class HappyHourBebidas implements Promocion {

    private static final LocalTime INICIO = LocalTime.of(15, 0);
    private static final LocalTime FIN = LocalTime.of(17, 0);
    private static final int PORCENTAJE = 20;

    @Override
    public String getNombre() {
        return "Happy hour (20% en bebidas, 3-5 p. m.)";
    }

    @Override
    public boolean aplicaEn(LocalDateTime momento) {
        LocalTime hora = momento.toLocalTime();
        return !hora.isBefore(INICIO) && hora.isBefore(FIN);
    }

    @Override
    public int calcularDescuento(Pedido pedido) {
        int bebidas = pedido.getItems().stream()
                .filter(p -> p.getCategoria() == Categoria.BEBIDA)
                .mapToInt(p -> p.getPrecio())
                .sum();
        return bebidas * PORCENTAJE / 100;
    }
}
