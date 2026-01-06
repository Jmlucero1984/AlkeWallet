package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.servicio.OutputProvider;

/**
 * Punto de entrada al programa siendo este el estado primigenio que da origen a la cadena de estados subsiguiente
 * a lo largo de la actividad del usuario dentro de la aplicación.
 @author José Maria Lucero
 */
public class EstadoEntrada extends EstadoUsuario{


    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoEntrada(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**
     * Muestra las opciones para iniciar sesión, registrarse o salir.
     * @param contextoUsuario
     */
    @Override
    public void mostrarMenu(ContextoUsuario contextoUsuario) {
        outputProvider.println("1. Iniciar sesión");
        outputProvider.println("2. Registrarse (Sign In)");
        outputProvider.println("3. Salir");
        outputProvider.print("Seleccione una opción: ");
    }

    /**
     * Recibe la opción para derivar en los estados correspondientes.
     * @param opcion
     * @param contextoUsuario
     */
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

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return "ENTRADA";
    }
}
