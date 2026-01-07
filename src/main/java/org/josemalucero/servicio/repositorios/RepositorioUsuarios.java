package org.josemalucero.servicio.repositorios;

import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.RegistroOperacion;
import org.josemalucero.dominio.usuario.Usuario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
/**
 * Provee un recurso de almacenamiento y consulta de usuarios para toda la aplicación.
 * @author José María Lucero
 */

public class RepositorioUsuarios {

    private static ArrayList<Usuario> usuariosDB= new ArrayList<>();

    /**
     * Busca un determinado usuario por su nombre y apellido.
     * @param nombre
     * @param apellido
     * @return {@code Optional<Usuario>} como resultado de la búsqueda.
     */
    public static  Optional<Usuario> consultarUsuario(String nombre, String apellido) {
        return usuariosDB.stream().filter(usuario ->usuario.getNombre().equals(nombre) && usuario.getApellido().equals(apellido) ).findFirst();
    }

    /**
     * Busca un determinado usuario por su N° de Cuenta.
     * @param cuenta
     * @return {@code Optional<Usuario>} como resultado de la búsqueda.
     */
    public static  Optional<Usuario> consultarUsuarioPorCuenta(String cuenta) {
        /* Si no se verifica primero la existencia de la cuenta regular, lanzará un error al tratar de hacer
        una transferencia, ya que no todos los usuarios inician con cuenta asignada
        */
        return usuariosDB.stream().filter(usuario ->usuario.getCuentaRegular()!=null &&
                usuario.getCuentaRegular().getSerialCuenta().equals(cuenta) ).findFirst();
    }

    /**
     * Crea un nuevo usuario con nombre, apellido y clave hasheada, lo agrega al {@code ArrayList<Usuario>}.
     * @param nombre
     * @param apellido
     * @param claveHasheada
     * @return {@link Usuario} que ha sido agregado.
     */
    public static Usuario agregarUsuario(String nombre,String apellido, String claveHasheada) {
        Usuario usuario = new Usuario(nombre, apellido, claveHasheada);
        usuariosDB.add(usuario);
        return usuario;
    }

/**
     * Crea un nuevo usuario con nombre, apellido y clave hasheada, crea una moneda, una cuenta de usuario a la
     * cual asignar a esta última, y finalmente agrega el usuario al {@code ArrayList<Usuario>}.
     * @param nombre
     * @param apellido
     * @param claveHasheada
     * @param codigoMoneda
     * @return {@link Usuario} que ha sido agregado.
     */
    public static Usuario agregarUsuarioYAsignarCuenta(String nombre,String apellido, String claveHasheada, String codigoMoneda) {
        Usuario usuario = new Usuario(nombre, apellido, claveHasheada);
        MonedaConvertible moneda = RepositorioMonedas.encontrarMonedaPorCodigo(codigoMoneda);
        usuario.crearCuentRegular().setMoneda(moneda);
        usuario.getCuentaRegular().registrarOperacion(new RegistroOperacion("Apertura Cuenta Regular en "+moneda.getNombre(), BigDecimal.ZERO,BigDecimal.ZERO));
        usuariosDB.add(usuario);
        return usuario;
    }

    /**
     * Vacía el repositorio de Usuarios.
     */
    public static void clearUsuariosDB() {
        usuariosDB.clear();
    }


}
