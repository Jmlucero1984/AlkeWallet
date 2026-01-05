package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;

public class OperacionDeMonto extends Operacion{
    protected final BigDecimal monto;


    public OperacionDeMonto(CuentaRegular cuentaRegular, BigDecimal monto, OutputProvider outputProvider) {
        super(cuentaRegular,outputProvider);
        this.monto = monto;
    }

    @Override
    public String getNombreOperacion() {
        return "OPERACION DE MONTO";
    }

    @Override
    public void ejecutar() {

    }
}
