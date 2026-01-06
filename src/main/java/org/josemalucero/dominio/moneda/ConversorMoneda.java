package org.josemalucero.dominio.moneda;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Proporciona la posibilidad de realizar conversiones de montos entre diferentes monedas.
 *  @author José Maria Lucero
 */
public class ConversorMoneda {

    /**
     * Realiza una conversión de monto de una moneda a otra, con determinadas especificaciones de exactitud y reondeo.
     * @param monedaOrigen
     * @param monedaDestino
     * @param monto
     * @return {@link BigDecimal} del monto convertido a la moneda de destino.
     */
    public BigDecimal convertirMoneda(MonedaConvertible monedaOrigen, MonedaConvertible monedaDestino, BigDecimal monto){
        return  monedaOrigen.getRatioDolar().divide(monedaDestino.getRatioDolar(),10, RoundingMode.HALF_UP).multiply(monto).setScale(2,RoundingMode.HALF_UP);
    }
}
