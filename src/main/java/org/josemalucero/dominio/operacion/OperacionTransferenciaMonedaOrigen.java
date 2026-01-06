package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;

/** Permite la realización de transferencias de montos {@link BigDecimal} de una cuenta a otra, pero indicando que la suma
 * indicada está en términos de la moneda de la cuenta de origen, por lo que la cuenta de destino recibirá un monto que previamente
 * deberá ser obtenido mediante una conversión.
 * @author Jose María Lucero
 */
public class OperacionTransferenciaMonedaOrigen extends OperacionTransferencia{
    BigDecimal montoEfectivo;
    ConversorMoneda conversorMoneda;
    public OperacionTransferenciaMonedaOrigen(CuentaRegular cuentaOrigen, CuentaRegular cuentaDestino, BigDecimal monto, ConversorMoneda conversorMoneda, OutputProvider outputProvider) {
        super(cuentaOrigen, cuentaDestino, monto, outputProvider);
        this.conversorMoneda = conversorMoneda;
        montoEfectivo = convertir(cuentaOrigen.getMonedaConvertible(),cuentaDestino.getMonedaConvertible(),monto);

    }

    /**
     * Constructor sobrecargado para la recepción de {@link DatosTransferencia}.
     * <p>
     * El <b>montoEfectivo</b> representa en moneda de la cuenta de destino el equivalente al monto ingresado, que fué especificado
     * sobre la base de la moneda de la propia cuenta.
     * </p>
     * @param datosTransferencia
     * @param outputProvider
     */
    public OperacionTransferenciaMonedaOrigen(DatosTransferencia datosTransferencia, OutputProvider outputProvider) {
        super(datosTransferencia.getCuentaOrigen(), datosTransferencia.getCuentaDestino(), datosTransferencia.getMonto(), outputProvider);
        montoEfectivo = convertir(datosTransferencia.getCuentaOrigen().getMonedaConvertible(),datosTransferencia.getCuentaDestino().getMonedaConvertible(),datosTransferencia.getMonto());

    }

    @Override
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void ejecutar() {
        outputProvider.println("EJECUTANDO TRANSFERENCIA EN MONEDA DE ORIGEN");
        super.registrarEstadoPrevio();
        cuentaRegular.tranfiere(monto);
        cuentaDestino.recibeTransferencia(montoEfectivo);

    }

    private BigDecimal convertir(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto) {
        return conversorMoneda.convertirMoneda(origen, destino, monto);

    }

    public BigDecimal getMontoEfectivo(){
        return montoEfectivo;
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
        if(monto.compareTo(cuentaRegular.getBalance())<=0){
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
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.subtract(monto)) == 0 &&
                cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino.add(montoEfectivo)) == 0) {
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
     *{@inheritDoc}
     */
    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        cuentaRegular.registrarOperacion(new RegistroOperacion(getNombreOperacion(),monto,cuentaRegular.getBalance()));
        cuentaDestino.registrarOperacion(new RegistroOperacion(getNombreOperacionReciproca()+" (C.N° "+cuentaRegular.getSerialCuenta()+")",montoEfectivo,cuentaDestino.getBalance()));
    }
}


