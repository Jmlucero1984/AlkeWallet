package org.josemalucero.servicio;

public interface PasswordEncoder {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String storedHash);
}
