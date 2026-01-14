package org.josemalucero.dominio.estado;

import org.josemalucero.app.AlkeWallet;
import org.josemalucero.dominio.usuario.Validador;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.passwords.BCryptPasswordEncoderService;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;
import org.josemalucero.servicio.repositorios.RepositorioUsuarios;

import java.io.Console;
import java.util.Optional;
/**
 * Permite autenticar un usuario y en caso de éxito se verifica si el mismo tiene una cuenta asociada, de serlo
 * efectivamente, se cambia al estado {@link EstadoOperaciones}, caso contrario, se deriva al estado {@link EstadoCreacionCuenta}.
 *  @author José Maria Lucero
 *

 */

public class EstadoLogin extends EstadoUsuario {
    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoLogin(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**
     * Muestra nuevamente a modo de confirmación la opción de Iniciar sesión o Volver.
     * @param contextoUsuario
     */
    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {

        outputProvider.println("1. "+Messages.get("opcion.iniciar.sesion"));
        outputProvider.println("2. "+Messages.get("volver"));
        outputProvider.print(Messages.get("seleccione.opcion")+": ");
    }

    /**
     * Realiza la aunteticación del usuario según las credenciales ingresadas. De ser efectiva dicha verificación,
     * controla si el usuario ya tiene una cuenta asocida, lo que implica derivar hacia {@link EstadoOperaciones}, caso contrario
     * redirije a {@link EstadoCreacionCuenta}. Si el usuario opta por Volver, se redirige al estado {@link EstadoInicio}.
     * @param opcionStr
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(String opcionStr, ContextoUsuario contextoUsuario) {
        try {
            int opcion = Integer.parseInt(opcionStr);
        switch (opcion) {
            case 1:
                Usuario usuario = autenticarUsuario(contextoUsuario);
                if (usuario != null) {
                    contextoUsuario.setUsuarioLogueado(usuario);
                    contextoUsuario.reset_operaciones_por_sesion();
                    if (contextoUsuario.getUsuarioLogueado().getCuentaRegular() == null) {
                        outputProvider.printlnAlert(Messages.get("alerta.aun.no.tiene.cuenta.asociada"));
                        contextoUsuario.cambiarEstado(new EstadoCreacionCuenta(contextoUsuario.getConsoleInputProvider(),
                                contextoUsuario.getOuputProvider()));
                    } else {
                        contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(),
                                contextoUsuario.getOuputProvider()));
                    }
                }
                break;
            case 2:
                contextoUsuario.cambiarEstado(new EstadoInicio(contextoUsuario.getConsoleInputProvider(),
                        contextoUsuario.getOuputProvider()));
                break;
            }
        } catch (NumberFormatException e) {
            outputProvider.printlnAlert(Messages.get("alerta.introduzca.opcion.valida"));
        }


    }


    /*
    Las líneas comentadas dentro del método corresponden a una implementacion para introducir contraseñas sin mostrar los
    caracteres. La misma se ha deshabilitado por ser necesaria la ejecución desde un terminal propiamente dicho, y no desde
    la misma salida del IDE.

    Seguramente podría haberse desagregado el Encoder en otro "provider", empleando una interfaz para igualmente desacoplarlo,
    de manera similar al InputProvider.
    */

    /**
     * Solicita las credenciales del usuario que solicita loquearse para ser auntenticadas. En caso de no encontrarse usuario
     * cuyas credenciales coincidan con las ingresadas, se informa media el correspondiente {@link OutputProvider}.
     * @param contextoUsuario
     * @return Usuario si las credenciales son auntenticadas, {@code null} en caso de no existir un usuario con tales credenciales.
     */
    private Usuario autenticarUsuario(ContextoUsuario contextoUsuario) {
        Validador validador = new Validador(inputProvider,outputProvider);

        BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
        InputProvider consoleInputProvider = contextoUsuario.getConsoleInputProvider();
        outputProvider.println("\n"+Messages.get("ingrese.sus.datos.personales"));
        String nombre=null;
        while(nombre==null){
            outputProvider.print(Messages.get("nombre.usuario")+": ");
            nombre = validador.validaNombresOApellidosDeUsuario();
        }
        String apellido=null;
        while(apellido==null){
            outputProvider.print(Messages.get("apellido.usuario")+": ");
            apellido = validador.validaNombresOApellidosDeUsuario();
        }

        Optional<Usuario> usuarioExistente = RepositorioUsuarios.consultarUsuario(nombre,apellido);
        if(usuarioExistente.isPresent()){
            outputProvider.print(Messages.get("ingrese.clave"));
            String clave;
            if(AlkeWallet.onConsole){
                Console console = System.console();
                char[] passwordArray = console.readPassword(" "+Messages.get("modo.secreto")+": ");
                clave = new String(passwordArray);
                // Limpiar el array de caracteres por seguridad
                java.util.Arrays.fill(passwordArray, ' ');
            } else {
                outputProvider.print(": ");
                clave = consoleInputProvider.leerOpcionString();
            }
            if(bCryptPasswordEncoderService.matches(clave, usuarioExistente.get().getClave())){
                outputProvider.println(Messages.get("logueo.exitoso"));
                return  usuarioExistente.get();
            } else {
                outputProvider.println(Messages.get("alerta.datos.inicio.sesion.no.validos"));
            }
        } else {
            outputProvider.println( Messages.get("el.usuario")+" "+nombre+" "+apellido+" "+Messages.get("alerta.no.existe.en.base.de.datos"));
        }
        return null;
    }



    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.login");
    }


}