package org.josemalucero.servicio;

import java.math.BigDecimal;

public class ConsoleOutputProvider implements OutputProvider{
    @Override
    public void println(Object object) {
        System.out.println(object);
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
    public void print(Object object) {

        System.out.print(object);

    }

    @Override
    public void print(String string) {
        System.out.print(string);
    }
}
