package com.cafeteria.pedido;

import com.cafeteria.modelo.Bebida;
import com.cafeteria.observador.Caja;
import com.cafeteria.observador.Cocina;
import com.cafeteria.observador.Mesero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PedidoObserverTest {

    private Pedido pedido;
    private Cocina cocina;
    private Caja caja;
    private Mesero mesero;

    @BeforeEach
    void preparar() {
        pedido = new Pedido(1, 4, LocalDateTime.of(2026, 11, 24, 10, 0));
        pedido.agregar(new Bebida("B01", "Tinto", 4500, true));
        cocina = new Cocina();
        caja = new Caja();
        mesero = new Mesero();
        pedido.suscribir(cocina);
        pedido.suscribir(caja);
        pedido.suscribir(mesero);
    }

    @Test
    void cadaAreaRecibeSoloLosAvisosQueLeInteresan() {
        pedido.cambiarEstado(EstadoPedido.EN_PREPARACION);
        pedido.cambiarEstado(EstadoPedido.LISTO);
        pedido.cambiarEstado(EstadoPedido.ENTREGADO);
        pedido.cambiarEstado(EstadoPedido.PAGADO);

        assertEquals(1, cocina.getAvisos().size());
        assertEquals(1, mesero.getAvisos().size());
        assertEquals(2, caja.getAvisos().size());
        assertTrue(mesero.getAvisos().get(0).contains("mesa 4"));
        assertEquals(1, caja.getVentas().size());
    }

    @Test
    void todosLosObservadoresSonNotificados() {
        List<EstadoPedido> recibidos = new ArrayList<>();
        pedido.suscribir((p, anterior) -> recibidos.add(p.getEstado()));
        pedido.cambiarEstado(EstadoPedido.EN_PREPARACION);
        pedido.cambiarEstado(EstadoPedido.CANCELADO);

        assertEquals(List.of(EstadoPedido.EN_PREPARACION, EstadoPedido.CANCELADO), recibidos);
        assertEquals(2, cocina.getAvisos().size());
    }

    @Test
    void unObservadorDesuscritoNoRecibeAvisos() {
        pedido.desuscribir(cocina);
        pedido.cambiarEstado(EstadoPedido.EN_PREPARACION);
        assertTrue(cocina.getAvisos().isEmpty());
    }

    @Test
    void transicionInvalidaNoNotifica() {
        assertThrows(IllegalStateException.class, () -> pedido.cambiarEstado(EstadoPedido.PAGADO));
        assertTrue(caja.getAvisos().isEmpty());
        assertEquals(EstadoPedido.RECIBIDO, pedido.getEstado());
    }

    @Test
    void noSeAgreganProductosDespuesDeEnviarACocina() {
        pedido.cambiarEstado(EstadoPedido.EN_PREPARACION);
        assertThrows(IllegalStateException.class,
                () -> pedido.agregar(new Bebida("B02", "Capuchino", 6500, true)));
    }

    @Test
    void pedidoVacioNoVaACocina() {
        Pedido vacio = new Pedido(2, 1, LocalDateTime.now());
        assertThrows(IllegalStateException.class, () -> vacio.cambiarEstado(EstadoPedido.EN_PREPARACION));
    }
}
