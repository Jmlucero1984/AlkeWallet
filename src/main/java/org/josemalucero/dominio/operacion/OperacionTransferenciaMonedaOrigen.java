package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OperacionTransferenciaMonedaOrigen extends OperacionTransferencia{
    BigDecimal montoEfectivo;
    ConversorMoneda conversorMoneda;
    public OperacionTransferenciaMonedaOrigen(CuentaRegular cuentaOrigen, CuentaRegular cuentaDestino, BigDecimal monto, ConversorMoneda conversorMoneda, OutputProvider outputProvider) {
        super(cuentaOrigen, cuentaDestino, monto, outputProvider);
        this.conversorMoneda = conversorMoneda;
        montoEfectivo = convertir(cuentaOrigen.getMonedaConvertible(),cuentaDestino.getMonedaConvertible(),monto);

    }
    public OperacionTransferenciaMonedaOrigen(DatosTransferencia datosTransferencia, OutputProvider outputProvider) {
        super(datosTransferencia.getCuentaOrigen(), datosTransferencia.getCuentaDestino(), datosTransferencia.getMonto(), outputProvider);
        this.conversorMoneda = conversorMoneda;
        montoEfectivo = convertir(datosTransferencia.getCuentaOrigen().getMonedaConvertible(),datosTransferencia.getCuentaDestino().getMonedaConvertible(),datosTransferencia.getMonto());

    }

    @Override
    public String getNombreOperacion() {
        return "TRANSFERENCIA A CUENTA DE DISTINTA MONEDA";
    }

    @Override
    public String getNombreOperacionReciproca() {
        return "TRANSFERENCIA DESDE CUENTA DE DISTINTA MONEDA";
    }

    @Override
    public void ejecutar() {
        outputProvider.println("EJECUTANDO TRANSFERENCIA EN MONEDA DE ORIGEN");
        super.registrarEstadoPrevio();
        cuentaRegular.tranfiere(monto);
        cuentaDestino.recibeTransferencia(montoEfectivo);

    }

    private BigDecimal convertir(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto) {
        return conversorMoneda.convertirMoneda(origen, destino, monto);
       // return  origen.getRatioDolar().divide(destino.getRatioDolar(),10, RoundingMode.HALF_UP).multiply(monto).setScale(2,RoundingMode.HALF_UP);

    }
    public BigDecimal getMontoEfectivo(){
        return montoEfectivo;
    }
    @Override
    public boolean preValidar() {
        //valorMoneda.multiply(ratio).setScale(2, RoundingMode.HALF_UP));


        if (monto.compareTo(BigDecimal.ZERO)==0) {
            outputProvider.println("No se puede realizar transferencia por monto igual a 0");
            return false;
        }
        if(monto.compareTo(cuentaRegular.getBalance())<=0){
            return true;

        } else {
            outputProvider.println("No se puede tranferir la cantidad solicitada. FONDOS INSUFICIENTES");
            return false;
        }
    }

    @Override
    public boolean postValidar() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.subtract(monto)) == 0 &&
                cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino.add(montoEfectivo)) == 0) {
            return true;

        } else {
            outputProvider.println("HA FALLADO LA TRANSFERENCIA");
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
        cuentaDestino.registrarOperacion(new RegistroOperacion(getNombreOperacionReciproca()+" (C.N° "+cuentaRegular.getSerialCuenta()+")",montoEfectivo,cuentaDestino.getBalance()));
    }
}


