package com.cafeteria.observador;

import com.cafeteria.pedido.EstadoPedido;
import com.cafeteria.pedido.ObservadorPedido;
import com.cafeteria.pedido.Pedido;

/** Observador concreto: la cocina reacciona cuando hay que preparar o se cancela un pedido. */
public class Cocina extends ObservadorConRegistro implements ObservadorPedido {

    @Override
    public void estadoCambiado(Pedido pedido, EstadoPedido anterior) {
        switch (pedido.getEstado()) {
            case EN_PREPARACION -> registrar("COCINA: preparar pedido #" + pedido.getNumero()
                    + " (" + pedido.getItems().size() + " productos)");
            case CANCELADO -> registrar("COCINA: detener pedido #" + pedido.getNumero());
            default -> { /* a la cocina no le interesan los demás estados */ }
        }
    }
}
