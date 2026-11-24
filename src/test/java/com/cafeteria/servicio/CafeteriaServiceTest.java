package com.cafeteria.servicio;

import com.cafeteria.decorador.ShotExtra;
import com.cafeteria.fabrica.MenuConfigurable;
import com.cafeteria.modelo.Categoria;
import com.cafeteria.pedido.EstadoPedido;
import com.cafeteria.pedido.Pedido;
import com.cafeteria.reporte.ReporteVentas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Prueba de integración del flujo principal: pedido -> cocina -> mesero -> caja -> cierre. */
class CafeteriaServiceTest {

    private CafeteriaService servicio;

    @BeforeEach
    void preparar() {
        ZoneId zona = ZoneId.of("America/Bogota");
        // jueves 26/11/2026 a las 10 a. m.: no hay promociones vigentes
        Clock reloj = Clock.fixed(LocalDateTime.of(2026, 11, 26, 10, 0).atZone(zona).toInstant(), zona);
        servicio = new CafeteriaService(MenuConfigurable.desdeRecurso("/menu.txt"), reloj);
    }

    @Test
    void flujoCompletoDeUnPedido() {
        Pedido pedido = servicio.nuevoPedido(3);
        servicio.agregarProducto(pedido.getNumero(), new ShotExtra(servicio.getMenu().buscar("B02").orElseThrow()));
        servicio.agregarProducto(pedido.getNumero(), servicio.getDirector().comboDesayuno());

        servicio.cambiarEstado(pedido.getNumero(), EstadoPedido.EN_PREPARACION);
        servicio.cambiarEstado(pedido.getNumero(), EstadoPedido.LISTO);
        servicio.cambiarEstado(pedido.getNumero(), EstadoPedido.ENTREGADO);
        servicio.cobrar(pedido.getNumero());

        assertEquals(EstadoPedido.PAGADO, pedido.getEstado());
        assertEquals(1, servicio.getCocina().getAvisos().size());
        assertEquals(1, servicio.getMesero().getAvisos().size());

        ReporteVentas reporte = servicio.cierreDeCaja();
        assertEquals(1, reporte.getCantidadPedidos());
        // capuchino 6500 + shot 2500 = 9000; desayuno (9000 + 4500) -10% = 12150
        assertEquals(21150, reporte.getTotalNeto());
        assertEquals(9000, (int) reporte.getVentasPorCategoria().get(Categoria.BEBIDA));
        assertEquals(0, servicio.getCaja().getVentas().size());
    }

    @Test
    void noSeCobraUnPedidoSinEntregar() {
        Pedido pedido = servicio.nuevoPedido(1);
        servicio.agregarProducto(pedido.getNumero(), servicio.getMenu().buscar("B01").orElseThrow());
        assertThrows(IllegalStateException.class, () -> servicio.cobrar(pedido.getNumero()));
    }

    @Test
    void pedidoInexistente() {
        assertThrows(IllegalArgumentException.class, () -> servicio.buscarPedido(99));
    }
}
