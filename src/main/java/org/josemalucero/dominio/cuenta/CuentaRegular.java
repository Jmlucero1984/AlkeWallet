package org.josemalucero.dominio.cuenta;

import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;

import java.math.BigDecimal;

public class CuentaRegular extends Cuenta implements Depositable,Retirable,Consultable,Transferible,Convertible{

    BigDecimal cantidadDisponible = BigDecimal.valueOf(0);

    public CuentaRegular() {
        super();
    }

    @Override
    public BigDecimal getBalance() {
        return cantidadDisponible;
    }

    @Override
    public String getNumeroCuenta() {
        return serialCuenta;
    }

    @Override
    public void depositar(BigDecimal amount) {
            cantidadDisponible=cantidadDisponible.add(amount);
    }

    @Override
    public void retirar(BigDecimal amount) {
            cantidadDisponible= cantidadDisponible.subtract(amount);
    }

    @Override
    public void tranfiere(BigDecimal amount) {
       retirar(amount);
    }

    @Override
    public void recibeTransferencia(BigDecimal amount) {
        depositar(amount);
    }


    @Override
    public void convertirAMoneda(MonedaConvertible monedaDestino) {
        BigDecimal nuevoBalance = new ConversorMoneda().convertirMoneda(monedaConvertible,monedaDestino,this.getBalance());
        cantidadDisponible = nuevoBalance;
        monedaConvertible = monedaDestino;
    }
}
