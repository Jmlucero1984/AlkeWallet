package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

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

        return Messages.get("operacion.transferencia.a.cuenta.distinta.moneda");

    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getNombreOperacionReciproca(){ return Messages.get("operacion.transferencia.desde.cuenta.distinta.moneda");};

    /**
     * {@inheritDoc}
     */
    @Override
    public void ejecutar() {
        outputProvider.println(Messages.get("ejecutando.transferencia.en.moneda.de.origem"));
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
     *Realiza las comprobaciones necesarias para realizar una transferencia significativa y efectiva.
     * @return {@code bool} que indica la posibilidad de ejecutar la transferencia, sea por los fondos disponibles o
     * por la introducción de una cifra monetario no trivial. Asi mismo verifica que el monto no tenga restricciones
     * bancarias o fiscales.
     */
    @Override
    public boolean preValidar() {
        //valorMoneda.multiply(ratio).setScale(2, RoundingMode.HALF_UP));


        if (monto.compareTo(BigDecimal.ZERO)==0) {
            outputProvider.println(Messages.get("alert.no.transferir.monto.nulo"));
            return false;
        }
        if(monto.compareTo(cuentaRegular.getBalance())<=0){
            if(monto.compareTo(ConstantesFiscalesBancarias.LIMITE_MONTO_TRANSFERENCIA)>0){
                outputProvider.println(Messages.get("alerta.no.se.puede.transferir.cantidad.limite.sii"));
                return false;
            }
            return true;

        } else {
            outputProvider.println(Messages.get("no.se.puede.transferir.cantidad")+". "+Messages.get("fondos.insuficientes"));
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
            outputProvider.println(Messages.get("ha.fallado.la.transferencia"));
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
        cuentaDestino.registrarOperacion(new RegistroOperacion(getNombreOperacionReciproca()+" ("+Messages.get("c.n")+" "+cuentaRegular.getSerialCuenta()+")",montoEfectivo,cuentaDestino.getBalance()));
    }
}


