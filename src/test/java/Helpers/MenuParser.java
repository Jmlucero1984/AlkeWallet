package Helpers;

import java.util.Arrays;

public class MenuParser {

    public static String getOptionNumber(String resourceMessage, String menu){
        String[] lineasDelMenu = menu.split("\n");
        String lineaObjetivo= Arrays.stream(lineasDelMenu).filter(t->t.contains(resourceMessage)).findAny().get();
        return lineaObjetivo.substring(0,lineaObjetivo.indexOf("."));
    }
}
