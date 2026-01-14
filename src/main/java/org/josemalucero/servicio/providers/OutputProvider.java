package org.josemalucero.servicio.providers;

import java.math.BigDecimal;

/**
 * Las clases que implementan esta interfaz poseen los métodos para mostrarle
 * al usuario los diferentes tipos de datos.
 * @author José María Lucero
 */

public interface OutputProvider {
    void println(Object object);
    void printMenuln(String menu);
    void printMenu(String menu);
    void printInfoln(String info);
    void println(String string);
    void println(BigDecimal bigDecimal);
    void println(int intNum);
    void print(Object object);
    void print(String string);
    void printlnAlert(String alert);
}
