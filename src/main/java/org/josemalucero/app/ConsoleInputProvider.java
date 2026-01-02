package org.josemalucero.app;

import java.util.Scanner;

public class ConsoleInputProvider  implements InputProvider{
    private final Scanner scanner = new Scanner(System.in);
    @Override
    public int leerOpcionInt() {
        int entrada = scanner.nextInt();
        scanner.nextLine();
        return entrada;


    }

    @Override
    public String leerOpcionString() {
        String entrada = scanner.nextLine();
        return entrada;
    }
}
