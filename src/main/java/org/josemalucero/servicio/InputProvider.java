package org.josemalucero.servicio;


/**
 *
Las clases que implementan esta interfaz proveen los métodos para leer los datos ingresados por el usuario.
 * @author José María Lucero
 */
public interface InputProvider {
    /**
     * Permite leer un dato numérico entero ingresado por el usuario.
     * @return {@code int} dato numérico ingresado por usuario.
     */
    int leerOpcionInt();

    /**
     * Permite leer un dato alfanumérico ingresado por usuario.
     * @return  {@link String} dato alfanumérico ingresado por usuario.
     */
    String leerOpcionString();

}
