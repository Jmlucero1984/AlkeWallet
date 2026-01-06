package org.josemalucero.dominio.operacion;

/**
 * Las clases que implementan esta interfaz proveen métodos para hacer validaciones antes y despues de una operación.
 * @author Jose María Lucero
 */
public interface Validable {
    /**
     * Realiza la validación previa a una operacion.
     * @return {@code bool} que indica la posibilidad de ejecutar una determinada operación.
     */
    boolean preValidar();

    /**
     * Realiza la validación posterior a una opreación.
     * @return {@code bool} que comprueba el resultado de ejecutar una determinada operación.
     */

    boolean postValidar();
}
