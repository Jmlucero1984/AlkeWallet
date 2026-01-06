package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;

/** Permite la realización de transferencias de montos {@link BigDecimal} de una cuenta a otra, pero indicando que la suma
 * indicada está en términos de la moneda de la cuenta de destino. Por lo que el monto que impactará sobre la propia cuenta
 * deberá obtenerse previamente por medio de una conversión.
 * @author Jose María Lucero
 */
public class OperacionTransferenciaMonedaDestino extends OperacionTransferencia{

    BigDecimal montoEfectivo;
    ConversorMoneda conversorMoneda;


    public OperacionTransferenciaMonedaDestino(CuentaRegular cuentaOrigen, CuentaRegular cuentaDestino, BigDecimal monto, ConversorMoneda conversorMoneda, OutputProvider outputProvider) {
        super(cuentaOrigen, cuentaDestino, monto,outputProvider);
        this.conversorMoneda = conversorMoneda;
        montoEfectivo = convertir(cuentaDestino.getMonedaConvertible(),cuentaOrigen.getMonedaConvertible(),monto);

    }

    /**
     * Constructor sobrecargado para la recepción de {@link DatosTransferencia}.
     * <p>
     * El <b>montoEfectivo</b> representa en moneda de la propia cuenta el monto de la operación de transferencia
     * que se indicó como siendo en términos de la cuenta de destino.
     * </p>
     * @param datosTransferencia
     * @param outputProvider
     */
    public OperacionTransferenciaMonedaDestino(DatosTransferencia datosTransferencia, OutputProvider outputProvider) {
        super(datosTransferencia.getCuentaOrigen(), datosTransferencia.getCuentaDestino(), datosTransferencia.getMonto(),outputProvider);
        this.conversorMoneda = datosTransferencia.getConversorMoneda();
        montoEfectivo = convertir(datosTransferencia.getCuentaDestino().getMonedaConvertible(),datosTransferencia.getCuentaOrigen().getMonedaConvertible(),datosTransferencia.getMonto());

    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getNombreOperacionReciproca() {
        return "TRANSFERENCIA DESDE CUENTA DE DISTINTA MONEDA";
    }

    public BigDecimal getMontoEfectivo(){
        return montoEfectivo;
    }

    private BigDecimal convertir(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto) {
        return conversorMoneda.convertirMoneda( origen,  destino,  monto);


    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void restaurarEstadoAnterior() {
        super.restaurarEstadoAnterior();
    }

    /**
     * Realiza el registro correspondiente en cada cuenta involucrada en la operación.
     * @param cuentaRegular
     */
    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        cuentaRegular.registrarOperacion(new RegistroOperacion(getNombreOperacion(),montoEfectivo,cuentaRegular.getBalance()));
        cuentaDestino.registrarOperacion(new RegistroOperacion(getNombreOperacionReciproca()+" (C.N° "+cuentaRegular.getSerialCuenta()+")",monto,cuentaDestino.getBalance()));
    }
}

