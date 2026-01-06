package org.josemalucero.dominio.cuenta;

import java.math.BigDecimal;

/**
 * Las clases que implementan esta interfaz proporcionan un método para realizar y recibir transferencias por montos {@link BigDecimal}.
 * @author José Maria Lucero
 */
public interface Transferible {
        /**
         * Realiza la transferencia por el monto especificado.
         * @param amount
         */
        void tranfiere(BigDecimal amount);

        /**
         * Recibe una transferencia por el monto especificado.
         * @param amount
         */
        void recibeTransferencia(BigDecimal amount);

}
