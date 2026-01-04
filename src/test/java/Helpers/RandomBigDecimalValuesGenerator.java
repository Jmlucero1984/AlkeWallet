package Helpers;

import java.util.Random;

/**
 * Fábrica para generar valores BigDecimal aleatorios.
 * <p>
 * Proporciona una forma de generar números BidDecimal
 * aleatorios dentro de rangos específicos.
 * </p>
 *  @author José María Lucero
 */
public class RandomBigDecimalValuesGenerator {
    /**
     * Genera un BigDecimal aleatorio dentro de un rango con decimales específicos.
     *
     * @param minimo valor mínimo inclusive
     * @param maximo valor máximo no inclusive
     * @return String que representa un BigDecimal aleatorio con enteros, décimas y centésimas
     * @throws IllegalArgumentException si maximo<=minimo
     * @see Random
     * @author José María Lucero
     */
    public static String generarBigDecimal(int minimo, int maximo){
        if (maximo<=minimo){
            throw new IllegalArgumentException("No están bien definidos los limites para generar el BigDecimal al azar");
        }
        Random random = new Random();
        int valorEntero = random.nextInt(maximo-minimo)+minimo;
        int valorDecimas = random.nextInt(10);
        int valorCentesimas = random.nextInt(10);
        return ""+valorEntero+"."+valorDecimas+""+valorCentesimas;
    }
}
