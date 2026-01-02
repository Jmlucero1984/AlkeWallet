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
            contexto.cambiarEstado(new EstadoLogin());
        }
    }

    @Override
    public String getNombreEstado() {
        return  "REGISTRO DE NUEVO USUARIO";
    }

    private void registrarNuevoUsuario(ContextoUsuario contexto) {
        InputProvider consoleInputProvider =  contexto.getConsoleInputProvider();
        //Console console = System.console();
        BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();

        System.out.print("Nombre de usuario: ");
        String nombre = consoleInputProvider.leerOpcionString();
        System.out.print("Apellido de usuario: ");
        String apellido = consoleInputProvider.leerOpcionString();


        Optional<Usuario> usuarioExistente = RepositorioUsuarios.consultarUsuario(nombre,apellido);
        if(usuarioExistente.isPresent()){
            System.out.println("Ya existe un usuario con el nombre: "+usuarioExistente.get().getNombreCompleto());
        } else {
            String clave ="";

            boolean coinciden = false;
            while(!coinciden){
                System.out.println("Introduzca su clave: ");
                clave = consoleInputProvider.leerOpcionString();
                /*
                char[] passwordArray = console.readPassword("Contraseña (espacios): ");
                clave = new String(passwordArray);
                */

                System.out.print("Confirmar clave: ");
                String confirmaClave  = consoleInputProvider.leerOpcionString();
                /*
                passwordArray = console.readPassword("Contraseña (espacios): ");
                String confirmaClave =new String(passwordArray);

                // Limpiar el array de caracteres por seguridad
                java.util.Arrays.fill(passwordArray, ' ');
                */

                if(clave.equals(confirmaClave)) {
                    coinciden = true;


                } else {
                    System.out.println("Las constraseñas no coincidem");
                }
            }

            Usuario nuevoUsuario = RepositorioUsuarios.agregarUsuario(nombre,apellido,bCryptPasswordEncoderService.hash(clave));
            System.out.println("El usuario "+nuevoUsuario.getNombreCompleto() + " ha sido creado");
        }

        contexto.cambiarEstado(new EstadoLogin());
    }
}
