package org.josemalucero.dominio.cuenta;

import org.josemalucero.dominio.moneda.Moneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;

public class Cuenta {
    String serialCuenta;

    public MonedaConvertible getMonedaConvertible() {
        return monedaConvertible;
    }

    public void setMoneda(MonedaConvertible monedaConvertible) {
        this.monedaConvertible = monedaConvertible;
    }

    MonedaConvertible monedaConvertible;

    public Cuenta() {

    }

    public String getSerialCuenta() {
        return serialCuenta;
    }

    public void setSerialCuenta(String serialCuenta) {
        this.serialCuenta = serialCuenta;
    }



}
