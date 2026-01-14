package org.josemalucero.dominio.estado;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

import java.util.Locale;

/**
 * Punto de entrada al programa siendo este el estado primigenio que da origen a la cadena de estados subsiguiente
 * a lo largo de la actividad del usuario dentro de la aplicación.
 @author José Maria Lucero
 */
public class EstadoInicio extends EstadoUsuario{


    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoInicio(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**
     * Muestra las opciones para iniciar sesión, registrarse o salir.
     * @param contextoUsuario
     */
    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {

        outputProvider.println("1. "+Messages.get("opcion.iniciar.sesion"));
        outputProvider.println("2. "+Messages.get("opcion.registrarse"));

        if(Messages.getCurrentLocale().toLanguageTag().equals("en")){
            outputProvider.println("3. Cambiar lenguaje a español");
        } else {
            outputProvider.println("3. Change to english language");
        }
        outputProvider.println("4. "+Messages.get("salir"));
        outputProvider.print(Messages.get("seleccione.opcion")+": ");
    }

    /**
     * Recibe la opción para derivar en los estados correspondientes.
     * @param opcionStr
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(String opcionStr, ContextoUsuario contextoUsuario) {

        try {
            int opcion = Integer.parseInt(opcionStr);
            switch (opcion) {
                case 1:
                    contextoUsuario.cambiarEstado(new EstadoLogin(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                    break;

                case 2:
                    contextoUsuario.cambiarEstado(new EstadoSignIn(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                    break;
                case 3:
                    cambiarLenguaje();
                    break;
                case 4:
                    mostrarMensajeDespedida();
                    System.exit(0);
                default:
                    outputProvider.printlnAlert(Messages.get("alerta.opcion.invalida"));
            }

        } catch (NumberFormatException e) {
            outputProvider.printlnAlert(Messages.get("alerta.introduzca.opcion.valida"));
        }
    }


    /**
     * Detecta cual es lenguaje actual de la app y lo cambia por su alternativa
     */
    private void cambiarLenguaje(){
        if(Messages.getCurrentLocale().toLanguageTag().equals("en")){
            Messages.init(Locale.forLanguageTag("es"));
        } else{
            Messages.init(Locale.forLanguageTag("en"));
        }
    }

    private void mostrarMensajeDespedida(){
        outputProvider.println("\n   "+Messages.get("esperamos.vuelva.pronto")+"\n");
        outputProvider.println("─────────────────────────────────");
        outputProvider.println(Messages.get("desarrollado.por"));
    }


    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.inicio");

    }
}
