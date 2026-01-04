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

       private void createSomeUsers() {
            BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
            RepositorioUsuarios.agregarUsuarioYAsignarCuenta("Jose", "Lucero", bCryptPasswordEncoderService.hash("Joselucero"),"ARS");
            RepositorioUsuarios.agregarUsuarioYAsignarCuenta("Mario", "Moya", bCryptPasswordEncoderService.hash("Mariomoya"),"CLP");
            RepositorioUsuarios.agregarUsuarioYAsignarCuenta("Javiera", "Rojas", bCryptPasswordEncoderService.hash("Javierarojas"),"CLP");
        }


}
