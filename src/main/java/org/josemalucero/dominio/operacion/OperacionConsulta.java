package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;

public class OperacionConsulta extends Operacion{

    public OperacionConsulta(CuentaRegular cuentaRegular, OutputProvider outputProvider) {
        super(cuentaRegular, outputProvider);
    }

    @Override
    public void ejecutar() {
        outputProvider.println("EL SALDO DE LA CUENTA EN "+cuentaRegular.getMonedaConvertible().getNombre().toUpperCase()+" ES :");
        outputProvider.println(cuentaRegular.getBalance()+" "+cuentaRegular.getMonedaConvertible().getCodigo());

    }


}
