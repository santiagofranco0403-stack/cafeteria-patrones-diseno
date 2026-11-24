package com.cafeteria.reporte;

import com.cafeteria.modelo.Categoria;
import com.cafeteria.modelo.Producto;
import com.cafeteria.pedido.Pedido;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Resultado del cierre de caja: totales y ventas por categoría. */
public class ReporteVentas {

    private final LocalDateTime fechaCierre;
    private final int cantidadPedidos;
    private final int totalBruto;
    private final int totalDescuentos;
    private final Map<Categoria, Integer> ventasPorCategoria = new EnumMap<>(Categoria.class);

    public ReporteVentas(List<Pedido> pedidosPagados, LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
        this.cantidadPedidos = pedidosPagados.size();
        int bruto = 0;
        int descuentos = 0;
        for (Pedido pedido : pedidosPagados) {
            bruto += pedido.getSubtotal();
            descuentos += pedido.getDescuento();
            for (Producto producto : pedido.getItems()) {
                ventasPorCategoria.merge(producto.getCategoria(), producto.getPrecio(), Integer::sum);
            }
        }
        this.totalBruto = bruto;
        this.totalDescuentos = descuentos;
    }

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public int getTotalBruto() {
        return totalBruto;
    }

    public int getTotalDescuentos() {
        return totalDescuentos;
    }

    public int getTotalNeto() {
        return totalBruto - totalDescuentos;
    }

    public Map<Categoria, Integer> getVentasPorCategoria() {
        return Map.copyOf(ventasPorCategoria);
    }

    public String comoTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== CIERRE DE CAJA =====\n");
        sb.append("Fecha: ").append(fechaCierre.withNano(0)).append('\n');
        sb.append("Pedidos pagados: ").append(cantidadPedidos).append('\n');
        sb.append("Total bruto: $").append(totalBruto).append('\n');
        sb.append("Descuentos: $").append(totalDescuentos).append('\n');
        sb.append("Total neto: $").append(getTotalNeto()).append('\n');
        sb.append("Ventas por categoría:\n");
        for (Categoria categoria : Categoria.values()) {
            sb.append("  ").append(categoria).append(": $")
              .append(ventasPorCategoria.getOrDefault(categoria, 0)).append('\n');
        }
        return sb.toString();
    }
}
