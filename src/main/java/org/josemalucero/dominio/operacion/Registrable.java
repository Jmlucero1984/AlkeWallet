package org.josemalucero.dominio.operacion;

import org.josemalucero.dominio.cuenta.CuentaRegular;

/**
 * Las clases que implementan esta interfaz proveen un método para realizar el registro de las operaciones en una {@link CuentaRegular}.
 * @author Jose María Lucero
 */

public interface Registrable {
    /**
     * Realiza el registro en la {@link CuentaRegular}.
     * @param cuentaRegular
     */
    void registrar(CuentaRegular cuentaRegular);
}
