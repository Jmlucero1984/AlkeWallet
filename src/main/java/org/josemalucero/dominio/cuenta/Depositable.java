package org.josemalucero.dominio.cuenta;

import java.math.BigDecimal;

public interface Depositable {
    void depositar(BigDecimal amount);
}
