package com.cafeteria.promocion;

import com.cafeteria.modelo.Bebida;
import com.cafeteria.modelo.Plato;
import com.cafeteria.modelo.Postre;
import com.cafeteria.pedido.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromocionStrategyTest {

    // 24/11/2026 es martes; 28/11/2026 es sábado
    private static final LocalDateTime MARTES_MANANA = LocalDateTime.of(2026, 11, 24, 10, 0);
    private static final LocalDateTime MARTES_TARDE = LocalDateTime.of(2026, 11, 24, 16, 0);
    private static final LocalDateTime SABADO = LocalDateTime.of(2026, 11, 28, 12, 0);
    private static final LocalDateTime JUEVES = LocalDateTime.of(2026, 11, 26, 12, 0);

    private Pedido pedido;
    private SelectorPromociones selector;

    @BeforeEach
    void preparar() {
        pedido = new Pedido(1, 1, MARTES_MANANA);
        pedido.agregar(new Bebida("B02", "Capuchino", 10000, true));
        pedido.agregar(new Plato("P03", "Hamburguesa", 22000, 15));
        pedido.agregar(new Postre("D02", "Cheesecake", 9000, false));
        selector = new SelectorPromociones();
        selector.registrar(new HappyHourBebidas());
        selector.registrar(new MartesDePostres());
        selector.registrar(new FinDeSemana());
    }

    @Test
    void happyHourSoloAplicaEnSuHorario() {
        Promocion happy = new HappyHourBebidas();
        assertTrue(happy.aplicaEn(MARTES_TARDE));
        assertFalse(happy.aplicaEn(MARTES_MANANA));
        assertEquals(2000, happy.calcularDescuento(pedido));
    }

    @Test
    void martesDePostresDescuentaLaMitadDeLosPostres() {
        assertEquals(4500, new MartesDePostres().calcularDescuento(pedido));
    }

    @Test
    void finDeSemanaExigeMontoMinimo() {
        assertEquals(4100, new FinDeSemana().calcularDescuento(pedido));
        Pedido pequeno = new Pedido(2, 1, SABADO);
        pequeno.agregar(new Bebida("B01", "Tinto", 4500, true));
        assertEquals(0, new FinDeSemana().calcularDescuento(pequeno));
    }

    @Test
    void elContextoEligeLaMejorPromocionVigente() {
        // martes 4 p. m.: compiten happy hour ($2.000) y martes de postres ($4.500)
        assertInstanceOf(MartesDePostres.class, selector.mejorPara(pedido, MARTES_TARDE));
        assertInstanceOf(FinDeSemana.class, selector.mejorPara(pedido, SABADO));
        assertInstanceOf(SinPromocion.class, selector.mejorPara(pedido, JUEVES));
    }

    @Test
    void aplicarRegistraElDescuentoEnElPedido() {
        selector.aplicar(pedido, SABADO);
        assertEquals(41000, pedido.getSubtotal());
        assertEquals(4100, pedido.getDescuento());
        assertEquals(36900, pedido.getTotal());
    }

    @Test
    void sePuedeAgregarUnaPromocionNuevaSinModificarElContexto() {
        selector.registrar(new Promocion() {
            public String getNombre() { return "Cliente del mes"; }
            public boolean aplicaEn(LocalDateTime momento) { return true; }
            public int calcularDescuento(Pedido p) { return 10000; }
        });
        assertEquals("Cliente del mes", selector.mejorPara(pedido, JUEVES).getNombre());
    }
}
