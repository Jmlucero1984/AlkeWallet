package org.josemalucero.servicio;

import Helpers.RandomStringGenerators;
import org.josemalucero.servicio.formatters.FormateadorDeRegistroAImprimir;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormateadorDeRegistroAImprimirTest {



    @Test
    void alinearTextoDerechaEspaciosSuficientes() {
        String textoAAlinear= RandomStringGenerators.getRandomString(8);
        String espaciosEnblanco = "          ";
        String resultadoEsperado=espaciosEnblanco+textoAAlinear;
        String resultadoObtenido = FormateadorDeRegistroAImprimir.alinearTexto(resultadoEsperado.length(),textoAAlinear, FormateadorDeRegistroAImprimir.Alineado.DERECHA);
        assertEquals(resultadoEsperado,resultadoObtenido);
    }

    @Test
    void alinearTextoDerechaEspaciosInsuficientes() {
        String textoAAlinear= RandomStringGenerators.getRandomString(8);
        String resultadoEsperado=textoAAlinear.substring(0,textoAAlinear.length()-3)+"...";
        String resultadoObtenido = FormateadorDeRegistroAImprimir.alinearTexto(textoAAlinear.length(),textoAAlinear, FormateadorDeRegistroAImprimir.Alineado.DERECHA);
        assertEquals(resultadoEsperado,resultadoObtenido);
    }

    @Test
    void alinearTextoIzquierdaEspaciosSuficientes() {
        String textoAAlinear= RandomStringGenerators.getRandomString(8);
        String espaciosEnblanco = "          ";
        String resultadoEsperado=textoAAlinear+espaciosEnblanco;
        String resultadoObtenido = FormateadorDeRegistroAImprimir.alinearTexto(resultadoEsperado.length(),textoAAlinear, FormateadorDeRegistroAImprimir.Alineado.IZQUIERDA);
        assertEquals(resultadoEsperado,resultadoObtenido);
    }

    @Test
    void alinearTextoIzquierdaEspaciosInsuficientes() {
        String textoAAlinear= RandomStringGenerators.getRandomString(8);
        String resultadoEsperado=textoAAlinear.substring(0,textoAAlinear.length()-3)+"...";
        String resultadoObtenido = FormateadorDeRegistroAImprimir.alinearTexto(textoAAlinear.length(),textoAAlinear, FormateadorDeRegistroAImprimir.Alineado.IZQUIERDA);
        assertEquals(resultadoEsperado,resultadoObtenido);
    }

    @Test
    void alinearTextoCentroEspaciosSuficientes() {
        String textoAAlinear= RandomStringGenerators.getRandomString(8);

        String resultadoEsperado="  "+textoAAlinear+"   ";
        String resultadoObtenido = FormateadorDeRegistroAImprimir.alinearTexto(resultadoEsperado.length(),textoAAlinear, FormateadorDeRegistroAImprimir.Alineado.CENTRO);
        assertEquals(resultadoEsperado,resultadoObtenido);
    }

    @Test
    void alinearTextoCentroEspaciosInsuficientes() {
        String textoAAlinear= RandomStringGenerators.getRandomString(8);
        String resultadoEsperado=textoAAlinear.substring(0,textoAAlinear.length()-3)+"...";
        String resultadoObtenido = FormateadorDeRegistroAImprimir.alinearTexto(textoAAlinear.length(),textoAAlinear, FormateadorDeRegistroAImprimir.Alineado.CENTRO);
        assertEquals(resultadoEsperado,resultadoObtenido);
    }
}