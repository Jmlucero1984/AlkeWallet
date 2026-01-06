package org.josemalucero.dominio.cuenta;

import org.josemalucero.dominio.moneda.MonedaConvertible;

import java.math.BigDecimal;

/**
 * Las clases que la implementan disponen de un método para realizar una conversión a una determinada moneda.
 *  @author José Maria Lucero
 */
public interface Convertible {
    /**
     * Realiza la conversión a la moneda especificada.
     * @param monedaDestino
     */
    void convertirAMoneda(MonedaConvertible monedaDestino);
}
