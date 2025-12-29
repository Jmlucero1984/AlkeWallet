package org.josemalucero.app;



import org.josemalucero.BasicConsolePassword;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.BCryptPasswordEncoderService;
import org.josemalucero.servicio.RepositorioMonedas;
import org.josemalucero.servicio.RepositorioUsuarios;
import org.josemalucero.servicio.RespositorioCuentas;

import java.io.Console;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
//        System.out.println("====== Alke Wallet ======");
//        //BasicConsolePassword.introducirPassword();
//        Scanner input = new Scanner(System.in);
//        Console console = System.console();
//        boolean salir = false;
//        while(!salir) {
//            mostrarMenu();
//            int opcion = obtenerOpcion(input);
//            switch(opcion) {
//                case 1:
//                    logInUsuario(input,console);
//                    break;
//                case 2:
//                    signInUsuario(input,console);
//                    break;
//                case 3:
//                    System.out.println("Hasta luego!");
//                    salir = true;
//                    break;
//                default:
//                    System.out.println("Opción no válida. Intente de nuevo.");
//                    break;
//            }
//
//        }
//        input.close();
//    }
        createSomeUsers();
        RepositorioMonedas.crearMonedasBasicas();
        BigDecimal ratio = new BigDecimal("0.85663");
        BigDecimal valorMoneda = new BigDecimal(10000.00);
        System.out.println(valorMoneda.multiply(ratio).setScale(2, RoundingMode.HALF_UP));
        Scanner scanner = new Scanner(System.in);
        ContextoUsuario contexto = new ContextoUsuario(scanner);

        System.out.println("=== BIENVENIDO A BILLETERA VIRTUAL ===");
        System.out.println(RespositorioCuentas.generarSerial(1,2));
        System.out.println(RespositorioCuentas.generarSerial(2,3));
        System.out.println(RespositorioCuentas.generarSerial(1,10));
        System.out.println(RespositorioCuentas.generarSerial(4,2));


        while (true) {
            try {
                contexto.mostrarMenu();
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                contexto.procesarOpcion(opcion);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Limpiar buffer en caso de error
            }
        }
    }

    public static void createSomeUsers(){
        BCryptPasswordEncoderService bCryptPasswordEncoderService = new BCryptPasswordEncoderService();
        RepositorioUsuarios.agregarUsuario("J", "P",bCryptPasswordEncoderService.hash("JP"));
        RepositorioUsuarios.agregarUsuario("M", "M",bCryptPasswordEncoderService.hash("MM"));
    }



    public static void mostrarMenu() {
        System.out.println("\n----- Menú de Opciones-----");
        System.out.println("1. Log In");
        System.out.println("2. Sign In");
        System.out.println("3. Salir");
        System.out.println("Ingrese su opción: ");
    }



    public static void signInUsuario(Scanner input, Console console){
        String nombre="";
        String apellido="";
        input.nextLine(); //Limpiar buffer
        System.out.println("----- NUEVO USUARIO -----");
        System.out.println("Introduzca su nombre: ");
        nombre = input.nextLine();
        System.out.println("Introduzca su apellido: ");
        apellido = input.nextLine();
        Optional<Usuario> usuarioExistente = RepositorioUsuarios.consultarUsuario(nombre,apellido);
        if(usuarioExistente.isPresent()){
            System.out.println("Ya existe un usuario con el nombre: "+usuarioExistente.get().getNombreCompleto());
        } else {
            System.out.println("Introduzca su clave: ");
            char[] passwordArray = console.readPassword("Contraseña (espacios): ");
            String clave = new String(passwordArray);
            // Limpiar el array de caracteres por seguridad
            java.util.Arrays.fill(passwordArray, ' ');

            Usuario nuevoUsuario = RepositorioUsuarios.agregarUsuario(nombre, apellido,clave);
            System.out.println("El usuario "+nuevoUsuario.getNombreCompleto() + " ha sido creado");
        }
    }


    public static void logInUsuario(Scanner input,Console console ){
        String nombre ="";
        String apellido ="";
        input.nextLine(); //Limpiar buffer
        System.out.println("----- LOG IN USUARIO -----");
        System.out.println("Introduzca su nombre: ");
        nombre= input.nextLine();
        System.out.println("Introduzca su apellido: ");
        apellido= input.nextLine();
        Optional<Usuario> usuarioExistente = RepositorioUsuarios.consultarUsuario(nombre,apellido);
        if(usuarioExistente.isPresent()){
            System.out.println("Ingrese su clave: ");
            char[] passwordArray = console.readPassword("Contraseña (espacios): ");
            String clave = new String(passwordArray);
            // Limpiar el array de caracteres por seguridad
            java.util.Arrays.fill(passwordArray, ' ');
            if(clave.equals(usuarioExistente.get().getClave())){
                System.out.println("LOGUEO EXITOSO");

            }

        } else {
            System.out.println("El usuario "+nombre+" "+apellido+" "+" no existe en la Base de Datos");
        }
    }


    public static int obtenerOpcion(Scanner input) {
        while(!input.hasNextInt()) {
            System.out.println("Por favor, ingrese un número válido: ");
            input.next(); // Limpiar el buffer


        }
        return input.nextInt();
    }

}