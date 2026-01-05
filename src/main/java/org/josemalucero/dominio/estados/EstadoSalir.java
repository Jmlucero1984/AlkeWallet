package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.servicio.OutputProvider;

public class EstadoSalir extends EstadoUsuario{

    public EstadoSalir(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        outputProvider.println("Esperamos vuelva pronto!");
        System.exit(0);

    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contexto) {

    }


    @Override
    public String getNombreEstado() {
        return "SALIENDO";
    }
}
