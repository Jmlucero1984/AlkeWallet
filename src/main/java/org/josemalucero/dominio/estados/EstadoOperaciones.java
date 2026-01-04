package org.josemalucero.dominio.estados;

import org.josemalucero.servicio.InputProvider;
import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.cuenta.Transferible;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.operacion.*;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.FormateadorDeRegistroAImprimir;
import org.josemalucero.servicio.RepositorioUsuarios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class EstadoOperaciones implements EstadoUsuario {

    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        System.out.println("Bienvenido, " + contexto.getUsuarioLogueado().getNombreCompleto());
        System.out.println("1. Consultar datos cuenta");
        System.out.println("2. Consultar saldo");
        System.out.println("3. Depositar dinero");
        System.out.println("4. Retirar dinero");
        System.out.println("5. Transferir dinero");
        System.out.println("6. Consultar conversión entre monedas");
        System.out.println("7. Convertir cuenta a otra moneda");
        System.out.println("8. Ver historial de transacciones");
        System.out.println("9. Cerrar sesión (Sign Out)");
        System.out.print("Seleccione una opción: ");
    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contexto) {
        switch (opcion) {
            case 1:
                consultarDatosCuenta(contexto);
                break;
            case 2:
                consultarSaldo(contexto);
                break;

            case 3:
                depositarDinero(contexto);
                break;

            case 4:
                retirarDinero(contexto);
                break;

            case 5:
                transferirDinero(contexto);
                break;

            case 6:
                consultarConversionMoneda(contexto);
                break;
            case 7:
                convertirCuentaAOtraMoneda(contexto);
                break;

            case 8:
                verHistorial(contexto);
                break;

            case 9:
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
    }

    private void depositarDinero(ContextoUsuario contextoUsuario) {
        System.out.println("DEPOSITAR EN CUENTA");
        Optional<BigDecimal> cifraVerificada;
        CuentaRegular cuentaRegular = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        boolean operacionExitosa=false;
        while(!operacionExitosa) {
            cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
            if(cifraVerificada.isEmpty()){
                return;
            } else {
                OperacionDeposito operacionDeposito = new OperacionDeposito(cuentaRegular,cifraVerificada.get());
                if (operacionDeposito.preValidar()) {
                    operacionDeposito.ejecutar();
                    if(operacionDeposito.posValidar()){
                        operacionExitosa = true;
                        operacionDeposito.registrar(cuentaRegular);
                        System.out.println("DEPOSITO REALIZADO");
                    }

                }
            }
        }
    }

    private void consultarDatosCuenta(ContextoUsuario contextoUsuario){
        System.out.println(contextoUsuario.getUsuarioLogueado().getNombreCompleto());
        System.out.println("Cuenta en "+contextoUsuario.getUsuarioLogueado().getCuentaRegular().getMonedaConvertible().getNombre());
        System.out.println("N° Cuenta: "+contextoUsuario.getUsuarioLogueado().getCuentaRegular().getSerialCuenta());
    }

    private void convertirCuentaAOtraMoneda(ContextoUsuario contextoUsuario){
        contextoUsuario.cambiarEstado(new EstadoConversionCuenta());
    }

    private void consultarConversionMoneda(ContextoUsuario contextoUsuario){
        contextoUsuario.cambiarEstado(new EstadoConversionMonedas());
    }

    private void retirarDinero(ContextoUsuario contextoUsuario) {
        System.out.println("RETIRAR DE CUENTA");
        Optional<BigDecimal> cifraVerificada;
        CuentaRegular cuentaRegular = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        boolean operacionExitosa=false;
        while(!operacionExitosa) {
            cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
            if(cifraVerificada.isEmpty()){
                return;
            } else  {
                OperacionRetiro operacionRetiro = new OperacionRetiro(cuentaRegular,cifraVerificada.get());
                if (operacionRetiro.preValidar()){
                    operacionRetiro.ejecutar();
                    if(operacionRetiro.posValidar()) {
                        operacionExitosa = true;
                        operacionRetiro.registrar(cuentaRegular);
                        System.out.println("RETIRO REALIZADO");
                    }
                }
            }
        }
    }

    private void transferirDinero(ContextoUsuario contextoUsuario) {
        System.out.println("TRANSFERIR A CUENTA");
        Optional<Usuario> usuarioDestino = manejarEntradaDeNumeroCuenta(contextoUsuario.getConsoleInputProvider());
        if(usuarioDestino!=null && !usuarioDestino.isEmpty()){
            operarSobreCuentaParaTransferir(contextoUsuario,usuarioDestino.get());
        }
    }

    private void operarSobreCuentaParaTransferir(ContextoUsuario contextoUsuario,Usuario usuarioDestino){
        System.out.println("La cuenta destino pertenece a: " + usuarioDestino.getNombreCompleto());
        CuentaRegular cuentaRegular =contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        if(usuarioDestino.equals(contextoUsuario.getUsuarioLogueado())){
            System.out.println("|||| No le parece sin sentido transferirse a usted mismo? ||||");
            return;
        }

        if (!(usuarioDestino.getCuentaRegular() instanceof Transferible)) {
            System.out.println("La cuenta destino no puede recibir transferencias");
            return;
        }
        int opcion = 0;

        if(!usuarioDestino.getCuentaRegular().getMonedaConvertible().getCodigo().equals(cuentaRegular.getMonedaConvertible().getCodigo())){
            System.out.println("La cuenta destino está en una moneda diferente ("+cuentaRegular.getMonedaConvertible().getCodigo()+")");
            String[] opciones = new String[]{"Monto en moneda de su propia cuenta","Monto en moneda de la cuenta destino"};
            String titulo = "Seleccione opción para transferencia entre cuentas";
            int eleccion = seleccionMultipleGenerica(contextoUsuario.getConsoleInputProvider(),titulo, opciones);
            if(eleccion==-1) return;
            opcion = eleccion;
        }

        Optional<BigDecimal> cifraVerificada=null;
        cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
        if(cifraVerificada==null) {return;}

        OperacionTransferencia operacionTransferencia = new OperacionTransferencia(cuentaRegular, usuarioDestino.getCuentaRegular(), cifraVerificada.get());
        switch (opcion) {
            case 1:
                operacionTransferencia = new OperacionTransferenciaMonedaOrigen(cuentaRegular, usuarioDestino.getCuentaRegular(), cifraVerificada.get(),new ConversorMoneda());
                break;
            case 2:
                operacionTransferencia = new OperacionTransferenciaMonedaDestino(cuentaRegular, usuarioDestino.getCuentaRegular(), cifraVerificada.get(),new ConversorMoneda());
                break;
        }

        if (operacionTransferencia.preValidar() ){
            operacionTransferencia.ejecutar();
            if(operacionTransferencia.posValidar()) {
                operacionTransferencia.registrar(cuentaRegular);
                System.out.println("TRANSFERENCIA REALIZADA");
            } else {
                operacionTransferencia.restaurarEstadoAnterior();
            }
        }
    }




    private void verHistorial(ContextoUsuario contextoUsuario) {
        System.out.println("Mostrando historial...");
        ArrayList<RegistroOperacion> operacionesHistoricas = contextoUsuario.getUsuarioLogueado().getCuentaRegular().getHistorialOperaciones();
        System.out.println(FormateadorDeRegistroAImprimir.generarCabeceras(FormateadorDeRegistroAImprimir.Alineado.CENTRO));
        operacionesHistoricas.forEach(t->System.out.println(FormateadorDeRegistroAImprimir.formatearRegistro(t, FormateadorDeRegistroAImprimir.Alineado.CENTRO)));

    }


    private int seleccionMultipleGenerica(InputProvider consoleInputProvider,String titulo,String[] opciones){
        while(true) {
            System.out.println(titulo+" | ESC para salir.");
            for(int i=0;i<opciones.length;i++){
                System.out.println((i+1)+". "+opciones[i]);
            }

            String textoIntroducido = consoleInputProvider.leerOpcionString();
            if (textoIntroducido.equalsIgnoreCase("ESC")) return -1;
            int opcion;
            try {
                opcion =  Integer.parseInt(textoIntroducido);
                if(opcion>0 && opcion<opciones.length+1){
                    return opcion;
                } else {
                    System.out.println("Opción invalida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción invalida");
            }
        }
    }

    private Optional<Usuario> manejarEntradaDeNumeroCuenta(InputProvider consoleInputProvider){
        boolean cuentaValida = false;
        while(!cuentaValida){
            System.out.println("Introducir numero de cuenta destino | ESC para salir.");
            String textoIntroducido = consoleInputProvider.leerOpcionString();
            if(textoIntroducido.equalsIgnoreCase("ESC")) return null;
            Optional<Usuario> usuario = RepositorioUsuarios.consultarUsuarioPorCuenta(textoIntroducido);
            if (usuario.isPresent()) {
                System.out.println("USUARIO ENCONTRADO");
                System.out.println(usuario.get().getNombreCompleto());
                return Optional.of(usuario.get());
            }
            System.out.println("No existe el numero de cuenta, intente nuevamente...");
        }
        return  null;
    }

    private BigDecimal manejarEntradaDeCifraMonetaria(InputProvider consoleInputProvider) {
        boolean cantidadVálida = false;
        while(!cantidadVálida){
            System.out.println("Introducir cantidad con enteros y centavos $$$.$$ | ESC para salir.");
            String cantidadIntroducida = consoleInputProvider.leerOpcionString();
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