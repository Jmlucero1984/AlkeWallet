package org.josemalucero.app;




import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.*;

import java.io.IOException;
import java.util.Locale;


public class Main {

    public static void main(String[] args) throws IOException {

        boolean isConsole=false;
        boolean englishLanguage=false;
        for(int i=0; i< args.length; i++){
            switch (args[i]){
                case "-c","-console","-Console","-CONSOLE","-consola","-Consola","-CONSOLA":
                    isConsole=true;
                    break;
                case "-EN","-en","-En","-English","-english","-ENGLISH","-ingles","-Ingles","-INGLES":
                    englishLanguage=true;
                    break;
            }
            
        }
        Locale locale =englishLanguage?Locale.forLanguageTag("en"):Locale.forLanguageTag("es");
        Messages.init(locale);
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
        new AlkeWallet(new ContextoUsuario(new ConsoleInputProvider(), new ConsoleOutputProvider()),runningOnConsole,true).run();
    }






}