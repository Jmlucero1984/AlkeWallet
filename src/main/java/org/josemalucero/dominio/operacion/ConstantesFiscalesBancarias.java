package org.josemalucero.dominio.operacion;

import java.math.BigDecimal;

/**
 *  Clase que establece los límites de montos para transacciones, la cantidad de transacciones por sesión
 *  y el límite de las mismas por cada cuenta.
 * @author José María Lucero
 */
public class ConstantesFiscalesBancarias {
    public static final BigDecimal LIMITE_MONTO_TRANSFERENCIA= new BigDecimal("200000.00");
    public static final BigDecimal LIMITE_MONTO_DEPOSITO= new BigDecimal("150000.00");
    public static final BigDecimal LIMITE_MONTO_RETIRO= new BigDecimal("50000.00");

    public static final int LIMITE_TRANSFERENCIAS_POR_SESION = 2;
    public static final int LIMITE_DEPOSITOS_POR_SESION = 2;
    public static final int LIMITE_RETIROS_POR_SESION= 2;

    public static final int LIMITE_TRANSFERENCIAS_POR_CUENTA = 4;
    public static final int LIMITE_DEPOSITOS_POR_CUENTA = 4;
    public static final int LIMITE_RETIROS_POR_CUENTA = 4;
}
