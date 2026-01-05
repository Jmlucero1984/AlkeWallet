package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;

public class OperacionDeposito extends OperacionDeMonto implements Validable,Reversible, Registrable{
    public OperacionDeposito(CuentaRegular cuenta, BigDecimal monto, OutputProvider outputProvider) {
        super( cuenta, monto,outputProvider);
    }
    BigDecimal saldoAnteriorCuentaOrigen;
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

    @Override
    public boolean postValidar() {
        if (cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.add(monto)) == 0) {

            return true;

        } else {
            outputProvider.println("HA FALLADO EL DEPOSITO");
            return false;
        }
    }

    @Override
    public void restaurarEstadoAnterior() {
        outputProvider.println("ROLLBACK");
    }

    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        cuentaRegular.registrarOperacion(new RegistroOperacion(getNombreOperacion(),monto,cuentaRegular.getBalance()));
    }


}
