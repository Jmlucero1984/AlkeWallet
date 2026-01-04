package org.josemalucero.dominio.cuenta;

import org.josemalucero.dominio.moneda.MonedaConvertible;

import java.math.BigDecimal;

public interface Convertible {
    void convertirAMoneda(MonedaConvertible monedaDestino);
}
