package com.cafeteria.observador;

import com.cafeteria.pedido.EstadoPedido;
import com.cafeteria.pedido.ObservadorPedido;
import com.cafeteria.pedido.Pedido;

/** Observador concreto: el mesero se entera cuando un pedido está listo para llevar a la mesa. */
public class Mesero extends ObservadorConRegistro implements ObservadorPedido {

    @Override
    public void estadoCambiado(Pedido pedido, EstadoPedido anterior) {
        if (pedido.getEstado() == EstadoPedido.LISTO) {
            registrar("MESERO: llevar pedido #" + pedido.getNumero() + " a la mesa " + pedido.getMesa());
        } else if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            registrar("MESERO: avisar a la mesa " + pedido.getMesa() + " que el pedido #"
                    + pedido.getNumero() + " fue cancelado");
        }
    }
}
