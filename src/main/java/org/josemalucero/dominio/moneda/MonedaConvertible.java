package org.josemalucero.dominio.moneda;

import java.math.BigDecimal;

public class MonedaConvertible extends Moneda{
    private BigDecimal ratioDolar;

    public MonedaConvertible(String codigo, String nombre,BigDecimal ratioDolar) {
        super(codigo, nombre);
        this.ratioDolar = ratioDolar;
    }

    public BigDecimal getRatioDolar() {
        return ratioDolar;
    }

    public void setRatioDolar(BigDecimal ratioDolar) {
        this.ratioDolar = ratioDolar;
    }
}
