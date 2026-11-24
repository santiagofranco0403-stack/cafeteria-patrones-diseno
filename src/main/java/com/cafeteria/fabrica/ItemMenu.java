package com.cafeteria.fabrica;

/**
 * Una línea del menú configurable. Formato en menu.txt:
 * TIPO;codigo;nombre;precio;dato_extra
 */
public record ItemMenu(String tipo, String codigo, String nombre, int precio, String extra) {

    public static ItemMenu desdeLinea(String linea) {
        String[] partes = linea.split(";");
        if (partes.length != 5) {
            throw new IllegalArgumentException("Línea de menú inválida: " + linea);
        }
        return new ItemMenu(
                partes[0].trim().toUpperCase(),
                partes[1].trim(),
                partes[2].trim(),
                Integer.parseInt(partes[3].trim()),
                partes[4].trim());
    }
}
