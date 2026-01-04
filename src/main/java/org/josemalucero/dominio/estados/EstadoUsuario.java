package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.usuario.ContextoUsuario;

public interface EstadoUsuario {

    void mostrarMenu(ContextoUsuario contextoUsuario);

    void procesarOpcion(int opcion, ContextoUsuario contextoUsuario);

    String getNombreEstado();

}
