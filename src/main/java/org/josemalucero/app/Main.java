package org.josemalucero.app;



import org.josemalucero.BasicConsolePassword;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.*;

import java.io.Console;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        new AlkeWallet(new ContextoUsuario(new ConsoleInputProvider())).run();


    }






}