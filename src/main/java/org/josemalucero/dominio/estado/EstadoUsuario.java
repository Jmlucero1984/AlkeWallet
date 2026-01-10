package org.josemalucero.dominio.estado;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

import java.math.BigDecimal;


/**
 Representa la entidad básica que modela los diferentes estados posibles del sistema durante la actividad del usuario en
 la plataforma.
 @author José Maria Lucero
 */
public abstract class EstadoUsuario  {

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
    public abstract void mostrarInformaciónContextual(ContextoUsuario contextoUsuario);

    /**
     * Permitirá realizar operaciones de acuerdo a lo elegido por el usuario en cada estado particular durante la actividad en
     * la plataforma.
     * @param opcion
     * @param contextoUsuario
     */
    public abstract void procesarOpcion(String opcion, ContextoUsuario contextoUsuario);

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    public abstract String getNombreEstado();

    protected BigDecimal verificarCifraMonetaria(String cifraMonetaria) {

        if (cifraMonetaria.matches("^\\d+\\.\\d{2}$")) {
            return new BigDecimal(cifraMonetaria);
        }
        if (cifraMonetaria.matches("^-\\d+\\.\\d{2}$")) {
            outputProvider.printlnAlert(Messages.get("alerta.no.puede.ingresar.numeros.negativos") +". "+Messages.get("intente.nuevamente"));
            return null;

        }
        outputProvider.printlnAlert(Messages.get("alerta.cantidad.invalida")+". "+Messages.get("intente.nuevamente"));

       return null;
    }

}
