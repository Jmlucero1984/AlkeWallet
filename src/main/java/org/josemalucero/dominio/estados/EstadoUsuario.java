package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.servicio.OutputProvider;




/**
 Representa la entidad básica que modela los diferentes estados posibles del sistema durante la actividad del usuario en
 la plataforma.
 @author José Maria Lucero
 */
public abstract class EstadoUsuario {

    protected InputProvider inputProvider;
    protected OutputProvider outputProvider;
    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoUsuario(InputProvider inputProvider, OutputProvider outputProvider) {
        this.inputProvider = inputProvider;
        this.outputProvider = outputProvider;
    }

    /**
     * Mostrará cualquier tipo de información que el usuario requiera para poder tomar decisiones sobre como proceder
     * en la plataforma.
     * @param contextoUsuario
     */
    public abstract void mostrarMenu(ContextoUsuario contextoUsuario);

    /**
     * Permitirá realizar operaciones de acuerdo a lo elegido por el usuario en cada estado particular durante la actividad en
     * la plataforma.
     * @param opcion
     * @param contextoUsuario
     */
    public abstract void procesarOpcion(int opcion, ContextoUsuario contextoUsuario);

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    public abstract String getNombreEstado();

}
