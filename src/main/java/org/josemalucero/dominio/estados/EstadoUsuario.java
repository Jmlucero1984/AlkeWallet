package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.usuario.ContextoUsuario;

public interface EstadoUsuario {
    void mostrarMenu(ContextoUsuario contexto);
    void procesarOpcion(int opcion, ContextoUsuario contexto);
    String getNombreEstado();
}
