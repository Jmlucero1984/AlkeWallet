package org.josemalucero.servicio;


/**
 *
 * <p>
 * Esta interfaz establece los métodos que permiten proveer
 * las respuestas que se deben ingresar por consola.
 * </p>
 *
 * @author José María Lucero
 */
public interface InputProvider {
    int leerOpcionInt();
    String leerOpcionString();

}
