package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.cuenta.Transferible;
import org.josemalucero.dominio.operacion.OperacionConsulta;
import org.josemalucero.dominio.operacion.OperacionDeposito;
import org.josemalucero.dominio.operacion.OperacionRetiro;
import org.josemalucero.dominio.operacion.OperacionTransferencia;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.RepositorioUsuarios;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

public class EstadoOperaciones implements EstadoUsuario {

    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
         // Método helper
        System.out.println("Bienvenido, " + contexto.getUsuarioLogueado().getNombreCompleto());
        System.out.println("1. Consultar saldo");
        System.out.println("2. Depositar dinero");
        System.out.println("3. Retirar dinero");
        System.out.println("4. Transferir dinero");
        System.out.println("5. Ver historial de transacciones");
        System.out.println("6. Cerrar sesión (Sign Out)");
        System.out.print("Seleccione una opción: ");
    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contexto) {
        switch (opcion) {
            case 1:
                consultarSaldo(contexto);
                break;

            case 2:
                depositarDinero(contexto);
                break;

            case 3:
                retirarDinero(contexto);
                break;

            case 4:
                transferirDinero(contexto);
                break;

            case 5:
                verHistorial(contexto.getUsuarioLogueado());
                break;

            case 6:
                System.out.println("Cerrando sesión...");
                contexto.cerrarSesion();
                break;

            default:
                System.out.println("Opción inválida");
        }
    }

    @Override
    public String getNombreEstado() {
        return "OPERACIONES";
    }



    private void consultarSaldo(ContextoUsuario contextoUsuario) {
        OperacionConsulta operacionConsulta = new OperacionConsulta(contextoUsuario.getUsuarioLogueado().getCuentaRegular());
        operacionConsulta.ejecutar();
        System.out.println("SERIAL CUENTA: "+contextoUsuario.getUsuarioLogueado().getCuentaRegular().getSerialCuenta());
    }

    private void depositarDinero(ContextoUsuario contextoUsuario) {
        System.out.println("DEPOSITAR EN CUENTA");
        Optional<BigDecimal> cifraVerificada;
        boolean operacionExitosa=false;
        while(!operacionExitosa) {
            cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getScanner()));
            if(cifraVerificada.isEmpty()){
                return;
            } else {
                OperacionDeposito operacionDeposito = new OperacionDeposito(contextoUsuario.getUsuarioLogueado().getCuentaRegular(),cifraVerificada.get());
                if (operacionDeposito.preValidar()) {
                    operacionDeposito.ejecutar();
                    if(operacionDeposito.posValidar()){
                        operacionExitosa = true;
                        System.out.println("DEPOSITO REALIZADO");
                    }

                }
            }
        }
    }

    private void retirarDinero(ContextoUsuario contextoUsuario) {
        System.out.println("RETIRAR DE CUENTA");
        Optional<BigDecimal> cifraVerificada;
        boolean operacionExitosa=false;
        while(!operacionExitosa) {
            cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getScanner()));
            if(cifraVerificada.isEmpty()){
                return;
            } else  {
                OperacionRetiro operacionRetiro = new OperacionRetiro(contextoUsuario.getUsuarioLogueado().getCuentaRegular(),cifraVerificada.get());
                if (operacionRetiro.preValidar()){
                    operacionRetiro.ejecutar();
                    if(operacionRetiro.posValidar()) {
                        operacionExitosa = true;
                        System.out.println("RETIRO REALIZADO");
                    }
                }
            }
        }
    }
    private void transferirDinero(ContextoUsuario contextoUsuario) {
        System.out.println("TRANSFERIR A CUENTA");
        Optional<Usuario> usuarioDestino = manejarEntradaDeNumeroCuenta(contextoUsuario.getScanner());
        if(usuarioDestino.isEmpty()){
            return;
        } else  {
            System.out.println("La cuenta destino pertenece a: " + usuarioDestino.get().getNombreCompleto());
            if (!(usuarioDestino.get().getCuentaRegular() instanceof Transferible)) {
                System.out.println("La cuenta destino no puede recibir transferencias");
                return;
            }
            Optional<BigDecimal> cifraVerificada;
            boolean operacionExitosa = false;
            while (!operacionExitosa) {
                cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getScanner()));
                if(cifraVerificada.isEmpty()) {
                    return;
                } else {
                    OperacionTransferencia operacionTransferencia = new OperacionTransferencia(contextoUsuario.getUsuarioLogueado().getCuentaRegular(), usuarioDestino.get().getCuentaRegular(), cifraVerificada.get());
                    if (operacionTransferencia.preValidar() ){
                        operacionTransferencia.ejecutar();
                        if(operacionTransferencia.posValidar()) {
                            operacionExitosa = true;
                            System.out.println("TRANSFERENCIA REALIZADA");
                        } else {
                            operacionTransferencia.restaurarEstadoAnterior();
                        }
                    }
                }
            }
        }
    }
//    private void transferirDinero(ContextoUsuario contextoUsuario) {
//        System.out.println("TRANSFERIR A CUENTA");
//        Usuario usuarioDestino = manejarEntradaDeNumeroCuenta(contextoUsuario.getScanner());
//        if(usuarioDestino!=null) {
//            System.out.println("La cuenta destino pertenece a: "+usuarioDestino.getNombreCompleto());
//            Optional<BigDecimal> cifraVerificada;
//            boolean operacionExitosa=false;
//            while(!operacionExitosa) {
//                cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getScanner()));
//                if (cifraVerificada.isPresent()) {
//                    operacionExitosa = contextoUsuario.getUsuarioLogueado().getCuentaRegularPesos().retirar(cifraVerificada.get());
//                    if(operacionExitosa) {
//                        usuarioDestino.getCuentaRegularPesos().depositar(cifraVerificada.get());
//                        System.out.println("TRANSFERENCIA REALIZADA");
//                    }
//                } else {
//                    return;
//                }
//            }
//
//        }
//
//    }

    private void verHistorial(Usuario usuario) {
        System.out.println("Mostrando historial...");
        // Lógica real aquí
    }
    private Optional<Usuario> manejarEntradaDeNumeroCuenta(Scanner scanner){
        boolean cuentaValida = false;

        while(!cuentaValida){
            System.out.println("Introducir numero de cuenta destino | ESC para salir.");
            String textoIntroducido = scanner.nextLine();
            if(textoIntroducido.equalsIgnoreCase("ESC")) return null;
            Optional<Usuario> usuario = RepositorioUsuarios.consultarUsuarioPorCuenta(textoIntroducido);
            if (usuario.isPresent()) {
                return Optional.of(usuario.get());
            }
            System.out.println("No existe el numero de cuenta, intente nuevamente...");
        }
        return  null;
    }
    private BigDecimal manejarEntradaDeCifraMonetaria(Scanner scanner) {

        boolean cantidadVálida = false;

        while(!cantidadVálida){
            System.out.println("Introducir cantidad con enteros y centavos $$$.$$ | ESC para salir.");
            String cantidadIntroducida = scanner.nextLine();
            if(cantidadIntroducida.equalsIgnoreCase("ESC")) return null;
            cantidadVálida=cantidadIntroducida.matches("^\\d+\\.\\d{2}$");
            if (cantidadVálida) {
                return new BigDecimal(cantidadIntroducida);
            }
            System.out.println("Cantidad inválida, intente nuevamente...");
        }
        return  null;
    }


}