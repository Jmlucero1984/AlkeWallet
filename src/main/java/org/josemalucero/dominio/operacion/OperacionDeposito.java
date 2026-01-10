package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

import java.math.BigDecimal;

/** Permite la realización de depósitos en la cuenta relacionada. Permite validar la operación y en caso de ser efectiva,
 * realiza el registro de la misma en el historial de la cuenta.
 * @author Jose María Lucero
 */
public class OperacionDeposito extends OperacionDeMonto implements Validable,Reversible, Registrable{
    BigDecimal saldoAnteriorCuentaOrigen;

    public OperacionDeposito(CuentaRegular cuenta, BigDecimal monto, OutputProvider outputProvider) {
        super( cuenta, monto,outputProvider);
    }

    @Override
    public void ejecutar() {
        saldoAnteriorCuentaOrigen = cuentaRegular.getBalance();
        cuentaRegular.depositar(monto);

    }




    /**
     *
     * @return {@code boolean} que indica el si el depósito es mayor que 0 y si el monto está dentro de los límites
     * establecidos por la entidad bancaria.
     */
    @Override
    public boolean preValidar() {
        if(monto.compareTo(BigDecimal.ZERO)>0){
            if(monto.compareTo(ConstantesFiscalesBancarias.LIMITE_MONTO_DEPOSITO)>0){
                outputProvider.printlnAlert(Messages.get("alerta.no.se.puede.depositar.cantidad.limite.bancario"));
                outputProvider.printInfoln(Messages.get("limite.deposito.por.operacion")+": "+ConstantesFiscalesBancarias.LIMITE_MONTO_DEPOSITO);
                return false;
            }
            return true;
        } else if(monto.compareTo(BigDecimal.ZERO)<0) {
            outputProvider.printlnAlert(Messages.get("alerta.no.depositar.cantidades.negativas"));
            return  false;
        } else {
            outputProvider.printlnAlert(Messages.get("alerta.no.depositar.cantidad.nula"));
            return  false;
        }
    }

    /**
     * Realiza la validación del depósito comparando el balance actual contra el balance previo más la suma
     * indicada como depósito.
     * @return {@code boolean} que indica el éxito o error en la operación.
     */
    @Override
    public boolean postValidar() {
        if (cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.add(monto)) == 0) {

            return true;

        } else {
            outputProvider.printlnAlert(Messages.get("alerta.ha.fallado.deposito"));
            return false;
        }
    }
    /**
     * @TODO Implementar los pasos necesarios para revertir esta operación y devolver la cuenta a su estado anterior.
     */
    @Override
    public void restaurarEstadoAnterior() {
        outputProvider.println(Messages.get("rollback"));
    }

    /***
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
        return Messages.get("operacion.deposito") ;
    }




}
