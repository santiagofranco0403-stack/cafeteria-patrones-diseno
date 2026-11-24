package com.cafeteria.pedido;

import java.util.EnumSet;
import java.util.Set;

/** Estados de un pedido y las transiciones permitidas entre ellos. */
public enum EstadoPedido {
    RECIBIDO,
    EN_PREPARACION,
    LISTO,
    ENTREGADO,
    PAGADO,
    CANCELADO;

    public Set<EstadoPedido> siguientesPermitidos() {
        return switch (this) {
            case RECIBIDO -> EnumSet.of(EN_PREPARACION, CANCELADO);
            case EN_PREPARACION -> EnumSet.of(LISTO, CANCELADO);
            case LISTO -> EnumSet.of(ENTREGADO);
            case ENTREGADO -> EnumSet.of(PAGADO);
            case PAGADO, CANCELADO -> EnumSet.noneOf(EstadoPedido.class);
        };
    }

    public boolean puedePasarA(EstadoPedido nuevo) {
        return siguientesPermitidos().contains(nuevo);
    }
}
