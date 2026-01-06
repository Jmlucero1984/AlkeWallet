package org.josemalucero.dominio.cuenta;

import java.math.BigDecimal;

/**
 * La clases que implementan esta interfaz proporcionan un método para generar depósitos
 * en cantidades {@link BigDecimal}.
 * Author: José Maria Lucero
 */
public interface Depositable {
    /**
     * Realiza el depósito por la suma correspondiente.
     * @param amount
     */
    void depositar(BigDecimal amount);

}
