package org.josemalucero.dominio.estados;

import org.josemalucero.servicio.InputProvider;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.BCryptPasswordEncoderService;
import org.josemalucero.servicio.OutputProvider;
import org.josemalucero.servicio.RepositorioUsuarios;

import java.util.Optional;

public class EstadoLogin extends EstadoUsuario {

    public EstadoLogin(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    @Override
    public void mostrarMenu(ContextoUsuario contextoUsuario) {

        outputProvider.println("1. Iniciar sesión");
        outputProvider.println("2. Volver...");

    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contextoUsuario) {
        switch (opcion) {
            case 1:
                Usuario usuario = autenticarUsuario(contextoUsuario);
                if (usuario != null) {
                    contextoUsuario.setUsuarioLogueado(usuario);
                    if (contextoUsuario.getUsuarioLogueado().getCuentaRegular() == null) {
                        outputProvider.println("AUN NO TIENE UNA CUENTA ASOCIADA");
                        contextoUsuario.cambiarEstado(new EstadoCreacionCuenta(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                    } else {
                        contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                    }
                }
                break;
            case 2:
                contextoUsuario.cambiarEstado(new EstadoEntrada(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                break;
        }


    }

    @Override
    public String getNombreEstado() {
        return "LOGIN";
    }

    private Usuario autenticarUsuario(ContextoUsuario contextoUsuario) {

        BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
        InputProvider consoleInputProvider = contextoUsuario.getConsoleInputProvider();
        //Console console = System.console();
        outputProvider.print("Usuario nombre: ");
        String nombre = consoleInputProvider.leerOpcionString();
        outputProvider.print("Usuario apellido: ");
        String apellido = consoleInputProvider.leerOpcionString();
        Optional<Usuario> usuarioExistente = RepositorioUsuarios.consultarUsuario(nombre,apellido);
        if(usuarioExistente.isPresent()){
            outputProvider.println("Ingrese su clave: ");
            String clave = consoleInputProvider.leerOpcionString();
            /*
            char[] passwordArray = console.readPassword("Contraseña (espacios): ");
            String clave = new String(passwordArray);
            java.util.Arrays.fill(passwordArray, ' ');
            */

            // Limpiar el array de caracteres por seguridad

            if(bCryptPasswordEncoderService.matches(clave, usuarioExistente.get().getClave())){
                outputProvider.println("LOGUEO EXITOSO");
                return  usuarioExistente.get();
            } else {
                outputProvider.println("Datos de inicio de sesión no válidos");
            }

        } else {
            outputProvider.println("El usuario "+nombre+" "+apellido+" no existe en la Base de Datos");
        }

        return null;
    }
}