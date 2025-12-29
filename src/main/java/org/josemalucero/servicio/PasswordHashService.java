package org.josemalucero.servicio;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

public class PasswordHashService {
    /**
     * Genera un hash SHA256 de una contraseña sin salt
     * Nota: Para mayor seguridad, usa la versión con salt
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Versión más segura con salt (recomendada)
     */
    public static String hashPasswordWithSalt(String password) {
        try {
            // Generar salt aleatorio
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Combinar password con salt
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hash = digest.digest(password.getBytes());

            // Retornar salt + hash (para almacenamiento)
            byte[] combined = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hash, 0, combined, salt.length, hash.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verificar password con salt
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Decodificar el hash almacenado
            byte[] combined = Base64.getDecoder().decode(storedHash);

            // Extraer salt (primeros 16 bytes)
            byte[] salt = new byte[16];
            System.arraycopy(combined, 0, salt, 0, salt.length);

            // Calcular hash del password proporcionado con el mismo salt
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hashToVerify = digest.digest(password.getBytes());

            // Comparar hashes
            for (int i = 0; i < hashToVerify.length; i++) {
                if (hashToVerify[i] != combined[i + salt.length]) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        HexFormat hex = HexFormat.of();
        return hex.formatHex(bytes);
    }
}
