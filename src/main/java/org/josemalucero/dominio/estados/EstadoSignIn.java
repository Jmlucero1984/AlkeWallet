package org.josemalucero.dominio.estados;

import org.josemalucero.app.ConsoleInputProvider;
import org.josemalucero.app.InputProvider;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.BCryptPasswordEncoderService;
import org.josemalucero.servicio.PasswordHashService;
import org.josemalucero.servicio.RepositorioUsuarios;

import java.io.Console;
import java.util.Optional;
import java.util.Scanner;

public class EstadoSignIn implements EstadoUsuario {

    @Override
    public void mostrarMenu(ContextoUsuario contextoUsuario) {

        System.out.println("A continuación creará un nuevo usuario, desea continuar?\n");
        System.out.println("1. Si");
        System.out.println("2. NO, volver");
        System.out.print("\nSeleccione una opción: ");
    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contexto) {
        // En este estado, no usamos opciones de menú numéricas
        // sino que procesamos el flujo de registro completo
        if(opcion==1) {
            registrarNuevoUsuario(contexto);
        } else {
            contexto.cambiarEstado(new EstadoEntrada());
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
            System.out.println("[!] No puede estar vacío");
            return null;
        }

        if (entrada.length() <6) {
            System.out.println("[!] Deber tener al menos 6 caracteres");
            return null;
        }

        // 2. Validar longitud máxima
        if (entrada.length() > 10) {
            System.out.println("[!] No puede ser mayor de 10 caracteres");
            return null;
        }

        // 3. Validar que no tenga espacios intermedios
        if (entrada.contains(" ")) {
            System.out.println("[!] No puede contener espacios intermedios");
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
            System.out.println("[!] No puede estar vacío");
            return null;
        }

        if (entrada.length() < 3) {
            System.out.println("[!] Deber tener al menos 3 caracteres");
            return null;
        }

        // 2. Validar longitud máxima
        if (entrada.length() > 15) {
            System.out.println("[!] No puede ser mayor de 15 caracteres");
            return null;
        }

        // 3. Validar que solo tenga letras (sin números ni caracteres especiales)
        if (!entrada.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$")) {
            System.out.println("[!] Solo puede contener letras (incluye tildes y ñ)");
            return null;
        }

        // 4. Validar que empiece con mayúscula
        if (!Character.isUpperCase(entrada.charAt(0))) {
            System.out.println("[!] Debe empezar con una letra mayúscula");
            return null;
        }

        // 5. Validar que no tenga espacios intermedios
        if (entrada.contains(" ")) {
            System.out.println("[!] No puede contener espacios intermedios");
            return null;
        }

        // Si pasa todas las validaciones, devolver el valor
        return entrada;

    }



    private void registrarNuevoUsuario(ContextoUsuario contexto) {
        InputProvider consoleInputProvider =  contexto.getConsoleInputProvider();
        //Console console = System.console();
        BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
        String nombre=null;
        String apellido=null;
        while(nombre==null){
            System.out.print("Nombre de usuario: ");
            nombre = validarNombresOApellidosDeUsuarios(consoleInputProvider);
        }
        while(apellido==null){
            System.out.print("Apellido de usuario: ");
            apellido = validarNombresOApellidosDeUsuarios(consoleInputProvider);
        }



        Optional<Usuario> usuarioExistente = RepositorioUsuarios.consultarUsuario(nombre,apellido);
        if(usuarioExistente.isPresent()){
            System.out.println("Ya existe un usuario con el nombre: "+usuarioExistente.get().getNombreCompleto());
        } else {
            String clave =null;

            boolean coinciden = false;
            while(!coinciden){

                while(clave==null){
                    System.out.println("Introduzca su clave: ");
                    clave = validarClavesDeUsuario(consoleInputProvider);
                }

                /*
                char[] passwordArray = console.readPassword("Contraseña (espacios): ");
                clave = new String(passwordArray);
                */
                String confirmaClave=null;
                while(confirmaClave==null){
                    System.out.print("Confirmar clave: ");
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
                    System.out.println("Las constraseñas no coincide");
                    clave=null;

                }
            }

            Usuario nuevoUsuario = RepositorioUsuarios.agregarUsuario(nombre,apellido,bCryptPasswordEncoderService.hash(clave));
            System.out.println("El usuario "+nuevoUsuario.getNombreCompleto() + " ha sido creado");
        }

        contexto.cambiarEstado(new EstadoLogin());
    }
}
