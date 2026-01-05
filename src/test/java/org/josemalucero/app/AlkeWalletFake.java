package org.josemalucero.app;

import org.josemalucero.dominio.usuario.ContextoUsuario;

public class AlkeWalletFake extends AlkeWallet{
    boolean continuar = true;

    public AlkeWalletFake(ContextoUsuario contextoUsuario) {
        super(contextoUsuario);
    }



    @Override
    public void run() {


        contextoUsuario.getOuputProvider().println("=== BIENVENIDO A BILLETERA VIRTUAL ===");

                try {
                    contextoUsuario.mostrarMenu();
                    int opcion = this.contextoUsuario.getConsoleInputProvider().leerOpcionInt();
                    if (opcion==-1) {
                        System.exit(0);
                    }

                    procesarOpcion(opcion);

                } catch (Exception e) {
                    contextoUsuario.getOuputProvider().println("Introduzca una opción válida");
                    this.contextoUsuario.getConsoleInputProvider().leerOpcionString();
                }


        }


    public void runBySteps(int steps) {
        for (int i = 0; i < steps; i++) {
            try {
                contextoUsuario.mostrarMenu();
                int opcion = this.contextoUsuario.getConsoleInputProvider().leerOpcionInt();
                if (opcion==-1) {
                    System.exit(0);
                }

                procesarOpcion(opcion);

            } catch (Exception e) {
               contextoUsuario.getOuputProvider().println("Introduzca una opción válida");
                this.contextoUsuario.getConsoleInputProvider().leerOpcionString();
            }
        }


   }

}
