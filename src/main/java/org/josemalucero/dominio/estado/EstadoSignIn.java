package org.josemalucero.dominio.estado;

import org.josemalucero.app.AlkeWallet;
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
 La clase {@code EstadoSignIn} es una implmentación concreta de la clase abstracta {@code EstadoUsuario}.
 @author José Maria Lucero
 */

public class EstadoSignIn extends EstadoUsuario {
    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoSignIn(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**
     * Informa al potencial usuario acerca del proceder sobre la creación de una entidad que lo represente para
     * poder operar en la plataforma.
     * @param contextoUsuario
     */
    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {

        outputProvider.println(Messages.get("a.continuacion.creara.nuevo.usuario.desea.continuar")+"\n");
        outputProvider.println("1. "+Messages.get("si"));
        outputProvider.println("2. "+Messages.get("no.volver"));
        outputProvider.print(Messages.get("seleccione.opcion"));
    }

    /**
     * Recibe la opción elegida, sea para registar un nuevo {@link Usuario}, o para retornar al estado anterior, {@link EstadoInicio}.
     * @param opcionStr
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(String opcionStr, ContextoUsuario contextoUsuario) {
        try {
            int opcion = Integer.parseInt(opcionStr);
            // En este estado, no usamos opciones de menú numéricas
            // sino que procesamos el flujo de registro completo
            if (opcion == 1) {
                registrarNuevoUsuario(contextoUsuario);
            } else {
                contextoUsuario.cambiarEstado(new EstadoInicio(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
            }
        } catch (NumberFormatException e) {
            outputProvider.println(Messages.get("introduzca.opcion.valida"));
        }
    }



    /**
     * Recibe lo que el usuario introduce como su clave y realiza una serie de validaciones mínimas,
     * como cantidad mínima y máxima de caracteres o prohibición de espacios intermedios.
     * @param inputProvider
     * @return {@code String} que cumple con las especificaciones para credenciales de usuario.
     */
    private String validarClavesDeUsuario(InputProvider inputProvider) {
        String entrada;

        entrada = inputProvider.leerOpcionString().trim();
        if (entrada.length() == 0) {
            outputProvider.println(Messages.get("validacion.no.puede.estar.vacio"));
            return null;
        }

        if (entrada.length() <6) {
            outputProvider.println(Messages.get("validacion.tener.al.menos")+" "+6+" "+Messages.get("caracteres"));
            return null;
        }

        if (entrada.length() > 10) {
            outputProvider.println(Messages.get("validacion.tener.no.mas")+" "+10+" "+Messages.get("caracteres"));
            return null;
        }

        if (entrada.contains(" ")) {
            outputProvider.println(Messages.get("validacion.no.espacios.intermedios"));
            return null;
        }

        return entrada;
    }

    /**
     * Recibe lo que el usuario introduce como su nombre y apellido y realiza una serie de validaciones mínimas,
     * como cantidad mínima y máxima de caracteres, prohibición de espacios intermedios, letra inicial en mayúscula
     * obligatoria y solo caracteres alfabéticos.
     * @param inputProvider
     * @return {@code String} que cumple con las especificaciones para credenciales de usuario.
     */

    private String validarNombresOApellidosDeUsuario(InputProvider inputProvider){
        String entrada;
        entrada = inputProvider.leerOpcionString().trim();
        if (entrada.length() == 0) {
            outputProvider.println(Messages.get("validacion.no.puede.estar.vacio"));
            return null;
        }

        if (entrada.length() < 3) {
            outputProvider.println(Messages.get("validacion.tener.al.menos")+" "+3+" "+Messages.get("caracteres"));
            return null;
        }

        if (entrada.length() > 15) {
            outputProvider.println(Messages.get("validacion.tener.no.mas")+" "+15+" "+Messages.get("caracteres"));
            return null;
        }

        if (!entrada.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$")) {
            outputProvider.println(Messages.get("validacion.contrasena.solo.contener"));
            return null;
        }

        if (!Character.isUpperCase(entrada.charAt(0))) {
            outputProvider.println(Messages.get("validacion.debe.empezar.con.mayuscula"));
            return null;
        }

        if (entrada.contains(" ")) {
            outputProvider.println(Messages.get("validacion.no.espacios.intermedios"));
            return null;
        }

        return entrada;

    }

    /*
    Si bien este método se denomina registrarNuevoUsuario, tal vez lo más adecuado debería haber sido desagregar las
    operaciones de validación de credenciales por un lado, y la propia incorporación del nuevo usuario a la base de datos
    por otro.

    Las líneas comentadas dentro del método corresponden a una implementacion para introducir contraseñas sin mostrar los
    caracteres. La misma se ha deshabilitado por ser necesaria la ejecución desde un terminal propiamente dicho, y no desde
    la misma salida del IDE.
     */

    /**
     *  Verifica los criterios de validación de las credenciales de usuario como así tambien su existencia
     *  actual en la base de datos de usuarios. Finalmente incorpora el nuevo usuario.
     * @param contextoUsuario
     */
    private void registrarNuevoUsuario(ContextoUsuario contextoUsuario) {
        InputProvider consoleInputProvider =  contextoUsuario.getConsoleInputProvider();
        //Console console = System.console();
        BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
        String nombre=null;
        String apellido=null;
        outputProvider.println("\n"+Messages.get("ingrese.sus.datos.personales"));
        while(nombre==null){
            outputProvider.print(Messages.get("nombre.usuario")+" ");
            nombre = validarNombresOApellidosDeUsuario(consoleInputProvider);
        }
        while(apellido==null){
            outputProvider.print(Messages.get("apellido.usuario")+" ");
            apellido = validarNombresOApellidosDeUsuario(consoleInputProvider);
        }

        Optional<Usuario> usuarioExistente = RepositorioUsuarios.consultarUsuario(nombre,apellido);
        if(usuarioExistente.isPresent()){
            outputProvider.println(Messages.get("ya.existe.usuario.con.nombre")+" "+usuarioExistente.get().getNombreCompleto());
        } else {
            String clave =null;
            boolean coinciden = false;
            while(!coinciden){
                while(clave==null){
                    outputProvider.println(Messages.get("ingrese.clave")+" ");
                    if(AlkeWallet.onConsole){
                            Console console = System.console();
                            char[] passwordArray = console.readPassword(Messages.get("modo.secreto")+" ");;
                            clave = new String(passwordArray);
                            // Limpiar el array de caracteres por seguridad
                            java.util.Arrays.fill(passwordArray, ' ');
                    } else {
                        clave = validarClavesDeUsuario(consoleInputProvider);
                    }
                }
                String confirmaClave=null;
                while(confirmaClave==null){
                    outputProvider.print("Confirmar clave: ");
                    if(AlkeWallet.onConsole){
                        Console console = System.console();
                        char[] passwordArray = console.readPassword("[MODO SECRETO]: ");
                        confirmaClave = new String(passwordArray);
                        // Limpiar el array de caracteres por seguridad
                        java.util.Arrays.fill(passwordArray, ' ');

                    } else {
                        confirmaClave = validarClavesDeUsuario(consoleInputProvider);
                    }
                }
                if(clave.equals(confirmaClave)) {
                    coinciden = true;
                } else {
                    outputProvider.println(Messages.get("validacion.las.contrasenas.no.coinciden"));
                    clave=null;
                }
            }

            Usuario nuevoUsuario = RepositorioUsuarios.agregarUsuario(nombre,apellido,bCryptPasswordEncoderService.hash(clave));
            outputProvider.println(Messages.get("el.usuario")+" "+nuevoUsuario.getNombreCompleto() + " "+Messages.get("ha.sido.creado"));
        }

        contextoUsuario.cambiarEstado(new EstadoLogin(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
    }

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.registro.nuevo.usuario");
    }
}
