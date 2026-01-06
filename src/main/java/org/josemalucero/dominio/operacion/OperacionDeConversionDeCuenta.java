package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;

/**
 * Permite realizar la conversión de la cuenta, desde la moneda actual a otra moneda de destino, haciendo
 * lo propio con el saldo existente y registrando la operación en el historial de la cuenta.
 * @author José Maria Lucero
 */
public class OperacionDeConversionDeCuenta extends Operacion implements Registrable{
    MonedaConvertible monedaDestino;
    BigDecimal balanceAntesDeConversion;
    MonedaConvertible monedaAntesDeConversion;
    public OperacionDeConversionDeCuenta(CuentaRegular cuentaRegular, MonedaConvertible monedaDestino, OutputProvider outputProvider) {
        super(cuentaRegular,outputProvider);
        this.monedaDestino = monedaDestino;
        monedaAntesDeConversion=cuentaRegular.getMonedaConvertible();
        balanceAntesDeConversion = cuentaRegular.getBalance();
    }

    @Override
    public void ejecutar() {
        cuentaRegular.convertirAMoneda(monedaDestino);

    }
    @Override
    public String getNombreOperacion() {
        return "CONVERSIÓN DE CUENTA DE "+monedaAntesDeConversion.getCodigo()+" A "+monedaDestino.getCodigo();
    }


    @Override
    public void registrar(CuentaRegular cuentaRegular) {
        cuentaRegular.registrarOperacion(new RegistroOperacion(getNombreOperacion(),balanceAntesDeConversion,cuentaRegular.getBalance()));
    }
}
