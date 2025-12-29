package org.josemalucero.dominio.cuenta;

import java.math.BigDecimal;

public interface Consultable {
    BigDecimal getBalance();
    String getNumeroCuenta();

}
