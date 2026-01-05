package org.josemalucero.servicio;

import java.math.BigDecimal;

public interface OutputProvider {
    void println(Object object);
    void println(String string);
    void println(BigDecimal bigDecimal);
    void println(int intNum);
    void print(Object object);
    void print(String string);
}
