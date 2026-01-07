package org.josemalucero.app;




import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.ConsoleInputProvider;
import org.josemalucero.servicio.providers.ConsoleOutputProvider;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.OutputProvider;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        boolean isConsole = args.length > 0 && args[0].equalsIgnoreCase("console");
         runApp(isConsole);


    }
    /**
     * Se instancia la clase AlkeWallet pasando como argumentos un objeto
     * que implementa la interfaz {@link InputProvider}
     * (para controlar lo que el
     * programa recibiría como entrada por teclado) y otro objeto que implementa
     * la interfaz {@link OutputProvider}, para hacer lo propio con lo
     * que se imprimiría en la consola.
     * @author José Maria Lucero
     */
    private static void runApp(boolean runningOnConsole){
        new AlkeWallet(new ContextoUsuario(new ConsoleInputProvider(), new ConsoleOutputProvider()),runningOnConsole).run();
    }






}