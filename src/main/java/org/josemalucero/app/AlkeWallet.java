package org.josemalucero.app;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.BCryptPasswordEncoderService;
import org.josemalucero.servicio.RepositorioMonedas;
import org.josemalucero.servicio.RepositorioUsuarios;


public class AlkeWallet {


    protected final ContextoUsuario contexto;

    public AlkeWallet(ContextoUsuario contexto) {
        this.contexto = contexto;
        RepositorioMonedas.crearMonedasBasicas();
        createSomeUsers();

    }

    public void procesarOpcion(int opcion) {
        contexto.procesarOpcion(opcion);
    }

    /**
     * Da inicio a la apliación que continua en un bucle while
     * manejando los distintos estados a través de lo que se ingresa
     * por consola. Solo se termina el programa cuando desde alguno de
     * los estados de llama a {@code System.exit()}
     *
     */
    public void run() {

        System.out.println("=== BIENVENIDO A BILLETERA VIRTUAL ===");

        while (true) {
            try {
                contexto.mostrarMenu();
                int opcion = contexto.getConsoleInputProvider().leerOpcionInt();

                procesarOpcion(opcion);

            } catch (Exception e) {
                System.out.println("Introduzca una opción válida");
                contexto.getConsoleInputProvider().leerOpcionString();
            }
        }
    }

    /**
     * Genera algunos usuarios ficticios para poder
     * hacer uso de la app con una base mínima.
     */
       private void createSomeUsers() {
            BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
            RepositorioUsuarios.agregarUsuarioYAsignarCuenta("Jose", "Lucero", bCryptPasswordEncoderService.hash("Joselucero"),"ARS");
            RepositorioUsuarios.agregarUsuarioYAsignarCuenta("Mario", "Moya", bCryptPasswordEncoderService.hash("Mariomoya"),"CLP");
            RepositorioUsuarios.agregarUsuarioYAsignarCuenta("Javiera", "Rojas", bCryptPasswordEncoderService.hash("Javierarojas"),"CLP");
        }


}
