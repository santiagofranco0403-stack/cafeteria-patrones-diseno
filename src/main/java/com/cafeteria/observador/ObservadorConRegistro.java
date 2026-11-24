package com.cafeteria.observador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base común de los observadores: guarda los avisos recibidos y permite que
 * la consola o la GUI se enganchen para mostrarlos en pantalla.
 */
public abstract class ObservadorConRegistro {

    private final List<String> avisos = new ArrayList<>();
    private Consumer<String> salida = mensaje -> { };

    protected void registrar(String mensaje) {
        avisos.add(mensaje);
        salida.accept(mensaje);
    }

    public void setSalida(Consumer<String> salida) {
        this.salida = salida == null ? mensaje -> { } : salida;
    }

    public List<String> getAvisos() {
        return Collections.unmodifiableList(avisos);
    }
}
