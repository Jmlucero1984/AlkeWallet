package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;

/**
 * Clase auxiliar que sirve para empaquetar todos los datos necesarios para una transferencia, disminuyendo la cantidad de
 * parametros definidos en la {@link OperacionTransferencia} y evitando una instanciación sobrecargada de argumentos.
 *@author José Maria Lucero
 */
public class DatosTransferencia {

    private CuentaRegular cuentaOrigen;
    private CuentaRegular cuentaDestino;
    private BigDecimal monto;
    private ConversorMoneda conversorMoneda;



    public DatosTransferencia(CuentaRegular cuentaOrigen, CuentaRegular cuentaDestino, BigDecimal monto, ConversorMoneda conversorMoneda) {
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.conversorMoneda = conversorMoneda;
    }

    public CuentaRegular getCuentaOrigen() {
        return cuentaOrigen;
    }

    public CuentaRegular getCuentaDestino() {
        return cuentaDestino;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public ConversorMoneda getConversorMoneda() {
        return conversorMoneda;
    }





}