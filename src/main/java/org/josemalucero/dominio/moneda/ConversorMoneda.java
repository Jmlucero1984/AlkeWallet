package org.josemalucero.dominio.moneda;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConversorMoneda {

    public BigDecimal convertirMoneda(MonedaConvertible monedaOrigen, MonedaConvertible monedaDestino, BigDecimal monto){

        return  monedaOrigen.getRatioDolar().divide(monedaDestino.getRatioDolar(),10, RoundingMode.HALF_UP).multiply(monto).setScale(2,RoundingMode.HALF_UP);
    }
}
