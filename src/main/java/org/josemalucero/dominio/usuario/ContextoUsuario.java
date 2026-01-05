package org.josemalucero.dominio.usuario;

import org.josemalucero.servicio.ConsoleOutputProvider;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.dominio.estados.EstadoEntrada;
import org.josemalucero.dominio.estados.EstadoLogin;
import org.josemalucero.dominio.estados.EstadoUsuario;
import org.josemalucero.servicio.OutputProvider;

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

    public void mostrarMenu() {

        consoleOutputProvider.println("\n=== " + estadoActual.getNombreEstado() + " ===");
        estadoActual.mostrarMenu(this);
    }

    public void procesarOpcion(int opcion) {
        estadoActual.procesarOpcion(opcion, this);
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
        cambiarEstado(new EstadoLogin(consoleInputProvider,consoleOutputProvider));
    }
}