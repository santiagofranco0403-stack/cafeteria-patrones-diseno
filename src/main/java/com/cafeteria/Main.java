package com.cafeteria;

import com.cafeteria.ui.consola.AppConsola;
import com.cafeteria.ui.gui.AppGUI;

/**
 * Punto de entrada.
 *   java -jar cafeteria.jar            -> abre la interfaz gráfica
 *   java -jar cafeteria.jar --consola  -> abre la versión por consola
 */
public class Main {

    public static void main(String[] args) {
        boolean consola = args.length > 0 && args[0].equalsIgnoreCase("--consola");
        if (consola || java.awt.GraphicsEnvironment.isHeadless()) {
            AppConsola.main(args);
        } else {
            AppGUI.main(args);
        }
    }
}
