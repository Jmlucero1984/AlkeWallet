package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
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
    @Override
    public String getNombreOperacion() {
        return "DEPÓSITO EN CUENTA";
    }


    @Override
    public boolean preValidar() {
        if(monto.compareTo(BigDecimal.ZERO)>=0){
            return true;
        } else {
            outputProvider.println("No se pueden depositar cantidades negativas");
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
            outputProvider.println("HA FALLADO EL DEPOSITO");
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
