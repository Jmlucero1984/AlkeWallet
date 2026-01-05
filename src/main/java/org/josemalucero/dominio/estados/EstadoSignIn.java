package org.josemalucero.dominio.estados;

import org.josemalucero.servicio.InputProvider;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.BCryptPasswordEncoderService;
import org.josemalucero.servicio.OutputProvider;
import org.josemalucero.servicio.RepositorioUsuarios;

import java.util.Optional;

public class EstadoSignIn extends EstadoUsuario {

    public EstadoSignIn(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    @Override
    public void mostrarMenu(ContextoUsuario contextoUsuario) {

        outputProvider.println("A continuación creará un nuevo usuario, desea continuar?\n");
        outputProvider.println("1. Si");
        outputProvider.println("2. NO, volver");
        outputProvider.print("\nSeleccione una opción: ");
    }

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

    @Override
    public String getNombreEstado() {
        return  "REGISTRO DE NUEVO USUARIO";
    }
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

        // 2. Validar longitud máxima
        if (entrada.length() > 10) {
            outputProvider.println("[!] No puede ser mayor de 10 caracteres");
            return null;
        }

        // 3. Validar que no tenga espacios intermedios
        if (entrada.contains(" ")) {
            outputProvider.println("[!] No puede contener espacios intermedios");
            return null;
        }

        // Si pasa todas las validaciones, devolver el valor
        return entrada;
    }

    private String validarNombresOApellidosDeUsuarios(InputProvider inputProvider){
        String entrada;

        entrada = inputProvider.leerOpcionString().trim();
        // 1. Validar que no esté vacío
        if (entrada.length() == 0) {
            outputProvider.println("[!] No puede estar vacío");
            return null;
        }

        if (entrada.length() < 3) {
            outputProvider.println("[!] Deber tener al menos 3 caracteres");
            return null;
        }

        // 2. Validar longitud máxima
        if (entrada.length() > 15) {
            outputProvider.println("[!] No puede ser mayor de 15 caracteres");
            return null;
        }

        // 3. Validar que solo tenga letras (sin números ni caracteres especiales)
        if (!entrada.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$")) {
            outputProvider.println("[!] Solo puede contener letras (incluye tildes y ñ)");
            return null;
        }

        // 4. Validar que empiece con mayúscula
        if (!Character.isUpperCase(entrada.charAt(0))) {
            outputProvider.println("[!] Debe empezar con una letra mayúscula");
            return null;
        }

        // 5. Validar que no tenga espacios intermedios
        if (entrada.contains(" ")) {
            outputProvider.println("[!] No puede contener espacios intermedios");
            return null;
        }

        // Si pasa todas las validaciones, devolver el valor
        return entrada;

    }



    private void registrarNuevoUsuario(ContextoUsuario contextoUsuario) {
        InputProvider consoleInputProvider =  contextoUsuario.getConsoleInputProvider();
        //Console console = System.console();
        BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
        String nombre=null;
        String apellido=null;
        while(nombre==null){
            outputProvider.print("Nombre de usuario: ");
            nombre = validarNombresOApellidosDeUsuarios(consoleInputProvider);
        }
        while(apellido==null){
            outputProvider.print("Apellido de usuario: ");
            apellido = validarNombresOApellidosDeUsuarios(consoleInputProvider);
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
}
