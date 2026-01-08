package org.josemalucero.app;

import org.josemalucero.dominio.usuario.ContextoUsuario;

public class AlkeWalletFake extends AlkeWallet{
    boolean continuar = true;

    public AlkeWalletFake(ContextoUsuario contextoUsuario,boolean runningInConsole, boolean createBasicEntities) {
        super(contextoUsuario,runningInConsole,createBasicEntities);
    }



    @Override
    public void run() {
                contextoUsuario.mostrarInformacionContextual();
                String opcion = this.contextoUsuario.getConsoleInputProvider().leerOpcionString();
                if (opcion.equals("EXIT")) {
                    System.exit(0);
                }
                procesarOpcion(opcion);

        }


    public void runBySteps(int steps) {
        for (int i = 0; i < steps; i++) {

            contextoUsuario.mostrarInformacionContextual();
            String opcion = this.contextoUsuario.getConsoleInputProvider().leerOpcionString();
            if (opcion.equals("EXIT")) {
                System.exit(0);
            }
            procesarOpcion(opcion);
        }
   }
}
