package org.josemalucero.servicio.providers;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  Permite el suministro de las cadenas de texto que se muestran por pantalla
 *  de acuerdo al idioma del entorno sobre el que corre la aplicación.
 * @author José María Lucero
 */

public class Messages {
    private static ResourceBundle bundle;
    private static Locale currentLocale;

    public static void init(Locale locale) {
        currentLocale=locale;
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

}
