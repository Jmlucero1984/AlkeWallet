package org.josemalucero.operacion.transferencia;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.OperacionTransferenciaMonedaDestino;
import org.josemalucero.dominio.operacion.OperacionTransferenciaMonedaOrigen;
import org.josemalucero.servicio.ConsoleOutputProvider;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TransferenciaEntreMonedasTest {

    OperacionTransferenciaMonedaDestino operacionTransferenciaMonedaDestino;
    OperacionTransferenciaMonedaOrigen operacionTransferenciaMonedaOrigen;
    MonedaConvertible monedaConvertibleOrigen;
    MonedaConvertible monedaConvertibleDestino;
    CuentaRegular cuentaOrigen;
    CuentaRegular cuentaDestino;
    @Test
    void validarConversionMonedaDestino() {
        cuentaDestino = new CuentaRegular();
        cuentaOrigen = new CuentaRegular();

        monedaConvertibleOrigen =  new MonedaConvertible("CLP","Peso Chileno", new BigDecimal("0.0011"));
        monedaConvertibleDestino = new MonedaConvertible("ARS","Peso Argentino", new BigDecimal("0.00069"));
        cuentaDestino.setMoneda(monedaConvertibleDestino);
        cuentaOrigen.depositar(new BigDecimal("1000.00"));
        cuentaOrigen.setMoneda(monedaConvertibleOrigen);
        BigDecimal cantidadATransferir = new BigDecimal("550.00");
        operacionTransferenciaMonedaDestino = new OperacionTransferenciaMonedaDestino(cuentaOrigen,cuentaDestino,cantidadATransferir, new ConversorMoneda(), new ConsoleOutputProvider());
        operacionTransferenciaMonedaDestino.ejecutar();
        System.out.println("MONTO EFECTIVO: "+operacionTransferenciaMonedaDestino.getMontoEfectivo());
        System.out.println("BALANCE ORIGEN: "+cuentaOrigen.getBalance());
        System.out.println("BALANCE DESTINO: "+cuentaDestino.getBalance());

    }
    @Test
    void validarConversionMonedaOrigen() {
        cuentaDestino = new CuentaRegular();
        cuentaOrigen = new CuentaRegular();

        monedaConvertibleOrigen =  new MonedaConvertible("CLP","Peso Chileno", new BigDecimal("0.0011"));
        monedaConvertibleDestino = new MonedaConvertible("ARS","Peso Argentino", new BigDecimal("0.00069"));
        cuentaDestino.setMoneda(monedaConvertibleDestino);
        cuentaOrigen.setMoneda(monedaConvertibleOrigen);
        cuentaOrigen.depositar(new BigDecimal("1000.00"));
        BigDecimal cantidadATransferir = new BigDecimal("1000.00");
        operacionTransferenciaMonedaOrigen = new OperacionTransferenciaMonedaOrigen(cuentaOrigen,cuentaDestino,cantidadATransferir, new ConversorMoneda(),new ConsoleOutputProvider());
        operacionTransferenciaMonedaOrigen.ejecutar();
        System.out.println("MONTO EFECTIVO: "+operacionTransferenciaMonedaOrigen.getMontoEfectivo());
        System.out.println("BALANCE ORIGEN: "+cuentaOrigen.getBalance());
        System.out.println("BALANCE DESTINO: "+cuentaDestino.getBalance());
    }

}
