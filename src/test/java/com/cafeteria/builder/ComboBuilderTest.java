package com.cafeteria.builder;

import com.cafeteria.decorador.ShotExtra;
import com.cafeteria.fabrica.MenuConfigurable;
import com.cafeteria.modelo.Bebida;
import com.cafeteria.modelo.Categoria;
import com.cafeteria.modelo.Plato;
import com.cafeteria.modelo.Postre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComboBuilderTest {

    private final Plato arepa = new Plato("P02", "Arepa", 9000, 8);
    private final Bebida tinto = new Bebida("B01", "Tinto", 4500, true);
    private final Postre brownie = new Postre("D01", "Brownie", 6000, false);

    @Test
    void armaUnComboPasoAPasoConDescuento() {
        Combo combo = new ComboBuilder()
                .nombre("Prueba")
                .platoPrincipal(arepa)
                .bebida(tinto)
                .postre(brownie)
                .descuento(10)
                .build();

        assertEquals(3, combo.getComponentes().size());
        assertEquals(19500, combo.getPrecioSinDescuento());
        assertEquals(19500 - 1950, combo.getPrecio());
        assertEquals(Categoria.COMBO, combo.getCategoria());
    }

    @Test
    void aceptaProductosDecorados() {
        Combo combo = new ComboBuilder().platoPrincipal(arepa).bebida(new ShotExtra(tinto)).build();
        assertEquals(9000 + 4500 + 2500, combo.getPrecio());
    }

    @Test
    void sinPlatoPrincipalNoSeConstruye() {
        assertThrows(IllegalStateException.class, () -> new ComboBuilder().bebida(tinto).build());
    }

    @Test
    void sinBebidaNoSeConstruye() {
        assertThrows(IllegalStateException.class, () -> new ComboBuilder().platoPrincipal(arepa).build());
    }

    @Test
    void validaLaCategoriaDeCadaPaso() {
        assertThrows(IllegalArgumentException.class, () -> new ComboBuilder().bebida(arepa));
        assertThrows(IllegalArgumentException.class, () -> new ComboBuilder().postre(tinto));
    }

    @Test
    void descuentoFueraDeRangoEsRechazado() {
        assertThrows(IllegalArgumentException.class, () -> new ComboBuilder().descuento(50));
    }

    @Test
    void elDirectorArmaLosCombosDeLaCarta() {
        DirectorCombos director = new DirectorCombos(MenuConfigurable.desdeRecurso("/menu.txt"));
        assertEquals(2, director.comboDesayuno().getComponentes().size());
        assertEquals(3, director.comboAlmuerzo().getComponentes().size());
        assertEquals(15, director.comboAlmuerzo().getPorcentajeDescuento());
    }
}
