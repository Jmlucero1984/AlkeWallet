package org.josemalucero.dominio.operacion;


import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;
/** Permite la realización de retiros de la cuenta relacionada. Permite validar la operación y en caso de ser efectiva,
 * realiza el registro de la misma en el historial de la cuenta.
 * @author Jose María Lucero
 */
public class OperacionRetiro extends OperacionDeMonto implements Validable,Reversible,Registrable{
    BigDecimal saldoAnteriorCuentaOrigen;

    public OperacionRetiro(CuentaRegular cuenta, BigDecimal monto, OutputProvider outputProvider) {
        super( cuenta, monto,outputProvider);
    }

    @Override
    public void ejecutar() {
        saldoAnteriorCuentaOrigen = cuentaRegular.getBalance();
        cuentaRegular.retirar(monto);

    }

    @Override
    public String getNombreOperacion() {
        return "RETIRO DE CUENTA";
    }

    /**
     * Antes de realizar un retiro de la cuenta, verifica que los fondos existentes sean suficientes como
     * para esa operación.
     * @return {@code bool} que indica la posibilidad de ejecutar el retiro de los fondos especificados.
     */
    @Override
    public boolean preValidar() {
        if(monto.compareTo(cuentaRegular.getBalance())<=0){
            return true;

        } else {
            outputProvider.println("No se puede retirar la cantidad solicitada. FONDOS INSUFICIENTES");
            return false;
        }
    }

    /**
     * Realiza la validación del retiro comparando el balance actual contra el balance previo menos la suma
     * indicada como depósito.
     * @return {@code boolean} que indica el éxito o error en la operación.
     */
    @Override
    public boolean postValidar() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.subtract(monto)) == 0 ) {
            return true;

        } else {
            outputProvider.println("HA FALLADO EL RETIRO");
            return false;
        }
    }

    /**
     * @TODO Implementar los pasos necesarios para revertir esta operación y devolver la cuenta a su estado anterior.
     */
    @Override
    public void restaurarEstadoAnterior() {

        outputProvider.println("ROLLBACK");

    }

    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        cuentaRegular.registrarOperacion(new RegistroOperacion(getNombreOperacion(),monto,cuentaRegular.getBalance()));
    }
}
