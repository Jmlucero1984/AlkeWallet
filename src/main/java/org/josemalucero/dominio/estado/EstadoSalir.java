package org.josemalucero.dominio.estado;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.OutputProvider;

/**
 * Tiene como única y trivial responsabilidad terminar el programa, mostrando un mensaje de despedida.
 *
 @author José Maria Lucero
 */
public class EstadoSalir extends EstadoUsuario{

    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoSalir(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**
     * Muestra simplemente un mensaje de despedida y da término al programa.
     * @param contextoUsuario
     */
    @Override
    public void mostrarMenu(ContextoUsuario contextoUsuario) {
        outputProvider.println("─────────────────────────────────");
        outputProvider.println("♦  Developed by Jose Ma Lucero  ♦");
        System.exit(0);

    }

    /**
     * Método sin efecto alguno por tratarse del estado último y terminal del programa.
     * @param opcion
     * @param contexto
     */
    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contexto) {


    }

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return "¡Esperamos vuelva pronto!";
    }
}
