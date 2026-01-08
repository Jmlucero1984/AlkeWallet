package org.josemalucero.app;

import org.josemalucero.servicio.providers.InputProvider;

import java.util.LinkedList;
import java.util.Queue;

public class ConsoleInputStub implements InputProvider {
    Queue<Integer> respuestasInt = new LinkedList<>();
    Queue<String> respuestasString = new LinkedList<>();


    public void addProximaRespuestaInt(int proximaRespuestaInt) {
        respuestasInt.add(proximaRespuestaInt);
    }


    public void addSerieDeRespuestasInt(int[] serieDeRespuestas) {
        for (int i = 0; i < serieDeRespuestas.length; i++) {
            respuestasInt.add(serieDeRespuestas[i]);
        }
    }

    public void clearRespuestasString(){
        respuestasString.clear();
    }

    public void clearRespuestasInt(){
        respuestasInt.clear();
    }

    public void addSerieDeRespuestasString(String[] serieDeRespuestas) {
        for (int i = 0; i < serieDeRespuestas.length; i++) {
            respuestasString.add(serieDeRespuestas[i]);
        }
    }


    public void addProximaRespuestaString(String proximaRespuestaString) {
        respuestasString.add(proximaRespuestaString);
    }

    @Override
    public int leerOpcionInt() {
        return respuestasInt.remove();
    }

    @Override
    public String leerOpcionString() {
        return respuestasString.remove();
    }
}
