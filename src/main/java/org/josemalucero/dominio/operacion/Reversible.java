package org.josemalucero.dominio.operacion;

/**
 * Las clases que implementan esta interfaz proveen un método para revertir la situación actual a una anterior.
 *  @author Jose María Lucero
 */
public interface Reversible {
    /**
     * Realiza la restauración a un estado anterior.
     */
    void restaurarEstadoAnterior();
}

