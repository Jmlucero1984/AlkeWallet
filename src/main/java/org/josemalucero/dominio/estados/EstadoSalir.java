package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.usuario.ContextoUsuario;

public class EstadoSalir implements EstadoUsuario{
    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        System.out.println("Esperamos vuelva pronto!");
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
