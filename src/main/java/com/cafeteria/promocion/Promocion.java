package com.cafeteria.promocion;

import com.cafeteria.pedido.Pedido;

import java.time.LocalDateTime;

/**
 * Patrón Strategy (comportamiento) - rol: Estrategia.
 * Cada promoción encapsula su propia regla de horario/día y su cálculo de descuento.
 */
public interface Promocion {

    String getNombre();

    boolean aplicaEn(LocalDateTime momento);

    /** Descuento en pesos que se le haría al pedido. */
    int calcularDescuento(Pedido pedido);
}
