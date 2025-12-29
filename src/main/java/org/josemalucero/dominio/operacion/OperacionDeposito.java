package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.Cuenta;
import org.josemalucero.dominio.cuenta.CuentaRegular;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperacionDeposito extends OperacionDeMonto implements Validable,Reversible{
    public OperacionDeposito(CuentaRegular cuenta, BigDecimal monto) {
        super( cuenta, monto);
    }
    BigDecimal saldoAnteriorCuentaOrigen;
    @Override
    public void ejecutar() {
        saldoAnteriorCuentaOrigen = cuentaRegular.getBalance();
        cuentaRegular.depositar(monto);

    }


    @Override
    public boolean preValidar() {
        if(monto.compareTo(BigDecimal.ZERO)>=0){
            return true;
        } else {
            System.out.println("No se pueden depositar cantidades negativas");
            return  false;
        }
    }

    @Override
    public boolean posValidar() {
        if (cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.add(monto)) == 0) {
            return true;

        } else {
            System.out.println("HA FALLADO EL DEPOSITO");
            return false;
        }
    }

    @Override
    public void restaurarEstadoAnterior() {
        System.out.println("ROLLBACK");
    }
}
