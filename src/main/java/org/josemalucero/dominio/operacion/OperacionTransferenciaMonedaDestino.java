package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.MonedaConvertible;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OperacionTransferenciaMonedaDestino extends OperacionTransferencia{
    BigDecimal montoEfectivo;
    public OperacionTransferenciaMonedaDestino(CuentaRegular cuentaOrigen, CuentaRegular cuentaDestino, BigDecimal monto) {
        super(cuentaOrigen, cuentaDestino, monto);
        montoEfectivo = convertir(cuentaOrigen.getMonedaConvertible(),cuentaDestino.getMonedaConvertible(),monto);

    }

    @Override
    public void ejecutar() {
        System.out.println("EJECUTANDO TRANSFERENCIA EN MONEDA DE DESTINO");
        super.registrarEstadoPrevio();
        cuentaRegular.tranfiere(montoEfectivo);
        cuentaDestino.recibeTransferencia(monto);

    }
    public String getNombreOperacion() {
        return "TRANSFERENCIA A CUENTA DE OTRA MONEDA";
    }

    public BigDecimal getMontoEfectivo(){
        return montoEfectivo;
    }

    private BigDecimal convertir(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto) {
        return  destino.getRatioDolar().divide(origen.getRatioDolar(),10, RoundingMode.HALF_UP).multiply(monto).setScale(2,RoundingMode.HALF_UP);

    }

    @Override
    public boolean preValidar() {
        //valorMoneda.multiply(ratio).setScale(2, RoundingMode.HALF_UP));


        if (monto.compareTo(BigDecimal.ZERO)==0) {
            System.out.println("No se puede realizar transferencia por monto igual a 0");
            return false;
        }
        if(montoEfectivo.compareTo(cuentaRegular.getBalance())<=0){
            return true;

        } else {
            System.out.println("No se puede tranferir la cantidad solicitada. FONDOS INSUFICIENTES");
            return false;
        }
    }

    @Override
    public boolean posValidar() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.subtract(montoEfectivo)) == 0 &&
                cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino.add(monto)) == 0) {
            return true;

        } else {
            System.out.println("HA FALLADO LA TRANSFERENCIA");
            return false;
        }
    }


    @Override
    public void restaurarEstadoAnterior() {
        super.restaurarEstadoAnterior();
    }

    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        cuentaRegular.registrarOperacion(new RegistroOperacion(getNombreOperacion(),montoEfectivo,cuentaRegular.getBalance()));
        cuentaDestino.registrarOperacion(new RegistroOperacion(getNombreOperacion()+" desde "+cuentaRegular.getSerialCuenta(),monto,cuentaDestino.getBalance()));
    }
}

