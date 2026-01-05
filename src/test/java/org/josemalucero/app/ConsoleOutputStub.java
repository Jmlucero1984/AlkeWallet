package org.josemalucero.app;

import org.josemalucero.servicio.ConsoleOutputProvider;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;

public class ConsoleOutputStub implements OutputProvider {
    @Override
    public void println(Object object) {

    }

    @Override
    public void println(String string) {

    }

    @Override
    public void println(BigDecimal bigDecimal) {

    }

    @Override
    public void println(int intNum) {

    }

    @Override
    public void print(Object object) {

    }

    @Override
    public void print(String string) {

    }
}
