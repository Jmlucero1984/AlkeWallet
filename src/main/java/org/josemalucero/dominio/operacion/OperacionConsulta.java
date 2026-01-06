package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.servicio.OutputProvider;

/** Permite obtener el saldo actual de la {@link CuentaRegular} asociada, mostrando además el tipo de moneda.
 * @author José Maria Lucero
 */
public class OperacionConsulta extends Operacion{

    public OperacionConsulta(CuentaRegular cuentaRegular, OutputProvider outputProvider) {
        super(cuentaRegular, outputProvider);
    }

    /**
     * Muestra al usuario el tipo de moneda de la cuenta y el saldo actual, mediante el {@link OutputProvider}.
     */
    @Override
    public void ejecutar() {
        outputProvider.println("EL SALDO DE LA CUENTA EN "+cuentaRegular.getMonedaConvertible().getNombre().toUpperCase()+" ES :");
        outputProvider.println(cuentaRegular.getBalance()+" "+cuentaRegular.getMonedaConvertible().getCodigo());

    }


}
