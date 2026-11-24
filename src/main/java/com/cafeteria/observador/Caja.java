package com.cafeteria.observador;

import com.cafeteria.pedido.EstadoPedido;
import com.cafeteria.pedido.ObservadorPedido;
import com.cafeteria.pedido.Pedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Observador concreto: la caja prepara el cobro cuando se entrega el pedido
 * y guarda las ventas pagadas para el cierre de caja.
 */
public class Caja extends ObservadorConRegistro implements ObservadorPedido {

    private final List<Pedido> ventas = new ArrayList<>();

    @Override
    public void estadoCambiado(Pedido pedido, EstadoPedido anterior) {
        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            registrar("CAJA: preparar cuenta del pedido #" + pedido.getNumero() + " por $" + pedido.getTotal());
        } else if (pedido.getEstado() == EstadoPedido.PAGADO) {
            ventas.add(pedido);
            registrar("CAJA: pago recibido del pedido #" + pedido.getNumero() + " por $" + pedido.getTotal());
        }
    }

    public List<Pedido> getVentas() {
        return Collections.unmodifiableList(ventas);
    }

    public void limpiarVentas() {
        ventas.clear();
    }
}
