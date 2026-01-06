package org.josemalucero.dominio.usuario;

import org.josemalucero.servicio.ConsoleOutputProvider;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.dominio.estados.EstadoEntrada;
import org.josemalucero.dominio.estados.EstadoLogin;
import org.josemalucero.dominio.estados.EstadoUsuario;
import org.josemalucero.servicio.OutputProvider;

/**
 * Esta clase representa lo que el contexto de la sesión del usuario activo necesita para su correcta experiencia.
 * @author Jose María Lucero
 */
public class ContextoUsuario {
    private EstadoUsuario estadoActual;
    private Usuario usuarioLogueado;
    private InputProvider consoleInputProvider;
    private OutputProvider consoleOutputProvider;

    public ContextoUsuario(InputProvider consoleInputProvider, OutputProvider consoleOutputProvider) {
        // Estado inicial: Login

        this.consoleInputProvider = consoleInputProvider;
        this.consoleOutputProvider = consoleOutputProvider;
        this.estadoActual = new EstadoEntrada(consoleInputProvider, consoleOutputProvider);

    }

    public OutputProvider getOuputProvider(){
        return consoleOutputProvider;
    }

    public InputProvider getConsoleInputProvider(){
        return this.consoleInputProvider;
    }

    public EstadoUsuario getEstadoActual() {
        return this.estadoActual;
    }

    public void cambiarEstado(EstadoUsuario nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }

    /**
     * Para cada estado durante el ciclo de vida de la actividad del usuario en la plataforma muestra la información
     * necesaria para la toma de decisiones.
     */
    public void mostrarMenu() {
        consoleOutputProvider.println("\n=== " + estadoActual.getNombreEstado() + " ===");
        estadoActual.mostrarMenu(this);
    }

    /**
     * Para cada estado durante el ciclo de vida de la actividad del usuario en la plataforma,
     * procesa las elecciones del usuario de acuerdo a lo que se muestra por {@link #mostrarMenu()}.
     * @param opcion
     */
    public void procesarOpcion(int opcion) {
        estadoActual.procesarOpcion(opcion, this);
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    /**
     * Elimina lo relacionado con el, hasta el momento, usuario logueado y activo. Luego
     * deriva en el estado primigenio de la app, {@link EstadoLogin}
     */
    public void cerrarSesion() {
        this.usuarioLogueado = null;
        cambiarEstado(new EstadoLogin(consoleInputProvider,consoleOutputProvider));
    }
}