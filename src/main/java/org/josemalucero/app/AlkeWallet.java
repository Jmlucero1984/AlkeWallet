package org.josemalucero.app;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.BCryptPasswordEncoderService;
import org.josemalucero.servicio.RepositorioMonedas;
import org.josemalucero.servicio.RepositorioUsuarios;


public class AlkeWallet {


    protected final ContextoUsuario contextoUsuario;
    public static boolean onConsole;

    public AlkeWallet(ContextoUsuario contextoUsuario,boolean runningInConsole ) {
        this.contextoUsuario = contextoUsuario;
        onConsole = runningInConsole;
        RepositorioMonedas.crearMonedasBasicas();
        createSomeUsers();

    }

    public void procesarOpcion(int opcion) {
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



        while (true) {
            try {
                contextoUsuario.mostrarMenu();
                int opcion = contextoUsuario.getConsoleInputProvider().leerOpcionInt();

                procesarOpcion(opcion);

            } catch (Exception e) {
                contextoUsuario.getOuputProvider().println("Introduzca una opción válida");
                contextoUsuario.getConsoleInputProvider().leerOpcionString();
            }
        }
    }

    /**
     * Genera algunos usuarios ficticios para poder
     * hacer uso de la app con una base mínima.
     * @author José Maria Lucero
     */
       private void createSomeUsers() {
            BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
            RepositorioUsuarios.agregarUsuarioYAsignarCuenta("Jose", "Lucero", bCryptPasswordEncoderService.hash("Joselucero"),"ARS");
            RepositorioUsuarios.agregarUsuarioYAsignarCuenta("Mario", "Moya", bCryptPasswordEncoderService.hash("Mariomoya"),"CLP");
            RepositorioUsuarios.agregarUsuarioYAsignarCuenta("Javiera", "Rojas", bCryptPasswordEncoderService.hash("Javierarojas"),"CLP");
        }


}
