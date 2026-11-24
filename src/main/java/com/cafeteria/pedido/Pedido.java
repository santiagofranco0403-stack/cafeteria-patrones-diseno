package com.cafeteria.pedido;

import com.cafeteria.modelo.Producto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Patrón Observer (comportamiento) - rol: Sujeto.
 * Mantiene la lista de observadores y les avisa cada vez que cambia su estado.
 */
public class Pedido {

    private final int numero;
    private final int mesa;
    private final LocalDateTime creadoEn;
    private final List<Producto> items = new ArrayList<>();
    private final List<ObservadorPedido> observadores = new ArrayList<>();
    private EstadoPedido estado = EstadoPedido.RECIBIDO;
    private String promocionAplicada = "Ninguna";
    private int descuento;

    public Pedido(int numero, int mesa, LocalDateTime creadoEn) {
        this.numero = numero;
        this.mesa = mesa;
        this.creadoEn = creadoEn;
    }

    // ----- Gestión de observadores -----

    public void suscribir(ObservadorPedido observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void desuscribir(ObservadorPedido observador) {
        observadores.remove(observador);
    }

    private void notificar(EstadoPedido anterior) {
        for (ObservadorPedido observador : List.copyOf(observadores)) {
            observador.estadoCambiado(this, anterior);
        }
    }

    // ----- Lógica del pedido -----

    public void agregar(Producto producto) {
        if (estado != EstadoPedido.RECIBIDO) {
            throw new IllegalStateException("Solo se pueden agregar productos a un pedido recién recibido");
        }
        items.add(producto);
    }

    public void cambiarEstado(EstadoPedido nuevo) {
        if (!estado.puedePasarA(nuevo)) {
            throw new IllegalStateException("No se puede pasar de " + estado + " a " + nuevo);
        }
        if (nuevo == EstadoPedido.EN_PREPARACION && items.isEmpty()) {
            throw new IllegalStateException("No se puede preparar un pedido vacío");
        }
        EstadoPedido anterior = estado;
        estado = nuevo;
        notificar(anterior);
    }

    public int getSubtotal() {
        return items.stream().mapToInt(Producto::getPrecio).sum();
    }

    public int getTotal() {
        return getSubtotal() - descuento;
    }

    public void aplicarPromocion(String nombre, int descuento) {
        if (descuento < 0 || descuento > getSubtotal()) {
            throw new IllegalArgumentException("Descuento inválido: " + descuento);
        }
        this.promocionAplicada = nombre;
        this.descuento = descuento;
    }

    public int getNumero() {
        return numero;
    }

    public int getMesa() {
        return mesa;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public List<Producto> getItems() {
        return Collections.unmodifiableList(items);
    }

    public String getPromocionAplicada() {
        return promocionAplicada;
    }

    public int getDescuento() {
        return descuento;
    }

    @Override
    public String toString() {
        return "Pedido #" + numero + " (mesa " + mesa + ") - " + estado + " - $" + getTotal();
    }
}
