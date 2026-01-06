package org.josemalucero.servicio;

import java.util.Scanner;

/**
 * Fachada/Adaptador para la entrada por consola usando Scanner.
 * <p>
 * Proporciona una interfaz simplificada y controlada para leer
 * diferentes tipos de entrada desde la consola.
 * </p>
 *
 * @see java.util.Scanner
 * @see InputProvider
 * @author José María Lucero
 */
public class ConsoleInputProvider  implements InputProvider{
    private Scanner scanner = new Scanner(System.in);

    public ConsoleInputProvider() {
    }
    public ConsoleInputProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int leerOpcionInt() {
        int entrada = scanner.nextInt();
        scanner.nextLine();
        return entrada;


    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */

    @Override
    public String leerOpcionString() {
        String entrada = scanner.nextLine();
        return entrada;
    }
}
