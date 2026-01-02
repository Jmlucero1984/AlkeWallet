package org.josemalucero.dominio.usuario;

import org.josemalucero.app.ConsoleInputProvider;
import org.josemalucero.app.InputProvider;
import org.josemalucero.dominio.estados.EstadoEntrada;
import org.josemalucero.dominio.estados.EstadoLogin;
import org.josemalucero.dominio.estados.EstadoUsuario;

import java.util.Scanner;

public class ContextoUsuario {
    private EstadoUsuario estadoActual;
    private Usuario usuarioLogueado;
    private InputProvider consoleInputProvider;

    public ContextoUsuario(InputProvider consoleInputProvider) {
        // Estado inicial: Login
        this.estadoActual = new EstadoEntrada();
        this.consoleInputProvider = consoleInputProvider;

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

        System.out.println("\n=== " + estadoActual.getNombreEstado() + " ===");
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
        cambiarEstado(new EstadoLogin());
    }
}