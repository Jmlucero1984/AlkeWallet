package org.josemalucero.dominio.cuenta;

import java.math.BigDecimal;

public interface Transferible {
        void tranfiere(BigDecimal amount);
        void recibeTransferencia(BigDecimal amount);

}
