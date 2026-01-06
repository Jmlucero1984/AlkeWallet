package org.josemalucero.servicio;
import org.mindrot.jbcrypt.BCrypt;


/**
 * Clase que implementa los métodos de la clase BCrypt. El mismo implementa "salt" y está
 * diseñado para entorpecer los ataques de fuerza bruta.
 * @author José María Lucero
 */
public class BCryptPasswordEncoderService implements PasswordEncoder {

    /**
     * {@inheritDoc}
     * @param rawPassword
     * @return {@inheritDoc}
     */
    @Override
    public String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
    }

    /**
     * {@inheritDoc}
     * @param rawPassword
     * @param storedHash
     * @return {@inheritDoc}
     */
    @Override
    public boolean matches(String rawPassword, String storedHash) {
        return BCrypt.checkpw(rawPassword, storedHash);
    }
}