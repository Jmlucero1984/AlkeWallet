package org.josemalucero.dominio.usuario;

import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.dominio.estado.EstadoInicio;
import org.josemalucero.dominio.estado.EstadoLogin;
import org.josemalucero.dominio.estado.EstadoUsuario;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

/**
 * Esta clase representa lo que el contexto de la sesión del usuario activo necesita para su correcta experiencia.
 * @author Jose María Lucero
 */
public class ContextoUsuario {
    private EstadoUsuario estadoActual;
    private Usuario usuarioLogueado;
    private InputProvider consoleInputProvider;
    private OutputProvider consoleOutputProvider;
    private int transferencias_por_sesion=0;
    private int depositos_por_sesion=0;
    private int retiros_por_sesion=0;


    public ContextoUsuario(InputProvider consoleInputProvider, OutputProvider consoleOutputProvider) {
        // Estado inicial: Login

        this.consoleInputProvider = consoleInputProvider;
        this.consoleOutputProvider = consoleOutputProvider;
        this.estadoActual = new EstadoInicio(consoleInputProvider, consoleOutputProvider);

    }
    public int getTransferencias_por_sesion() {
        return transferencias_por_sesion;
    }

    public int getDepositos_por_sesion() {
        return depositos_por_sesion;
    }

    public int getRetiros_por_sesion() {
        return retiros_por_sesion;
    }
    public void reset_operaciones_por_sesion(){
        depositos_por_sesion=0;
        retiros_por_sesion=0;
        transferencias_por_sesion=0;
    }


    public void incrementar_retiros_por_session(){
        retiros_por_sesion++;
    }
    public void incrementar_depositos_por_sesion(){
        depositos_por_sesion++;
    }
    public void incrementar_transferencias_por_sesion(){
        transferencias_por_sesion++;
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
    public void mostrarInformacionContextual() {
        consoleOutputProvider.println("\n=== " + estadoActual.getNombreEstado() + " ===");
        estadoActual.mostrarInformaciónContextual(this);
    }

    /**
     * Para cada estado durante el ciclo de vida de la actividad del usuario en la plataforma,
     * procesa las elecciones del usuario de acuerdo a lo que se muestra por {@link #mostrarInformacionContextual()}.
     * @param opcion
     */
    public void procesarOpcion(String opcion) {
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

    /**
     * Espera un la confirmación del usuario para continuar.
     */
    public void confirmaContinuar(){
        consoleOutputProvider.println("\n"+ Messages.get("presione.enter.para.continuar"));
        consoleInputProvider.leerOpcionString();
    }
}