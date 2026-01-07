package org.josemalucero.dominio.operacion;


import org.josemalucero.dominio.cuenta.CuentaRegular;

import org.josemalucero.servicio.providers.OutputProvider;

import java.math.BigDecimal;

/** Permite la realización de transferencias de montos {@link BigDecimal} de una cuenta a otra, ambas con la misma moneda.
 * @author Jose María Lucero
 */
public class OperacionTransferencia extends OperacionDeMonto implements Reversible,Validable, Registrable{
    protected final CuentaRegular cuentaDestino;

    BigDecimal saldoAnteriorCuentaOrigen;
    BigDecimal saldoAnteriorCuentaDestino;


    public OperacionTransferencia (CuentaRegular cuentaOrigen, CuentaRegular cuentaDestino, BigDecimal monto, OutputProvider outputProvider) {

        super( cuentaOrigen, monto,outputProvider);
        this.cuentaDestino = cuentaDestino;

    }

    /**
     * Constructor sobrecargado para la recepción de {@link DatosTransferencia}.
     * @param datosTransferencia
     * @param outputProvider
     */
    public OperacionTransferencia (DatosTransferencia datosTransferencia, OutputProvider outputProvider) {

        super( datosTransferencia.getCuentaOrigen(), datosTransferencia.getMonto(),outputProvider);
        this.cuentaDestino = datosTransferencia.getCuentaDestino();

    }

    /**
     * Ejecuta la operación de transferencia, previo registro del estado actual, necesario al momento de restaurar las cuentas
     * involucradas al estadío previo. A continuación llama al método {@link CuentaRegular#tranfiere(BigDecimal)} de la cuenta de origen
     * por el monto indicado de la operación, y al método {@link CuentaRegular#recibeTransferencia(BigDecimal)} de la cuenta de destino por
     * el respectivo e idéntico valor.
     */
    @Override
    public void ejecutar() {
        registrarEstadoPrevio();
        cuentaRegular.tranfiere(monto);
        cuentaDestino.recibeTransferencia(monto);


    }

    @Override
    public String getNombreOperacion() {
        return "TRANSFERENCIA A OTRA CUENTA DE IGUAL MONEDA";
    }

    /**
     * Devuelve la descripción de la operacion para el registro en la cuenta destino de la transferencia.
     * @return {@link String} descripción de la operación en la cuenta destino.
     */

    public String getNombreOperacionReciproca() {
        return "TRANSFERENCIA DESDE CUENTA DE IGUAL MONEDA";
    }



    protected void registrarEstadoPrevio(){
        saldoAnteriorCuentaOrigen = cuentaRegular.getBalance();
        saldoAnteriorCuentaDestino = cuentaDestino.getBalance();
    }

    /**
     *Realiza las comprobaciones necesarias para realizar una transferencia significativa y efectiva.
     * @return {@code bool} que indica la posibilidad de ejecutar la transferencia, sea por los fondos disponibles o
     * por la introducción de una cifra monetario no trivial.
     */
    @Override
    public boolean preValidar() {
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
     * Realiza las comprobaciones comparando los saldos anteriores  de cada cuenta involucrada
     *   con sendos montos debitados y acreditados.
     *
     * @return {@code bool} que indica que la operación ha sido exitosa o ha fallado.
     */
    @Override
    public boolean postValidar() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen.subtract(monto)) == 0 &&
                cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino.add(monto)) == 0) {
            return true;

        } else {
            outputProvider.println("HA FALLADO LA TRANSFERENCIA");
            return false;
        }
    }

    /**
     * Devuelve las cuentas involucradas en la operacion a sus estados previos a la misma, realizando
     * debitaciones y acreditaciones adecuadas.
     */
    @Override
    public void restaurarEstadoAnterior() {
        if(cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen)<0){
            cuentaRegular.depositar(saldoAnteriorCuentaOrigen.subtract(cuentaRegular.getBalance()));
        } else if (cuentaRegular.getBalance().compareTo(saldoAnteriorCuentaOrigen)>0) {
            cuentaRegular.retirar(cuentaRegular.getBalance().subtract(saldoAnteriorCuentaOrigen));
        }
        if(cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino)<0){
            cuentaDestino.depositar(saldoAnteriorCuentaDestino.subtract(cuentaDestino.getBalance()));
        } else if (cuentaDestino.getBalance().compareTo(saldoAnteriorCuentaDestino)>0) {
            cuentaDestino.retirar(cuentaDestino.getBalance().subtract(saldoAnteriorCuentaDestino));
        }


    }

    /**
     * Realiza el registro correspondiente en cada cuenta involucrada en la operación.
     * @param cuentaRegular
     */
    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        cuentaRegular.registrarOperacion(new RegistroOperacion(getNombreOperacion(),monto,cuentaRegular.getBalance()));
        cuentaDestino.registrarOperacion(new RegistroOperacion(getNombreOperacionReciproca()+" (C.N° "+cuentaRegular.getSerialCuenta()+")",monto,cuentaDestino.getBalance()));
    }
}
