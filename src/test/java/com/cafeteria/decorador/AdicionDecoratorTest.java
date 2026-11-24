package com.cafeteria.decorador;

import com.cafeteria.modelo.Bebida;
import com.cafeteria.modelo.Categoria;
import com.cafeteria.modelo.Plato;
import com.cafeteria.modelo.Producto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdicionDecoratorTest {

    private final Producto capuchino = new Bebida("B02", "Capuchino", 6500, true);

    @Test
    void unaAdicionSumaPrecioYDescripcion() {
        Producto conShot = new ShotExtra(capuchino);
        assertEquals(9000, conShot.getPrecio());
        assertEquals("Capuchino (caliente) + shot extra", conShot.getDescripcion());
    }

    @Test
    void lasAdicionesSePuedenApilar() {
        Producto completo = new Crema(new LecheAlmendras(new ShotExtra(capuchino)));
        assertEquals(6500 + 2500 + 3000 + 1500, completo.getPrecio());
        assertTrue(completo.getDescripcion().endsWith("+ shot extra + leche de almendras + crema batida"));
    }

    @Test
    void elDecoradorConservaLaCategoria() {
        assertEquals(Categoria.BEBIDA, new Crema(capuchino).getCategoria());
    }

    @Test
    void elProductoOriginalNoCambia() {
        new ShotExtra(capuchino);
        assertEquals(6500, capuchino.getPrecio());
    }

    @Test
    void tipoAdicionValidaQueAplique() {
        Producto sandwich = new Plato("P01", "Sándwich", 14000, 10);
        assertTrue(TipoAdicion.QUESO_EXTRA.aplicaA(sandwich));
        assertFalse(TipoAdicion.CREMA.aplicaA(sandwich));
        assertThrows(IllegalArgumentException.class, () -> TipoAdicion.CREMA.aplicar(sandwich));
        assertEquals(16000, TipoAdicion.QUESO_EXTRA.aplicar(sandwich).getPrecio());
    }

    @Test
    void noSePuedeDecorarNull() {
        assertThrows(IllegalArgumentException.class, () -> new Crema(null));
    }
}
