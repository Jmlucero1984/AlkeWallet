package org.josemalucero.dominio.cuenta;

import java.math.BigDecimal;

/**
 * Las clases que implementan esta interfaz proporcionan un método para hacer retiros por una cantidad {@link BigDecimal}.
 @author José Maria Lucero
 */
public interface Retirable {
    /**
     * Realiza los retiros por la cantidad especificada.
     * @param amount
     */
    void retirar(BigDecimal amount);

}
