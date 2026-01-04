package org.josemalucero.servicio;

import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.RegistroOperacion;
import org.josemalucero.dominio.usuario.Usuario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class RepositorioUsuarios {
    private static ArrayList<Usuario> usuariosDB= new ArrayList<>();

    public static  Optional<Usuario> consultarUsuario(String nombre, String apellido) {
        return usuariosDB.stream().filter(usuario ->usuario.getNombre().equals(nombre) && usuario.getApellido().equals(apellido) ).findFirst();
    }

    public static  Optional<Usuario> consultarUsuarioPorCuenta(String cuenta) {
        /* Si no se verifica primero la existencia de la cuenta regular, lanzará un error al tratar de hacer
        una transferencia, ya que no todos los usuarios inician con cuenta asignada
        */
        return usuariosDB.stream().filter(usuario ->usuario.getCuentaRegular()!=null &&
                usuario.getCuentaRegular().getSerialCuenta().equals(cuenta) ).findFirst();
    }

    public static Usuario agregarUsuario(String nombre,String apellido, String clave) {
        Usuario usuario = new Usuario(nombre, apellido, clave);
        usuariosDB.add(usuario);
        return usuario;
    }

    public static Usuario agregarUsuarioYAsignarCuenta(String nombre,String apellido, String clave, String codigoMoneda) {
        Usuario usuario = new Usuario(nombre, apellido, clave);
        MonedaConvertible moneda = RepositorioMonedas.encontrarMonedaPorCodigo(codigoMoneda);
        usuario.crearCuentRegular().setMoneda(moneda);
        usuario.getCuentaRegular().registrarOperacion(new RegistroOperacion("Apertura Cuenta Regular en "+moneda.getNombre(), BigDecimal.ZERO,BigDecimal.ZERO));
        usuariosDB.add(usuario);
        return usuario;
    }

    public static void clearUsuariosDB() {
        usuariosDB.clear();
    }


}
