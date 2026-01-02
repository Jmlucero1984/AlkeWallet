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

public class EstadoLogin implements EstadoUsuario {

    @Override
    public void mostrarMenu(ContextoUsuario contextoUsuario) {

        System.out.println("1. Iniciar sesión");
        System.out.println("2. Volver...");

    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contexto) {
        switch (opcion) {
            case 1:
                Usuario usuario = autenticarUsuario(contexto);
                if (usuario != null) {
                    contexto.setUsuarioLogueado(usuario);
                    if (contexto.getUsuarioLogueado().getCuentaRegular() == null) {
                        System.out.println("AUN NO TIENE UNA CUENTA ASOCIADA");
                        contexto.cambiarEstado(new EstadoCreacionCuenta());
                    } else {
                        contexto.cambiarEstado(new EstadoOperaciones());
                    }
                }
                break;
            case 2:
                contexto.cambiarEstado(new EstadoEntrada());
                break;
        }


    }

    @Override
    public String getNombreEstado() {
        return "LOGIN";
    }

    private Usuario autenticarUsuario(ContextoUsuario contextoUsuario) {
        // Lógica real de autenticación aquí
        BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
        InputProvider consoleInputProvider = contextoUsuario.getConsoleInputProvider();
        //Console console = System.console();
        System.out.print("Usuario nombre: ");
        String nombre = consoleInputProvider.leerOpcionString();
        System.out.print("Usuario apellido: ");
        String apellido = consoleInputProvider.leerOpcionString();
        Optional<Usuario> usuarioExistente = RepositorioUsuarios.consultarUsuario(nombre,apellido);
        if(usuarioExistente.isPresent()){
            System.out.println("Ingrese su clave: ");
            String clave = consoleInputProvider.leerOpcionString();
            /*
            char[] passwordArray = console.readPassword("Contraseña (espacios): ");
            String clave = new String(passwordArray);
            java.util.Arrays.fill(passwordArray, ' ');
            */

            // Limpiar el array de caracteres por seguridad

            if(bCryptPasswordEncoderService.matches(clave, usuarioExistente.get().getClave())){
                System.out.println("LOGUEO EXITOSO");
                return  usuarioExistente.get();
            } else {
                System.out.println("Datos de inicio de sesión no válidos");
            }

        } else {
            System.out.println("El usuario "+nombre+" "+apellido+" no existe en la Base de Datos");
        }

        return null;
    }
}