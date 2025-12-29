package org.josemalucero.servicio;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordEncoderService implements PasswordEncoder {

    @Override
    public String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
    }

    @Override
    public boolean matches(String rawPassword, String storedHash) {
        return BCrypt.checkpw(rawPassword, storedHash);
    }
}