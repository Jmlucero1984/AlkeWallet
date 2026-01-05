package org.josemalucero.dominio.operacion;

public interface Validable {
    boolean preValidar();

    boolean postValidar();
}
