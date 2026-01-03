package org.josemalucero.app;

import org.josemalucero.dominio.usuario.ContextoUsuario;

public class AlkeWalletFake extends AlkeWallet{
    boolean continuar = true;

    public AlkeWalletFake(ContextoUsuario contexto) {
        super(contexto);
    }



    @Override
    public void run() {


            System.out.println("=== BIENVENIDO A BILLETERA VIRTUAL ===");

                try {
                    contexto.mostrarMenu();
                    int opcion = this.contexto.getConsoleInputProvider().leerOpcionInt();
                    if (opcion==-1) {
                        System.exit(0);
                    }

                    procesarOpcion(opcion);

                } catch (Exception e) {
                    System.out.println("Introduzca una opción válida");
                    this.contexto.getConsoleInputProvider().leerOpcionString();
                }


        }


    public void runBySteps(int steps) {
        for (int i = 0; i < steps; i++) {
            try {
                contexto.mostrarMenu();
                int opcion = this.contexto.getConsoleInputProvider().leerOpcionInt();
                if (opcion==-1) {
                    System.exit(0);
                }

                procesarOpcion(opcion);

            } catch (Exception e) {
                System.out.println("Introduzca una opción válida");
                this.contexto.getConsoleInputProvider().leerOpcionString();
            }
        }


   }

}
