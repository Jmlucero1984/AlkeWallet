package org.josemalucero.app;




import org.josemalucero.dominio.usuario.ContextoUsuario;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        new AlkeWallet(new ContextoUsuario(new ConsoleInputProvider())).run();


    }






}