package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;

public class EstadoEntrada implements EstadoUsuario{

    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        System.out.println("1. Iniciar sesión");
        System.out.println("2. Registrarse (Sign In)");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contexto) {

            switch (opcion) {
                case 1:
                    contexto.cambiarEstado(new EstadoLogin());
                    break;

                case 2:
                    contexto.cambiarEstado(new EstadoSignIn());
                    break;

                case 3:
                    contexto.cambiarEstado(new EstadoSalir());
                    break;

                default:
                    System.out.println("Opción inválida");
            }
        }


    @Override
    public String getNombreEstado() {
        return "ENTRADA";
    }
}
