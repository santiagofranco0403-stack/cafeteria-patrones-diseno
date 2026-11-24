package com.cafeteria.ui.consola;

import com.cafeteria.builder.ComboBuilder;
import com.cafeteria.decorador.TipoAdicion;
import com.cafeteria.modelo.Categoria;
import com.cafeteria.modelo.Producto;
import com.cafeteria.modelo.ProductoBase;
import com.cafeteria.pedido.EstadoPedido;
import com.cafeteria.pedido.Pedido;
import com.cafeteria.promocion.Promocion;
import com.cafeteria.servicio.CafeteriaService;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/** Interfaz por consola. Toda la lógica la delega en CafeteriaService. */
public class AppConsola {

    private final CafeteriaService servicio;
    private final Scanner entrada;
    private final PrintStream salida;

    public AppConsola(CafeteriaService servicio, InputStream in, PrintStream out) {
        this.servicio = servicio;
        this.entrada = new Scanner(in, "UTF-8");
        this.salida = out;
        servicio.getCocina().setSalida(m -> salida.println("  >> " + m));
        servicio.getCaja().setSalida(m -> salida.println("  >> " + m));
        servicio.getMesero().setSalida(m -> salida.println("  >> " + m));
    }

    public static void main(String[] args) {
        new AppConsola(CafeteriaService.porDefecto(), System.in, System.out).ejecutar();
    }

    public void ejecutar() {
        salida.println("=== Cafetería - Sistema de pedidos ===");
        boolean seguir = true;
        while (seguir) {
            salida.println();
            salida.println("1. Ver menú");
            salida.println("2. Nuevo pedido");
            salida.println("3. Agregar producto a un pedido");
            salida.println("4. Agregar combo a un pedido");
            salida.println("5. Cambiar estado de un pedido");
            salida.println("6. Cobrar pedido");
            salida.println("7. Ver pedidos");
            salida.println("8. Cierre de caja");
            salida.println("0. Salir");
            try {
                switch (leerEntero("Opción: ")) {
                    case 1 -> mostrarMenu();
                    case 2 -> nuevoPedido();
                    case 3 -> agregarProducto();
                    case 4 -> agregarCombo();
                    case 5 -> cambiarEstado();
                    case 6 -> cobrar();
                    case 7 -> verPedidos();
                    case 8 -> salida.println(servicio.cierreDeCaja().comoTexto());
                    case 0 -> seguir = false;
                    default -> salida.println("Opción no válida");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                salida.println("Error: " + e.getMessage());
            }
        }
        salida.println("Hasta luego.");
    }

    private void mostrarMenu() {
        for (Categoria categoria : List.of(Categoria.BEBIDA, Categoria.PLATO, Categoria.POSTRE)) {
            salida.println("-- " + categoria + " --");
            for (ProductoBase p : servicio.getMenu().porCategoria(categoria)) {
                salida.println("  [" + p.getCodigo() + "] " + p);
            }
        }
    }

    private void nuevoPedido() {
        Pedido pedido = servicio.nuevoPedido(leerEntero("Número de mesa: "));
        salida.println("Creado " + pedido);
    }

    private void agregarProducto() {
        int numero = leerEntero("Número de pedido: ");
        Producto producto = elegirProducto("Código del producto: ");
        producto = agregarAdiciones(producto);
        servicio.agregarProducto(numero, producto);
        salida.println("Agregado: " + producto);
    }

    private Producto agregarAdiciones(Producto producto) {
        while (true) {
            List<TipoAdicion> posibles = List.of(TipoAdicion.values()).stream()
                    .filter(a -> a.aplicaA(producto)).toList();
            if (posibles.isEmpty()) {
                return producto;
            }
            salida.println("Adiciones para: " + producto);
            for (int i = 0; i < posibles.size(); i++) {
                salida.println("  " + (i + 1) + ". " + posibles.get(i));
            }
            salida.println("  0. Terminar");
            int opcion = leerEntero("Adición: ");
            if (opcion == 0) {
                return producto;
            }
            if (opcion < 1 || opcion > posibles.size()) {
                salida.println("Opción no válida");
                continue;
            }
            return agregarAdiciones(posibles.get(opcion - 1).aplicar(producto));
        }
    }

    private void agregarCombo() {
        int numero = leerEntero("Número de pedido: ");
        salida.println("1. Combo desayuno  2. Combo almuerzo  3. Armar combo paso a paso");
        Producto combo = switch (leerEntero("Opción: ")) {
            case 1 -> servicio.getDirector().comboDesayuno();
            case 2 -> servicio.getDirector().comboAlmuerzo();
            case 3 -> armarCombo();
            default -> throw new IllegalArgumentException("Opción no válida");
        };
        servicio.agregarProducto(numero, combo);
        salida.println("Agregado: " + combo);
    }

    private Producto armarCombo() {
        ComboBuilder builder = new ComboBuilder().nombre("Personalizado");
        builder.platoPrincipal(agregarAdiciones(elegirProducto("Paso 1 - código del plato principal: ")));
        builder.bebida(agregarAdiciones(elegirProducto("Paso 2 - código de la bebida: ")));
        if (leerSiNo("Paso 3 - ¿Agregar postre? (s/n): ")) {
            builder.postre(elegirProducto("Código del postre: "));
        }
        builder.descuento(10);
        return builder.build();
    }

    private void cambiarEstado() {
        int numero = leerEntero("Número de pedido: ");
        Pedido pedido = servicio.buscarPedido(numero);
        salida.println("Estado actual: " + pedido.getEstado() + ". Siguientes: "
                + pedido.getEstado().siguientesPermitidos());
        EstadoPedido nuevo = EstadoPedido.valueOf(leerTexto("Nuevo estado: ").trim().toUpperCase());
        servicio.cambiarEstado(numero, nuevo);
    }

    private void cobrar() {
        int numero = leerEntero("Número de pedido: ");
        Promocion promocion = servicio.cobrar(numero);
        Pedido pedido = servicio.buscarPedido(numero);
        salida.println("Promoción: " + promocion.getNombre());
        salida.println("Subtotal $" + pedido.getSubtotal() + " - descuento $" + pedido.getDescuento()
                + " = TOTAL $" + pedido.getTotal());
    }

    private void verPedidos() {
        if (servicio.getPedidos().isEmpty()) {
            salida.println("No hay pedidos.");
        }
        for (Pedido pedido : servicio.getPedidos()) {
            salida.println(pedido);
            pedido.getItems().forEach(p -> salida.println("    - " + p));
        }
    }

    private Producto elegirProducto(String mensaje) {
        String codigo = leerTexto(mensaje).trim().toUpperCase();
        return servicio.getMenu().buscar(codigo)
                .orElseThrow(() -> new IllegalArgumentException("No existe el producto " + codigo));
    }

    private int leerEntero(String mensaje) {
        String texto = leerTexto(mensaje).trim();
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Se esperaba un número y llegó '" + texto + "'");
        }
    }

    private boolean leerSiNo(String mensaje) {
        return leerTexto(mensaje).trim().toLowerCase().startsWith("s");
    }

    private String leerTexto(String mensaje) {
        salida.print(mensaje);
        if (!entrada.hasNextLine()) {
            return "0";
        }
        return entrada.nextLine();
    }
}
