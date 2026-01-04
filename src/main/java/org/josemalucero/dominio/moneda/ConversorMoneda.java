package org.josemalucero.dominio.moneda;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConversorMoneda {

    /**
     * Descripción breve de la clase/método.
     *
     * @param monedaOrigen Descripción del parámetro (obligatorio para métodos)
     * @param monedaDestino Descripción del parámetro (obligatorio para métodos)
     * @return Descripción del valor de retorno (obligatorio si no es void)
     */
    public BigDecimal convertirMoneda(MonedaConvertible monedaOrigen, MonedaConvertible monedaDestino, BigDecimal monto){
        return  monedaOrigen.getRatioDolar().divide(monedaDestino.getRatioDolar(),10, RoundingMode.HALF_UP).multiply(monto).setScale(2,RoundingMode.HALF_UP);
    }
}
