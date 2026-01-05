package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.servicio.OutputProvider;

public class EstadoEntrada extends EstadoUsuario{

    public EstadoEntrada(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        outputProvider.println("1. Iniciar sesión");
        outputProvider.println("2. Registrarse (Sign In)");
        outputProvider.println("3. Salir");
        outputProvider.print("Seleccione una opción: ");
    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contextoUsuario) {

            switch (opcion) {
                case 1:
                    contextoUsuario.cambiarEstado(new EstadoLogin(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                    break;

                case 2:
                    contextoUsuario.cambiarEstado(new EstadoSignIn(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                    break;

                case 3:
                    contextoUsuario.cambiarEstado(new EstadoSalir(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                    break;

                default:
                    outputProvider.println("Opción inválida");
            }
        }


    @Override
    public String getNombreEstado() {
        return "ENTRADA";
    }
}
