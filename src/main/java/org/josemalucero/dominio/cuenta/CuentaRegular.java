package org.josemalucero.dominio.cuenta;

import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;

import java.math.BigDecimal;

/** Cuenta con todas las facultades para ser operada por el usuario mediante cada {@link org.josemalucero.dominio.operacion.Operacion} específica.
 *  @author José Maria Lucero
 */

public class CuentaRegular extends Cuenta implements Depositable,Retirable,Consultable,Transferible,Convertible{

    BigDecimal cantidadDisponible = BigDecimal.valueOf(0);
    private int cantidad_depositos_historicos=0;
    private int cantidad_transferencias_historicas=0;
    private int cantidad_retiros_historicos=0;

    public CuentaRegular() {
        super();
    }

    public void incrementar_cantidad_depositos_historicos(){

        cantidad_depositos_historicos++;

    }

    public void incrementar_cantidad_retiros_historicos(){
        cantidad_retiros_historicos++;
    }

    public void incrementar_cantidad_transferencias_historicas(){
        cantidad_transferencias_historicas++;
    }

    public int getCantidad_depositos_historicos() {
        return cantidad_depositos_historicos;
    }

    public int getCantidad_transferencias_historicas() {
        return cantidad_transferencias_historicas;
    }

    public int getCantidad_retiros_historicos() {
        return cantidad_retiros_historicos;
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

    /**
     * Realiza la conversipon de la propia cuenta, de la moneda especificada original a otra de destino, haciendo lo
     * propio con el saldo actual de la misma.
     * @param monedaDestino
     */
    @Override
    public void convertirAMoneda(MonedaConvertible monedaDestino) {
        cantidadDisponible = new ConversorMoneda().convertirMoneda(monedaConvertible,monedaDestino,getBalance());
        monedaConvertible = monedaDestino;
    }
}
