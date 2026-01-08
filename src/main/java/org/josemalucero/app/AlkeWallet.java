package org.josemalucero.app;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.passwords.BCryptPasswordEncoderService;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.repositorios.RepositorioMonedas;
import org.josemalucero.servicio.repositorios.RepositorioUsuarios;


public class AlkeWallet {


    protected final ContextoUsuario contextoUsuario;
    public static boolean onConsole;

    public AlkeWallet(ContextoUsuario contextoUsuario,boolean runningInConsole,boolean createBasicEntities) {
        this.contextoUsuario = contextoUsuario;
        onConsole = runningInConsole;
        if(createBasicEntities) {
            RepositorioMonedas.crearMonedasBasicas();
            RepositorioUsuarios.createSomeUsers();
        }


    }

    public void procesarOpcion(String opcion) {
        contextoUsuario.procesarOpcion(opcion);
    }

    /**
     * Da inicio a la apliación que continua en un bucle while
     * manejando los distintos estados a través de lo que se ingresa
     * por consola. Solo se termina el programa cuando desde alguno de
     * los estados de llama a {@code System.exit()}
     * @author José Maria Lucero
     */
    public void run() {

        contextoUsuario.getOuputProvider().println("\n"+ Messages.get("bienvenido.a.alke.wallet"));

        while (true) {
                contextoUsuario.mostrarInformacionContextual();
                String opcion = contextoUsuario.getConsoleInputProvider().leerOpcionString();
                procesarOpcion(opcion);

        }
    }





}
