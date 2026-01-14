package org.josemalucero.servicio.repositorios;

import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.RegistroOperacion;
import org.josemalucero.dominio.usuario.Credencial;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.passwords.BCryptPasswordEncoderService;
import org.josemalucero.servicio.providers.Messages;

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
     * Agrega el usuario al {@code ArrayList<Usuario>}.
     * @param usuario
     */
    public static void agregarUsuario(Usuario usuario) {
        usuariosDB.add(usuario);
    }

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
    public static Usuario crearYAgregarUsuario(String nombre, String apellido, String claveHasheada) {
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
    public static Usuario crearYAgregarUsuarioYAsignarCuenta(String nombre, String apellido, String claveHasheada, String codigoMoneda) {
        Usuario usuario = new Usuario(nombre, apellido, claveHasheada);
        MonedaConvertible moneda = RepositorioMonedas.encontrarMonedaPorCodigo(codigoMoneda);
        usuario.crearCuentRegular().setMoneda(moneda);
        usuario.getCuentaRegular().registrarOperacion(new RegistroOperacion(Messages.get("apertura.cuenta.regular.en") +" "+moneda.getNombre(), BigDecimal.ZERO,BigDecimal.ZERO));
        usuariosDB.add(usuario);
        return usuario;
    }

    /**
     * Crea un nuevo usuario con nombre, apellido y clave hasheada, crea una moneda, una cuenta de usuario a la
     * cual asignar a esta última, y finalmente agrega el usuario al {@code ArrayList<Usuario>}.
     * @param credencial
     * @param codigoMoneda
     * @return {@link Usuario} que ha sido agregado.
     */
    public static Usuario crearYAgregarUsuarioYAsignarCuenta(Credencial credencial, String codigoMoneda) {

        Usuario usuario = new Usuario(credencial.getNombre(), credencial.getApellido(), new BCryptPasswordEncoderService().hash(credencial.getClave()));
        MonedaConvertible moneda = RepositorioMonedas.encontrarMonedaPorCodigo(codigoMoneda);
        usuario.crearCuentRegular().setMoneda(moneda);
        usuario.getCuentaRegular().registrarOperacion(new RegistroOperacion(Messages.get("apertura.cuenta.regular.en") +" "+moneda.getNombre(), BigDecimal.ZERO,BigDecimal.ZERO));
        usuariosDB.add(usuario);
        return usuario;
    }

    /**
     * Genera algunos usuarios ficticios para poder
     * hacer uso de la app con una base mínima.
     * @author José Maria Lucero
     */

    public static void createSomeUsers() {
        BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
        RepositorioUsuarios.crearYAgregarUsuarioYAsignarCuenta("Juan", "Lucero", bCryptPasswordEncoderService.hash("Juanlucero"),"ARS");
        RepositorioUsuarios.crearYAgregarUsuarioYAsignarCuenta("Mario", "Moya", bCryptPasswordEncoderService.hash("Mariomoya"),"CLP");
        RepositorioUsuarios.crearYAgregarUsuarioYAsignarCuenta("Javiera", "Rojas", bCryptPasswordEncoderService.hash("Javierarojas"),"CLP");
    }

    /**
     * Vacía el repositorio de Usuarios.
     */
    public static void clearUsuariosDB() {
        usuariosDB.clear();
    }


}
