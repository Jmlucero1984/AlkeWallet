package org.josemalucero.servicio;


/**
 * Las clases que implementan esta interfaz proveen un método para aplicar una función hash sobre un String que
 * representa una contraseña y retornar una cadena de caracteres como resultado, y otro método para corroborar
 * la coincidencia de contraseñas, la hash almacenada y otra en texto plano.
 * @author José María Lucero
 */
public interface PasswordEncoder {
    /**
     *  Aplica la función de hash sobre la contraseña en texto plano.
     * @param rawPassword
     * @return {@link String} como resultado de aplicar el hash.
     */
    String hash(String rawPassword);

    /**
     * Comprueba la coincidencia entre una cadena hasheada y una contraseña en texto plano.
     * @param rawPassword
     * @param storedHash
     * @return {@code bool} que indica el resultado de comprobar la coincidencia.
     */
    boolean matches(String rawPassword, String storedHash);
}
