package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;

import java.math.BigDecimal;

public class OperacionConsulta extends Operacion{

    public OperacionConsulta(CuentaRegular cuentaRegular) {
        super(cuentaRegular);
    }

    @Override
    public void ejecutar() {
        System.out.println("EL SALDO DE LA CUENTA EN "+cuentaRegular.getMonedaConvertible().getNombre().toUpperCase()+" ES :");
        System.out.println(cuentaRegular.getBalance()+" "+cuentaRegular.getMonedaConvertible().getCodigo());

    }


}
