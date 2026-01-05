package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.servicio.OutputProvider;

public abstract class EstadoUsuario {

    protected InputProvider inputProvider;
    protected OutputProvider outputProvider;

    public EstadoUsuario(InputProvider inputProvider, OutputProvider outputProvider) {
        this.inputProvider = inputProvider;
        this.outputProvider = outputProvider;
    }

    public abstract void mostrarMenu(ContextoUsuario contextoUsuario);

    public abstract void procesarOpcion(int opcion, ContextoUsuario contextoUsuario);

    public abstract String getNombreEstado();

}
