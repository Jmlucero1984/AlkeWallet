package org.josemalucero.servicio;

import org.josemalucero.dominio.usuario.Usuario;

import java.util.ArrayList;
import java.util.Optional;

public class RepositorioUsuarios {
    private static ArrayList<Usuario> usuariosDB= new ArrayList<>();

    public static  Optional<Usuario> consultarUsuario(String nombre, String apellido) {
        return usuariosDB.stream().filter(usuario ->usuario.getNombre().equals(nombre) && usuario.getApellido().equals(apellido) ).findFirst();
    }

    public static  Optional<Usuario> consultarUsuarioPorCuenta(String cuenta) {
        return usuariosDB.stream().filter(usuario ->usuario.getCuentaRegular().getSerialCuenta().equals(cuenta) ).findFirst();
    }

    public static Usuario agregarUsuario(String nombre,String apellido, String clave) {
        Usuario usuario = new Usuario(nombre, apellido, clave);
        usuariosDB.add(usuario);
        return usuario;
    }


}
