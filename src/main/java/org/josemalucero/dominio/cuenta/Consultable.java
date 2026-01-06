package org.josemalucero.dominio.cuenta;

import javax.swing.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Las clases que implementan esta interfaz proporcionan acceso al estado actual del balance
 * como así tambien permiten obtener un identificador de cuenta.
 *  @author José Maria Lucero
 */
public interface Consultable {

    /**
     * Permite obtener el saldo actual o balance.
     * @return {@link BigDecimal} del balance actual.
     */
    BigDecimal getBalance();

    /**Permite obtener el indentificador de la cuenta.
     *
     * @return {@link String} que representa el número de cuenta.
     */
    String getNumeroCuenta();


}
