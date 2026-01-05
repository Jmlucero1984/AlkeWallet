package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.Cuenta;
import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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


    @Override
    public boolean preValidar() {
        if(monto.compareTo(cuentaRegular.getBalance())<=0){
            return true;

        } else {
            outputProvider.println("No se puede retirar la cantidad solicitada. FONDOS INSUFICIENTES");
            return false;
        }
    }

    @Override
    public boolean posValidar() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.subtract(monto)) == 0 ) {
            return true;

        } else {
            outputProvider.println("HA FALLADO EL RETIRO");
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
