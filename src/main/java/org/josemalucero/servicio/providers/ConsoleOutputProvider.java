package org.josemalucero.servicio.providers;

import java.math.BigDecimal;

/**
 * Clase que funciona como un proxy, ya que redirige los objetos y datos a imprimir sin
 * agregar lógica adicional.
 * @author José María Lucero
 */

public class ConsoleOutputProvider implements OutputProvider{


    @Override
    public void println(Object object) {
        System.out.println(object);
    }

    @Override
    public void printMenuln(String menu) {
        System.out.println(menu);

    }

    @Override
    public void printInfoln(String info) {
        System.out.println(info);
    }

    @Override
    public void println(String string) {
        System.out.println(string);
    }

    @Override
    public void println(BigDecimal bigDecimal) {
        System.out.println(bigDecimal);
    }

    @Override
    public void println(int intNum) {
        System.out.println(intNum);
    }

    @Override
    public void print(Object object) {System.out.print(object);}

    @Override
    public void print(String string) {
        System.out.print(string);
    }

    @Override
    public void printlnAlert(String alert) {
        System.out.println(alert);
    }
}
