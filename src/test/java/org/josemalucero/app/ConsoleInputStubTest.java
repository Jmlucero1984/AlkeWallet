package org.josemalucero.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleInputStubTest {

        ConsoleInputStub consoleInputStub;
    @Test
    void verificarEntradasYSalidasConsoleInputStubTest() {
        String elemento_str_a ="aa";
        String elemento_str_b ="bb";
        String elemento_str_c="cc";
        int elemento_int_a = 0;
        int elemento_int_b = 4;
        int elemento_int_c = 6;
        consoleInputStub = new ConsoleInputStub();
        consoleInputStub.addProximaRespuestaInt(elemento_int_a);
        consoleInputStub.setProximaRespuestaString(elemento_str_a);
        consoleInputStub.addProximaRespuestaInt(elemento_int_b);
        consoleInputStub.setProximaRespuestaString(elemento_str_b);
        consoleInputStub.addProximaRespuestaInt(elemento_int_c);
        consoleInputStub.setProximaRespuestaString(elemento_str_c);
        Assertions.assertAll(
                ()->assertEquals(consoleInputStub.leerOpcionInt(),elemento_int_a),
                ()->assertEquals(consoleInputStub.leerOpcionInt(),elemento_int_b),
                ()->assertEquals(consoleInputStub.leerOpcionString(),elemento_str_a),
                ()->assertEquals(consoleInputStub.leerOpcionInt(),elemento_int_c),
                ()->assertEquals(consoleInputStub.leerOpcionString(),elemento_str_b),
                ()->assertEquals(consoleInputStub.leerOpcionString(),elemento_str_c)
        );
    }

    @Test
    void verificarSeriesDeEntradasYSalidasConsoleInputStubTest() {
        String elemento_str_a ="aa";
        String elemento_str_b ="bb";
        String elemento_str_c="cc";
        int elemento_int_a = 0;
        int elemento_int_b = 4;
        int elemento_int_c = 6;
        consoleInputStub = new ConsoleInputStub();
      consoleInputStub.addSerieDeRespuestasInt(new int[]{elemento_int_a,elemento_int_b,elemento_int_c});
      consoleInputStub.addSerieDeRespuestasString(new String[]{elemento_str_a,elemento_str_b,elemento_str_c});
        Assertions.assertAll(
                ()->assertEquals(consoleInputStub.leerOpcionInt(),elemento_int_a),
                ()->assertEquals(consoleInputStub.leerOpcionInt(),elemento_int_b),
                ()->assertEquals(consoleInputStub.leerOpcionString(),elemento_str_a),
                ()->assertEquals(consoleInputStub.leerOpcionInt(),elemento_int_c),
                ()->assertEquals(consoleInputStub.leerOpcionString(),elemento_str_b),
                ()->assertEquals(consoleInputStub.leerOpcionString(),elemento_str_c)
        );
    }

}