package org.josemalucero.dominio.usuario;

import org.josemalucero.dominio.estados.EstadoLogin;
import org.josemalucero.dominio.estados.EstadoUsuario;

import java.util.Scanner;

public class ContextoUsuario {
    private EstadoUsuario estadoActual;
    private Usuario usuarioLogueado;
    private Scanner scanner;

    public ContextoUsuario(Scanner scanner) {
        // Estado inicial: Login
        this.estadoActual = new EstadoLogin();
        this.scanner = scanner;

    }

    public Scanner getScanner(){
        return this.scanner;
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