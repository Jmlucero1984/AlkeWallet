package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.Consultable;
import org.josemalucero.dominio.cuenta.Cuenta;
import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.cuenta.Transferible;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperacionTransferencia extends OperacionDeMonto implements Reversible,Validable{
    protected final CuentaRegular cuentaDestino;
    BigDecimal saldoAnteriorCuentaOrigen;
    BigDecimal saldoAnteriorCuentaDestino;
    public OperacionTransferencia (CuentaRegular cuentaOrigen, CuentaRegular cuentaDestino, BigDecimal monto) {

        super( cuentaOrigen, monto);
        this.cuentaDestino = cuentaDestino;
    }

    @Override
    public void ejecutar() {
        saldoAnteriorCuentaOrigen = cuentaRegular.getBalance();
        saldoAnteriorCuentaDestino = cuentaDestino.getBalance();
        cuentaRegular.tranfiere(monto);
        cuentaDestino.recibeTransferencia(monto);


    }

    @Override
    public boolean preValidar() {
        if (monto.compareTo(BigDecimal.ZERO)==0) {
            System.out.println("No se puede realizar transferencia por monto igual a 0");
            return false;
        }
        if(monto.compareTo(cuentaRegular.getBalance())<=0){
            return true;

        } else {
            System.out.println("No se puede tranferir la cantidad solicitada. FONDOS INSUFICIENTES");
            return false;
        }
    }

    @Override
    public boolean posValidar() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.subtract(monto)) == 0 &&
                cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino.add(monto)) == 0) {
            return true;

        } else {
            System.out.println("HA FALLADO LA TRANSFERENCIA");
            return false;
        }
    }

    @Override
    public void restaurarEstadoAnterior() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen)<0){
            cuentaRegular.depositar(saldoAnteriorCuentaOrigen.subtract(cuentaRegular.getBalance()));
        } else if (cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen)>0) {
            cuentaRegular.retirar(cuentaRegular.getBalance().subtract(saldoAnteriorCuentaOrigen));
        }
        if(cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino)<0){
            cuentaDestino.depositar(saldoAnteriorCuentaDestino.subtract(cuentaDestino.getBalance()));
        } else if (cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino)>0) {
            cuentaDestino.retirar(cuentaDestino.getBalance().subtract(saldoAnteriorCuentaDestino));
        }


    }
}
