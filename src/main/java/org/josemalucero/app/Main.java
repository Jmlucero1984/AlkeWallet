package org.josemalucero.app;




import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.ConsoleInputProvider;
import org.josemalucero.servicio.ConsoleOutputProvider;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        String string;

         runApp();


    }
    /**
     * Se instancia la clase AlkeWallet pasando como argumentos un objeto
     * que implementa la interfaz {@link org.josemalucero.servicio.InputProvider}
     * (para controlar lo que el
     * programa recibiría como entrada por teclado) y otro objeto que implementa
     * la interfaz {@link org.josemalucero.servicio.OutputProvider}, para hacer lo propio con lo
     * que se imprimiría en la consola.
     * @author José Maria Lucero
     */
    private static void runApp(){
        new AlkeWallet(new ContextoUsuario(new ConsoleInputProvider(), new ConsoleOutputProvider())).run();
    }






}