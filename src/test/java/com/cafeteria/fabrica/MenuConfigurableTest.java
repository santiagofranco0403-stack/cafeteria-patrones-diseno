package com.cafeteria.fabrica;

import com.cafeteria.modelo.Bebida;
import com.cafeteria.modelo.Categoria;
import com.cafeteria.modelo.Plato;
import com.cafeteria.modelo.Postre;
import com.cafeteria.modelo.ProductoBase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuConfigurableTest {

    @Test
    void cadaCreadorInstanciaSuTipo() {
        MenuConfigurable menu = new MenuConfigurable();
        menu.cargarLineas(List.of(
                "BEBIDA;B1;Tinto;2000;caliente",
                "PLATO;P1;Arepa;9000;8",
                "POSTRE;D1;Galleta;4000;sin_azucar"));

        assertInstanceOf(Bebida.class, menu.buscar("B1").orElseThrow());
        assertInstanceOf(Plato.class, menu.buscar("P1").orElseThrow());
        Postre galleta = (Postre) menu.buscar("D1").orElseThrow();
        assertTrue(galleta.isSinAzucar());
        assertEquals(8, ((Plato) menu.buscar("P1").orElseThrow()).getMinutosPreparacion());
    }

    @Test
    void ignoraComentariosYLineasVacias() {
        MenuConfigurable menu = new MenuConfigurable();
        menu.cargarLineas(List.of("# comentario", "", "BEBIDA;B1;Tinto;2000;caliente"));
        assertEquals(1, menu.getProductos().size());
    }

    @Test
    void tipoDesconocidoLanzaError() {
        MenuConfigurable menu = new MenuConfigurable();
        assertThrows(IllegalArgumentException.class,
                () -> menu.cargarLineas(List.of("HELADO;H1;Paleta;3000;fresa")));
    }

    @Test
    void sePuedeRegistrarUnCreadorNuevoSinTocarElMenu() {
        MenuConfigurable menu = new MenuConfigurable();
        menu.registrarCreador("SNACK", new CreadorProducto() {
            @Override
            protected ProductoBase crearProducto(ItemMenu item) {
                return new Postre(item.codigo(), item.nombre(), item.precio(), false);
            }
        });
        menu.cargarLineas(List.of("SNACK;S1;Papas;3500;-"));
        assertTrue(menu.buscar("S1").isPresent());
    }

    @Test
    void precioInvalidoEsRechazadoPorElCreador() {
        CreadorProducto creador = new CreadorBebida();
        assertThrows(IllegalArgumentException.class,
                () -> creador.crear(new ItemMenu("BEBIDA", "B9", "Agua", 0, "fria")));
    }

    @Test
    void elMenuPorDefectoCargaDesdeElArchivo() {
        MenuConfigurable menu = MenuConfigurable.desdeRecurso("/menu.txt");
        assertFalse(menu.porCategoria(Categoria.BEBIDA).isEmpty());
        assertFalse(menu.porCategoria(Categoria.PLATO).isEmpty());
        assertFalse(menu.porCategoria(Categoria.POSTRE).isEmpty());
    }
}
