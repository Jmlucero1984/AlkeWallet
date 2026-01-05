package org.josemalucero.app;




import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.ConsoleInputProvider;
import org.josemalucero.servicio.ConsoleOutputProvider;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        new AlkeWallet(new ContextoUsuario(new ConsoleInputProvider(), new ConsoleOutputProvider())).run();


    }






}