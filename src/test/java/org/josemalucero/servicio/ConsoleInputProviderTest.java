package org.josemalucero.servicio;

import Helpers.RandomStringGenerators;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleInputProviderTest {

    InputStream inputStream = System.in;
    String inputString = "prueba\n";
    Scanner input;
    @Test
    void leerOpcionIntThrowsInputMismatchException() {
        String inputString = RandomStringGenerators.getRandomString(1);
        input= new Scanner(new ByteArrayInputStream(inputString.getBytes()));
        ConsoleInputProvider consoleInputProvider =new ConsoleInputProvider(input);
        assertThrows(InputMismatchException.class,()->consoleInputProvider.leerOpcionInt());
    }
    @Test
    void leerOpcionInt() {
        int inputInt =8 ;
        input= new Scanner(new ByteArrayInputStream(((String.valueOf(inputInt))+"\n").getBytes()));
        ConsoleInputProvider consoleInputProvider =new ConsoleInputProvider(input);
        int obtenido = consoleInputProvider.leerOpcionInt();
        assertEquals(inputInt,obtenido);

    }

    @Test
    void leerOpcionString() {
        String inputString = RandomStringGenerators.getRandomString(8);
        input= new Scanner(new ByteArrayInputStream(inputString.getBytes()));
        ConsoleInputProvider consoleInputProvider =new ConsoleInputProvider(input);
        String obtenido = consoleInputProvider.leerOpcionString();
        assertEquals(inputString,obtenido,"No coinciden los strings de entrada y lo obtenido");
    }

}