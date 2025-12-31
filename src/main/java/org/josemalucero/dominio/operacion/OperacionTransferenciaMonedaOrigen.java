package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.MonedaConvertible;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OperacionTransferenciaMonedaOrigen extends OperacionTransferencia{
    BigDecimal montoEfectivo;
    public OperacionTransferenciaMonedaOrigen(CuentaRegular cuentaOrigen, CuentaRegular cuentaDestino, BigDecimal monto) {
        super(cuentaOrigen, cuentaDestino, monto);
        montoEfectivo = convertir(cuentaOrigen.getMonedaConvertible(),cuentaDestino.getMonedaConvertible(),monto);

    }
    public String getNombreOperacion() {
        return "TRANSFERENCIA A CUENTA DE OTRA MONEDA";
    }

    @Override
    public void ejecutar() {
        System.out.println("EJECUTANDO TRANSFERENCIA EN MONEDA DE ORIGEN");
        super.registrarEstadoPrevio();
        cuentaRegular.tranfiere(monto);
        cuentaDestino.recibeTransferencia(montoEfectivo);

    }

    private BigDecimal convertir(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto) {

        return  origen.getRatioDolar().divide(destino.getRatioDolar(),10, RoundingMode.HALF_UP).multiply(monto).setScale(2,RoundingMode.HALF_UP);

    }
    public BigDecimal getMontoEfectivo(){
        return montoEfectivo;
    }
    @Override
    public boolean preValidar() {
        //valorMoneda.multiply(ratio).setScale(2, RoundingMode.HALF_UP));


        if (monto.compareTo(BigDecimal.ZERO)==0) {
            System.out.println("No se puede realizar transferencia por monto igual a 0");
            return false;
        }
        if(monto.compareTo(cuentaRegular.getBalance())<=0){
            return true;

        } else {
            System.out.println("No se puede tranferir la cantidad solicitada. FONDOS INSUFICIENTES");
            return false;
        }
    }

    @Override
    public boolean posValidar() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.subtract(monto)) == 0 &&
                cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino.add(montoEfectivo)) == 0) {
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
        cuentaRegular.registrarOperacion(new RegistroOperacion(getNombreOperacion(),monto,cuentaRegular.getBalance()));
        cuentaDestino.registrarOperacion(new RegistroOperacion(getNombreOperacion()+" desde "+cuentaRegular.getSerialCuenta(),montoEfectivo,cuentaDestino.getBalance()));
    }
}


