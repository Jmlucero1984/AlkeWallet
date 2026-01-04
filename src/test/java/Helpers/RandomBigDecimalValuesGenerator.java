package Helpers;

import java.util.Random;

public class RandomBigDecimalValuesGenerator {
    public static String generarBigDecimal(int minimo, int maximo){
        if (maximo<=minimo){
            throw new RuntimeException("No están bien definidos los limites para generar el BigDecimal al azar");
        }
        Random random = new Random();
        int valorEntero = random.nextInt(maximo-minimo)+minimo;
        int valorDecimas = random.nextInt(10);
        int valorCentesimas = random.nextInt(10);
        return ""+valorEntero+"."+valorDecimas+""+valorCentesimas;
    }
}
