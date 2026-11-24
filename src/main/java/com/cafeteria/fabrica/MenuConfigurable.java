package com.cafeteria.fabrica;

import com.cafeteria.modelo.Categoria;
import com.cafeteria.modelo.ProductoBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Menú cargado desde texto. Usa un creador (Factory Method) por cada tipo,
 * así que agregar un tipo nuevo de producto solo exige registrar un creador nuevo.
 */
public class MenuConfigurable {

    private final Map<String, CreadorProducto> creadores = new HashMap<>();
    private final Map<String, ProductoBase> productos = new LinkedHashMap<>();

    public MenuConfigurable() {
        registrarCreador("BEBIDA", new CreadorBebida());
        registrarCreador("PLATO", new CreadorPlato());
        registrarCreador("POSTRE", new CreadorPostre());
    }

    public void registrarCreador(String tipo, CreadorProducto creador) {
        creadores.put(tipo.toUpperCase(), creador);
    }

    public void cargarLineas(List<String> lineas) {
        for (String linea : lineas) {
            String limpia = linea.trim();
            if (limpia.isEmpty() || limpia.startsWith("#")) {
                continue;
            }
            agregar(ItemMenu.desdeLinea(limpia));
        }
    }

    public ProductoBase agregar(ItemMenu item) {
        CreadorProducto creador = creadores.get(item.tipo());
        if (creador == null) {
            throw new IllegalArgumentException("No hay creador para el tipo " + item.tipo());
        }
        ProductoBase producto = creador.crear(item);
        productos.put(producto.getCodigo(), producto);
        return producto;
    }

    public static MenuConfigurable desdeRecurso(String recurso) {
        MenuConfigurable menu = new MenuConfigurable();
        InputStream entrada = MenuConfigurable.class.getResourceAsStream(recurso);
        if (entrada == null) {
            throw new IllegalStateException("No se encontró el menú " + recurso);
        }
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8))) {
            menu.cargarLineas(lector.lines().toList());
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el menú", e);
        }
        return menu;
    }

    public Optional<ProductoBase> buscar(String codigo) {
        return Optional.ofNullable(productos.get(codigo));
    }

    public List<ProductoBase> getProductos() {
        return Collections.unmodifiableList(new ArrayList<>(productos.values()));
    }

    public List<ProductoBase> porCategoria(Categoria categoria) {
        return productos.values().stream()
                .filter(p -> p.getCategoria() == categoria)
                .toList();
    }
}
