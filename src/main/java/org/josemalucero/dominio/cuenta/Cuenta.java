package org.josemalucero.dominio.cuenta;

import org.josemalucero.dominio.moneda.Moneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;

public class Cuenta {
    String serialCuenta;

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    Moneda moneda;

    public Cuenta() {

    }

    public String getSerialCuenta() {
        return serialCuenta;
    }

    public void setSerialCuenta(String serialCuenta) {
        this.serialCuenta = serialCuenta;
    }



}
