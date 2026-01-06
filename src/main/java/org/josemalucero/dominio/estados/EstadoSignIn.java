package org.josemalucero.dominio.estados;

import org.josemalucero.servicio.InputProvider;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.BCryptPasswordEncoderService;
import org.josemalucero.servicio.OutputProvider;
import org.josemalucero.servicio.RepositorioUsuarios;

import java.util.Optional;
/**
 La clase {@code EstadoSignIn} es una implmentación concreta de la clase abstracta {@code EstadoUsuario}.

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
    public void mostrarMenu(ContextoUsuario contextoUsuario) {

        outputProvider.println("A continuación creará un nuevo usuario, desea continuar?\n");
        outputProvider.println("1. Si");
        outputProvider.println("2. NO, volver");
        outputProvider.print("\nSeleccione una opción: ");
    }

    /**
     * Recibe la opción elegida, sea para registar un nuevo {@link Usuario}, o para retornar al estado anterior, {@link EstadoEntrada}.
     * @param opcion
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contextoUsuario) {
        // En este estado, no usamos opciones de menú numéricas
        // sino que procesamos el flujo de registro completo
        if(opcion==1) {
            registrarNuevoUsuario(contextoUsuario);
        } else {
            contextoUsuario.cambiarEstado(new EstadoEntrada(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
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
            outputProvider.println("[!] No puede estar vacío");
            return null;
        }

        if (entrada.length() <6) {
            outputProvider.println("[!] Deber tener al menos 6 caracteres");
            return null;
        }

        if (entrada.length() > 10) {
            outputProvider.println("[!] No puede ser mayor de 10 caracteres");
            return null;
        }

        if (entrada.contains(" ")) {
            outputProvider.println("[!] No puede contener espacios intermedios");
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
            outputProvider.println("[!] No puede estar vacío");
            return null;
        }

        if (entrada.length() < 3) {
            outputProvider.println("[!] Deber tener al menos 3 caracteres");
            return null;
        }

        if (entrada.length() > 15) {
            outputProvider.println("[!] No puede ser mayor de 15 caracteres");
            return null;
        }

        if (!entrada.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$")) {
            outputProvider.println("[!] Solo puede contener letras (incluye tildes y ñ)");
            return null;
        }

        if (!Character.isUpperCase(entrada.charAt(0))) {
            outputProvider.println("[!] Debe empezar con una letra mayúscula");
            return null;
        }

        if (entrada.contains(" ")) {
            outputProvider.println("[!] No puede contener espacios intermedios");
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
        while(nombre==null){
            outputProvider.print("Nombre de usuario: ");
            nombre = validarNombresOApellidosDeUsuario(consoleInputProvider);
        }
        while(apellido==null){
            outputProvider.print("Apellido de usuario: ");
            apellido = validarNombresOApellidosDeUsuario(consoleInputProvider);
        }


        Optional<Usuario> usuarioExistente = RepositorioUsuarios.consultarUsuario(nombre,apellido);
        if(usuarioExistente.isPresent()){
            outputProvider.println("Ya existe un usuario con el nombre: "+usuarioExistente.get().getNombreCompleto());
        } else {
            String clave =null;
            boolean coinciden = false;
            while(!coinciden){

                while(clave==null){
                    outputProvider.println("Introduzca su clave: ");
                    clave = validarClavesDeUsuario(consoleInputProvider);
                }

                /*
                char[] passwordArray = console.readPassword("Contraseña (espacios): ");
                clave = new String(passwordArray);
                */
                String confirmaClave=null;
                while(confirmaClave==null){
                    outputProvider.print("Confirmar clave: ");
                    confirmaClave  = validarClavesDeUsuario(consoleInputProvider);
                }

                /*
                passwordArray = console.readPassword("Contraseña (espacios): ");
                String confirmaClave =new String(passwordArray);

                // Limpiar el array de caracteres por seguridad
                java.util.Arrays.fill(passwordArray, ' ');
                */

                if(clave.equals(confirmaClave)) {
                    coinciden = true;
                } else {
                    outputProvider.println("Las constraseñas no coincide");
                    clave=null;

                }
            }

            Usuario nuevoUsuario = RepositorioUsuarios.agregarUsuario(nombre,apellido,bCryptPasswordEncoderService.hash(clave));
            outputProvider.println("El usuario "+nuevoUsuario.getNombreCompleto() + " ha sido creado");
        }

        contextoUsuario.cambiarEstado(new EstadoLogin(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
    }

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return  "REGISTRO DE NUEVO USUARIO";
    }
}
