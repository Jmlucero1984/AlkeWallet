package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.MonedaConvertible;

import java.math.BigDecimal;

public class OperacionDeConversionDeCuenta extends Operacion implements Registrable{
    MonedaConvertible monedaDestino;
    BigDecimal balanceAntesDeConversion;
    MonedaConvertible monedaAntesDeConversion;
    public OperacionDeConversionDeCuenta(CuentaRegular cuentaRegular, MonedaConvertible monedaDestino) {
        super(cuentaRegular);
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
