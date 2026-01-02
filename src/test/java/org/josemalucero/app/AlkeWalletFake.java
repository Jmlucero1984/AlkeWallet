package org.josemalucero.app;

import org.josemalucero.dominio.usuario.ContextoUsuario;

public class AlkeWalletFake extends AlkeWallet{


    public AlkeWalletFake(ContextoUsuario contexto) {
        super(contexto);
    }

    @Override
    public void run() {


            System.out.println("=== BIENVENIDO A BILLETERA VIRTUAL ===");

                try {
                    contexto.mostrarMenu();
                    int opcion = this.contexto.getConsoleInputProvider().leerOpcionInt();

                    procesarOpcion(opcion);

                } catch (Exception e) {
                    System.out.println("Introduzca una opción válida");
                    this.contexto.getConsoleInputProvider().leerOpcionString();
                }

        }

}
