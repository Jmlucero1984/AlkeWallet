package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

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
        outputProvider.println(Messages.get("ejecutando.transferencia.en.moneda.de.destino"));
        super.registrarEstadoPrevio();
        cuentaRegular.tranfiere(montoEfectivo);
        cuentaDestino.recibeTransferencia(monto);

    }
    public String getNombreOperacion() {
        return Messages.get("operacion.transferencia.a.cuenta.distinta.moneda");
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getNombreOperacionReciproca() {
        return Messages.get("operacion.transferencia.desde.cuenta.distinta.moneda");
    }

    public BigDecimal getMontoEfectivo(){
        return montoEfectivo;
    }

    private BigDecimal convertir(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto) {
        return conversorMoneda.convertirMoneda( origen,  destino,  monto);


    }

    /**
     *Realiza las comprobaciones necesarias para realizar una transferencia significativa y efectiva.
     * @return {@code bool} que indica la posibilidad de ejecutar la transferencia, sea por los fondos disponibles o
     * por la introducción de una cifra monetario no trivial. Asi mismo verifica que el monto no tenga restricciones
     * bancarias o fiscales.
     */
    @Override
    public boolean preValidar() {



        if (monto.compareTo(BigDecimal.ZERO)==0) {
            outputProvider.printlnAlert(Messages.get("alert.no.transferir.monto.nulo"));
            return false;
        }
        if(montoEfectivo.compareTo(cuentaRegular.getBalance())<=0){
            if(montoEfectivo.compareTo(ConstantesFiscalesBancarias.LIMITE_MONTO_TRANSFERENCIA)>0){
                outputProvider.printlnAlert(Messages.get("alerta.no.se.puede.transferir.cantidad.limite.sii"));
                outputProvider.printInfoln(Messages.get("limite.transferencia.por.operacion")+": "+ConstantesFiscalesBancarias.LIMITE_MONTO_TRANSFERENCIA);
                return false;
            }
            return true;

        } else {
            outputProvider.printlnAlert(Messages.get("alerta.no.se.puede.transferir.cantidad")+". "+Messages.get("alerta.fondos.insuficientes"));
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
            outputProvider.printlnAlert(Messages.get("alerta.ha.fallado.la.transferencia"));
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
        cuentaDestino.registrarOperacion(new RegistroOperacion(getNombreOperacionReciproca()+" ("+Messages.get("c.n")+" "+cuentaRegular.getSerialCuenta()+")",monto,cuentaDestino.getBalance()));
    }
}

