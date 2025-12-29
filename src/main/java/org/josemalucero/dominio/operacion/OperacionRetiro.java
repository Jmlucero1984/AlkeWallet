package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.Cuenta;
import org.josemalucero.dominio.cuenta.CuentaRegular;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperacionRetiro extends OperacionDeMonto implements Validable,Reversible{
    BigDecimal saldoAnteriorCuentaOrigen;
    public OperacionRetiro(CuentaRegular cuenta, BigDecimal monto) {
        super( cuenta, monto);
    }

    @Override
    public void ejecutar() {
        saldoAnteriorCuentaOrigen = cuentaRegular.getBalance();
        cuentaRegular.retirar(monto);

    }

    @Override
    public boolean preValidar() {
        if(monto.compareTo(cuentaRegular.getBalance())<=0){
            return true;

        } else {
            System.out.println("No se puede retirar la cantidad solicitada. FONDOS INSUFICIENTES");
            return false;
        }
    }

    @Override
    public boolean posValidar() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.subtract(monto)) == 0 ) {
            return true;

        } else {
            System.out.println("HA FALLADO EL RETIRO");
            return false;
        }
    }

    @Override
    public void restaurarEstadoAnterior() {
        System.out.println("ROLLBACK");
    }
}
