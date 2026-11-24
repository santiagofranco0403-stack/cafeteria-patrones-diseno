package com.cafeteria.promocion;

import com.cafeteria.pedido.Pedido;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Patrón Strategy - rol: Contexto.
 * Recibe las estrategias registradas, filtra las vigentes en el momento
 * del cobro y aplica la que más le favorece al cliente (no son acumulables).
 */
public class SelectorPromociones {

    private final List<Promocion> promociones = new ArrayList<>();

    public SelectorPromociones() {
        promociones.add(new SinPromocion());
    }

    public void registrar(Promocion promocion) {
        promociones.add(promocion);
    }

    public Promocion mejorPara(Pedido pedido, LocalDateTime momento) {
        return promociones.stream()
                .filter(p -> p.aplicaEn(momento))
                .max(Comparator.comparingInt(p -> p.calcularDescuento(pedido)))
                .orElseGet(SinPromocion::new);
    }

    public Promocion aplicar(Pedido pedido, LocalDateTime momento) {
        Promocion elegida = mejorPara(pedido, momento);
        pedido.aplicarPromocion(elegida.getNombre(), elegida.calcularDescuento(pedido));
        return elegida;
    }

    public List<Promocion> getPromociones() {
        return Collections.unmodifiableList(promociones);
    }
}
