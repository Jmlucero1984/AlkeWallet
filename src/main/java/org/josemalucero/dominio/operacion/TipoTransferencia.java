package org.josemalucero.dominio.operacion;

/**
 * Un {@link Enum} que indica los tres tipos posibles de transferencias entre cuentas, sean ambas sobre la misma moneda
 * o cada una con moneda distinta.
 * @author Jose María Lucero
 */
public enum TipoTransferencia {
    UNDEFINED,
    IGUAL_MONEDA,
    DISTINTA_MONEDA,
    MONEDA_ORIGEN,
    MONEDA_DESTINO
}
