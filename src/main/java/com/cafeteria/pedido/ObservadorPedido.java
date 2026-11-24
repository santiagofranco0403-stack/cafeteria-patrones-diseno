package com.cafeteria.pedido;

/**
 * Patrón Observer (comportamiento) - rol: Observador.
 * Cocina, caja y mesero implementan esta interfaz para enterarse
 * de cada cambio de estado de un pedido.
 */
public interface ObservadorPedido {

    void estadoCambiado(Pedido pedido, EstadoPedido anterior);
}
