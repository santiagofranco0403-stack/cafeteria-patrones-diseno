package com.cafeteria.servicio;

import com.cafeteria.builder.DirectorCombos;
import com.cafeteria.fabrica.MenuConfigurable;
import com.cafeteria.observador.Caja;
import com.cafeteria.observador.Cocina;
import com.cafeteria.observador.Mesero;
import com.cafeteria.pedido.EstadoPedido;
import com.cafeteria.pedido.Pedido;
import com.cafeteria.modelo.Producto;
import com.cafeteria.promocion.FinDeSemana;
import com.cafeteria.promocion.HappyHourBebidas;
import com.cafeteria.promocion.MartesDePostres;
import com.cafeteria.promocion.Promocion;
import com.cafeteria.promocion.SelectorPromociones;
import com.cafeteria.reporte.ReporteVentas;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Núcleo de la aplicación. La consola y la interfaz gráfica solo hablan con esta
 * clase, así que las dos comparten exactamente la misma lógica de negocio.
 */
public class CafeteriaService {

    private final MenuConfigurable menu;
    private final DirectorCombos director;
    private final SelectorPromociones promociones = new SelectorPromociones();
    private final Cocina cocina = new Cocina();
    private final Caja caja = new Caja();
    private final Mesero mesero = new Mesero();
    private final Map<Integer, Pedido> pedidos = new LinkedHashMap<>();
    private final Clock reloj;
    private int siguienteNumero = 1;

    public CafeteriaService(MenuConfigurable menu, Clock reloj) {
        this.menu = menu;
        this.reloj = reloj;
        this.director = new DirectorCombos(menu);
        promociones.registrar(new HappyHourBebidas());
        promociones.registrar(new MartesDePostres());
        promociones.registrar(new FinDeSemana());
    }

    /** Servicio con el menú por defecto (menu.txt) y la hora del sistema. */
    public static CafeteriaService porDefecto() {
        return new CafeteriaService(MenuConfigurable.desdeRecurso("/menu.txt"), Clock.systemDefaultZone());
    }

    public Pedido nuevoPedido(int mesa) {
        if (mesa <= 0) {
            throw new IllegalArgumentException("El número de mesa debe ser positivo");
        }
        Pedido pedido = new Pedido(siguienteNumero++, mesa, LocalDateTime.now(reloj));
        pedido.suscribir(cocina);
        pedido.suscribir(caja);
        pedido.suscribir(mesero);
        pedidos.put(pedido.getNumero(), pedido);
        return pedido;
    }

    public void agregarProducto(int numeroPedido, Producto producto) {
        buscarPedido(numeroPedido).agregar(producto);
    }

    public void cambiarEstado(int numeroPedido, EstadoPedido nuevo) {
        if (nuevo == EstadoPedido.PAGADO) {
            cobrar(numeroPedido);
            return;
        }
        buscarPedido(numeroPedido).cambiarEstado(nuevo);
    }

    /** Aplica la mejor promoción vigente y marca el pedido como pagado. */
    public Promocion cobrar(int numeroPedido) {
        Pedido pedido = buscarPedido(numeroPedido);
        if (pedido.getEstado() != EstadoPedido.ENTREGADO) {
            throw new IllegalStateException("Solo se cobra un pedido entregado");
        }
        Promocion aplicada = promociones.aplicar(pedido, LocalDateTime.now(reloj));
        pedido.cambiarEstado(EstadoPedido.PAGADO);
        return aplicada;
    }

    public Promocion promocionVigente(int numeroPedido) {
        return promociones.mejorPara(buscarPedido(numeroPedido), LocalDateTime.now(reloj));
    }

    public ReporteVentas cierreDeCaja() {
        ReporteVentas reporte = new ReporteVentas(new ArrayList<>(caja.getVentas()), LocalDateTime.now(reloj));
        caja.limpiarVentas();
        return reporte;
    }

    public Pedido buscarPedido(int numero) {
        Pedido pedido = pedidos.get(numero);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe el pedido #" + numero);
        }
        return pedido;
    }

    public List<Pedido> getPedidos() {
        return List.copyOf(pedidos.values());
    }

    public MenuConfigurable getMenu() {
        return menu;
    }

    public DirectorCombos getDirector() {
        return director;
    }

    public SelectorPromociones getPromociones() {
        return promociones;
    }

    public Cocina getCocina() {
        return cocina;
    }

    public Caja getCaja() {
        return caja;
    }

    public Mesero getMesero() {
        return mesero;
    }
}
