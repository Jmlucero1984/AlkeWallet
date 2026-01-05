package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OperacionTransferenciaMonedaDestino extends OperacionTransferencia{
    BigDecimal montoEfectivo;
    ConversorMoneda conversorMoneda;
    public OperacionTransferenciaMonedaDestino(CuentaRegular cuentaOrigen, CuentaRegular cuentaDestino, BigDecimal monto, ConversorMoneda conversorMoneda, OutputProvider outputProvider) {
        super(cuentaOrigen, cuentaDestino, monto,outputProvider);
        this.conversorMoneda = conversorMoneda;
        montoEfectivo = convertir(cuentaDestino.getMonedaConvertible(),cuentaOrigen.getMonedaConvertible(),monto);

    }
    public OperacionTransferenciaMonedaDestino(DatosTransferencia datosTransferencia, OutputProvider outputProvider) {
        super(datosTransferencia.getCuentaOrigen(), datosTransferencia.getCuentaDestino(), datosTransferencia.getMonto(),outputProvider);
        this.conversorMoneda = datosTransferencia.getConversorMoneda();
        montoEfectivo = convertir(datosTransferencia.getCuentaDestino().getMonedaConvertible(),datosTransferencia.getCuentaOrigen().getMonedaConvertible(),datosTransferencia.getMonto());

    }

    @Override
    public void ejecutar() {
        outputProvider.println("EJECUTANDO TRANSFERENCIA EN MONEDA DE DESTINO");
        super.registrarEstadoPrevio();
        cuentaRegular.tranfiere(montoEfectivo);
        cuentaDestino.recibeTransferencia(monto);

    }
    public String getNombreOperacion() {
        return "TRANSFERENCIA A CUENTA DE DISTINTA MONEDA";
    }

    @Override
    public String getNombreOperacionReciproca() {
        return "TRANSFERENCIA DESDE CUENTA DE DISTINTA MONEDA";
    }

    public BigDecimal getMontoEfectivo(){
        return montoEfectivo;
    }

    private BigDecimal convertir(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto) {
        // ORIGINAL:
        // destino.getRatioDolar().divide(origen.getRatioDolar(),10, RoundingMode.HALF_UP).multiply(monto).setScale(2,RoundingMode.HALF_UP);
        // FUNCTION
        // monedaOrigen.getRatioDolar().divide(monedaOrigen.getRatioDolar(),10, RoundingMode.HALF_UP).multiply(monto).setScale(2,RoundingMode.HALF_UP);
        return conversorMoneda.convertirMoneda( origen,  destino,  monto);


    }

    @Override
    public boolean preValidar() {
        //valorMoneda.multiply(ratio).setScale(2, RoundingMode.HALF_UP));


        if (monto.compareTo(BigDecimal.ZERO)==0) {
            outputProvider.println("No se puede realizar transferencia por monto igual a 0");
            return false;
        }
        if(montoEfectivo.compareTo(cuentaRegular.getBalance())<=0){
            return true;

        } else {
            outputProvider.println("No se puede tranferir la cantidad solicitada. FONDOS INSUFICIENTES");
            return false;
        }
    }

    @Override
    public boolean postValidar() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.subtract(montoEfectivo)) == 0 &&
                cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino.add(monto)) == 0) {
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
        cuentaRegular.registrarOperacion(new RegistroOperacion(getNombreOperacion(),montoEfectivo,cuentaRegular.getBalance()));
        cuentaDestino.registrarOperacion(new RegistroOperacion(getNombreOperacionReciproca()+" (C.N° "+cuentaRegular.getSerialCuenta()+")",monto,cuentaDestino.getBalance()));
    }
}

