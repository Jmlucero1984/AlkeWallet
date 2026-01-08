package org.josemalucero.dominio.operacion;

import java.math.BigDecimal;

public class ConstantesFiscalesBancarias {
    public static final BigDecimal LIMITE_MONTO_TRANSFERENCIA= new BigDecimal("500000.00");
    public static final BigDecimal LIMITE_MONTO_DEPOSITO= new BigDecimal("150000.00");
    public static final BigDecimal LIMITE_MONTO_RETIRO= new BigDecimal("50000.00");

    public static final int LIMITE_TRANSFERENCIAS_POR_SESION = 2;
    public static final int LIMITE_DEPOSITOS_POR_SESION = 2;
    public static final int LIMITE_RETIROS_POR_SESION= 2;

    public static final int LIMITE_TRANSFERENCIAS_POR_CUENTA = 4;
    public static final int LIMITE_DEPOSITOS_POR_CUENTA = 4;
    public static final int LIMITE_RETIROS_POR_CUENTA = 4;
}
