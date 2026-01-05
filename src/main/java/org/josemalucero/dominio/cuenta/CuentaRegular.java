package org.josemalucero.dominio.cuenta;

import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;

import java.math.BigDecimal;
/**
 La clase {@code CuentaRegular} extiende la clase {@code Cuenta} con la propiedades
 y métodos elementales para registrar el activo digital, en este caso,
 la {@code MonedaConvertible}.
 Implementa las interfaces {@code Depositable},{@code Retirable},{@code Consultable},
 {@code Transferible} y {@code Convertible},
 <blockquote><b>Propiedades</b></blockquote>
 <li> {@code BigDecimal cantidadDisponible} representa el saldo actual de la cuenta.</li>

 @author José Maria Lucero
 */

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
        cantidadDisponible = new ConversorMoneda().convertirMoneda(monedaConvertible,monedaDestino,getBalance());
        monedaConvertible = monedaDestino;
    }
}
