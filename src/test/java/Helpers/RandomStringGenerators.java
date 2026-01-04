package Helpers;

import java.util.Random;
/**
 * Fábrica para generar cadenas de caracteres aleatorios
 * <p>
 * Proporciona una forma de generar nombres y claves
 * aleatorias.
 * </p>
 *
 */
public class RandomStringGenerators {

    /**
     * Genera una cadena de caracteres aleatorios de largo length
     *
     * @param length valor exacto del largo en caracteres devuelto
     * @return String aleatorio con letras de la a la z
     * @see Random
     */
    public static String getRandomString(int length){
        Random random = new Random();
        char[] chainOfChars = new char[length];
        for (int i = 0; i < length; i++) {
            // 'a' = 97, 'z' = 122
            chainOfChars[i] = (char) ('a' + random.nextInt(26));
        }

        return new String(chainOfChars);
    }

}
