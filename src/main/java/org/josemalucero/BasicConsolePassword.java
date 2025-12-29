package org.josemalucero;

import java.io.Console;

public class BasicConsolePassword {



    public static  void introducirPassword() {
        Console console = System.console();
        if (console == null) {
            System.out.println("No se puede obtener la consola. Ejectua desde la terminal/cmd");
            System.exit(1);
        }

        System.out.println("=== SISTEMA DE LOGIN ====");
        String username = console.readLine("Usuario: ");
        // Leer contraseña (no se muestra)
        char[] passwordArray = console.readPassword("Contraseña (espacios): ");
        String password = new String(passwordArray);

        // Limpiar el array de caracteres por seguridad
        java.util.Arrays.fill(passwordArray, ' ');

        // Verificar credenciales (ejemplo simple)
        if (autenticar(username, password)) {
            System.out.println("\n¡Login exitoso! Bienvenido " + username);
        } else {
            System.out.println("\nError: Credenciales incorrectas");
        }
    }

    private static boolean autenticar(String user, String pass) {
        // Ejemplo: usuario "admin" y contraseña "1234"
        return user.equals("admin") && pass.equals("1234");
    }


}

