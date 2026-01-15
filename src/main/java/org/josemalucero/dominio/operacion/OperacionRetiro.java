package org.josemalucero.dominio.operacion;


import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void ejecutar() {
        saldoAnteriorCuentaOrigen = cuentaRegular.getBalance();
        cuentaRegular.retirar(monto);

    }

    /**
     * Antes de realizar un retiro de la cuenta, verifica que los fondos existentes sean suficientes como
     * para esa operación. Asi mismo verifica que el monto no tenga restricciones
     * bancarias o fiscales.
     * @return {@code bool} que indica la posibilidad de ejecutar el retiro de los fondos especificados.
     */
    @Override
    public boolean preValidar() {
        if(monto.compareTo(new BigDecimal("0.00"))==0){
            outputProvider.printlnAlert(Messages.get("alerta.retiro.cantidad.zero"));
            return false;
        }
       else if(monto.compareTo(cuentaRegular.getBalance())<=0){
            if(monto.compareTo(ConstantesFiscalesBancarias.LIMITE_MONTO_RETIRO)>0){
                outputProvider.printlnAlert(Messages.get("alerta.no.se.puede.retirar.cantidad.limite.bancario"));
                outputProvider.printInfoln(Messages.get("limite.retiro.por.operacion")+": "+ConstantesFiscalesBancarias.LIMITE_MONTO_RETIRO);
                return false;
            }
            return true;

        } else {
            outputProvider.printlnAlert(Messages.get("alerta.fondos.insuficientes"));
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
            outputProvider.printlnAlert(Messages.get("alerta.ha.fallado.retiro"));
            return false;
        }
    }


    /**
     * TODO Implementar los pasos necesarios para revertir esta operación y devolver la cuenta a su estado anterior.
     */
    @Override
    public void restaurarEstadoAnterior() {
        outputProvider.println(Messages.get("rollback"));
    }

    /**
     * {@inheritDoc}
     * @param cuentaRegular
     */
    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        cuentaRegular.registrarOperacion(new RegistroOperacion(getNombreOperacion(),monto,cuentaRegular.getBalance()));
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getNombreOperacion() {
        return Messages.get("operacion.retiro");
    }
}
