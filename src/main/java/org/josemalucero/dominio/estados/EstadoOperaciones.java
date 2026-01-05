package org.josemalucero.dominio.estados;

import org.josemalucero.servicio.InputProvider;
import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.cuenta.Transferible;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.operacion.*;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.FormateadorDeRegistroAImprimir;
import org.josemalucero.servicio.OutputProvider;
import org.josemalucero.servicio.RepositorioUsuarios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class EstadoOperaciones extends EstadoUsuario {

    public EstadoOperaciones(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        outputProvider.println("Bienvenido, " + contexto.getUsuarioLogueado().getNombreCompleto());
        outputProvider.println("1. Consultar datos cuenta");
        outputProvider.println("2. Consultar saldo");
        outputProvider.println("3. Depositar dinero");
        outputProvider.println("4. Retirar dinero");
        outputProvider.println("5. Transferir dinero");
        outputProvider.println("6. Consultar conversión entre monedas");
        outputProvider.println("7. Convertir cuenta a otra moneda");
        outputProvider.println("8. Ver historial de transacciones");
        outputProvider.println("9. Cerrar sesión (Sign Out)");
        outputProvider.print("Seleccione una opción: ");
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
                outputProvider.println("Cerrando sesión...");
                contexto.cerrarSesion();
                break;

            default:
                outputProvider.println("Opción inválida");
        }
    }

    @Override
    public String getNombreEstado() {
        return "OPERACIONES";
    }



    private void consultarSaldo(ContextoUsuario contextoUsuario) {
        OperacionConsulta operacionConsulta = new OperacionConsulta(contextoUsuario.getUsuarioLogueado().getCuentaRegular(),outputProvider);
        operacionConsulta.ejecutar();
    }

    private void depositarDinero(ContextoUsuario contextoUsuario) {
        outputProvider.println("DEPOSITAR EN CUENTA");
        Optional<BigDecimal> cifraVerificada;
        CuentaRegular cuentaRegular = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        boolean operacionExitosa=false;
        while(!operacionExitosa) {
            cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
            if(cifraVerificada.isEmpty()){
                return;
            } else {
                OperacionDeposito operacionDeposito = new OperacionDeposito(cuentaRegular,cifraVerificada.get(),outputProvider);
                if (operacionDeposito.preValidar()) {
                    operacionDeposito.ejecutar();
                    if(operacionDeposito.posValidar()){
                        operacionExitosa = true;
                        operacionDeposito.registrar(cuentaRegular);
                        outputProvider.println("DEPOSITO REALIZADO");
                    }

                }
            }
        }
    }

    private void consultarDatosCuenta(ContextoUsuario contextoUsuario){
        outputProvider.println(contextoUsuario.getUsuarioLogueado().getNombreCompleto());
        outputProvider.println("Cuenta en "+contextoUsuario.getUsuarioLogueado().getCuentaRegular().getMonedaConvertible().getNombre());
        outputProvider.println("N° Cuenta: "+contextoUsuario.getUsuarioLogueado().getCuentaRegular().getSerialCuenta());
    }

    private void convertirCuentaAOtraMoneda(ContextoUsuario contextoUsuario){
        contextoUsuario.cambiarEstado(new EstadoConversionCuenta(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
    }

    private void consultarConversionMoneda(ContextoUsuario contextoUsuario){
        contextoUsuario.cambiarEstado(new EstadoConversionMonedas(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
    }

    private void retirarDinero(ContextoUsuario contextoUsuario) {
        outputProvider.println("RETIRAR DE CUENTA");
        Optional<BigDecimal> cifraVerificada;
        CuentaRegular cuentaRegular = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        boolean operacionExitosa=false;
        while(!operacionExitosa) {
            cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
            if(cifraVerificada.isEmpty()){
                return;
            } else  {
                OperacionRetiro operacionRetiro = new OperacionRetiro(cuentaRegular,cifraVerificada.get(),outputProvider);
                if (operacionRetiro.preValidar()){
                    operacionRetiro.ejecutar();
                    if(operacionRetiro.posValidar()) {
                        operacionExitosa = true;
                        operacionRetiro.registrar(cuentaRegular);
                        outputProvider.println("RETIRO REALIZADO");
                    }
                }
            }
        }
    }

    private void transferirDinero(ContextoUsuario contextoUsuario) {
        outputProvider.println("TRANSFERIR A CUENTA");
        Optional<Usuario> usuarioDestino = manejarEntradaDeNumeroCuenta(contextoUsuario.getConsoleInputProvider());
        if(usuarioDestino!=null && !usuarioDestino.isEmpty()){
            operarSobreCuentaParaTransferir(contextoUsuario,usuarioDestino.get());
        }
    }

    private void operarSobreCuentaParaTransferir(ContextoUsuario contextoUsuario,Usuario usuarioDestino){
        outputProvider.println("La cuenta destino pertenece a: " + usuarioDestino.getNombreCompleto());
        CuentaRegular cuentaRegular =contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        outputProvider.println("");
        if(usuarioDestino.equals(contextoUsuario.getUsuarioLogueado())){
            outputProvider.println("|||| No le parece sin sentido transferirse a usted mismo? ||||");
            return;
        }

        if (!(usuarioDestino.getCuentaRegular() instanceof Transferible)) {
            outputProvider.println("La cuenta destino no puede recibir transferencias");
            return;
        }
        int opcion = 0;

        if(!usuarioDestino.getCuentaRegular().getMonedaConvertible().getCodigo().equals(cuentaRegular.getMonedaConvertible().getCodigo())){
            outputProvider.println("La cuenta destino está en una moneda diferente ("+cuentaRegular.getMonedaConvertible().getCodigo()+")");
            String[] opciones = new String[]{"Monto en moneda de su propia cuenta","Monto en moneda de la cuenta destino"};
            String titulo = "Seleccione opción para transferencia entre cuentas";
            int eleccion = seleccionMultipleGenerica(contextoUsuario.getConsoleInputProvider(),titulo, opciones);
            if(eleccion==-1) return;
            opcion = eleccion;
        }

        Optional<BigDecimal> cifraVerificada=null;
        cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
        if(cifraVerificada==null) {return;}

        OperacionTransferencia operacionTransferencia = new OperacionTransferencia(cuentaRegular, usuarioDestino.getCuentaRegular(), cifraVerificada.get(),outputProvider);
        switch (opcion) {
            case 1:
                operacionTransferencia = new OperacionTransferenciaMonedaOrigen(cuentaRegular, usuarioDestino.getCuentaRegular(), cifraVerificada.get(),new ConversorMoneda(),outputProvider);
                break;
            case 2:
                operacionTransferencia = new OperacionTransferenciaMonedaDestino(cuentaRegular, usuarioDestino.getCuentaRegular(), cifraVerificada.get(),new ConversorMoneda(),outputProvider);
                break;
        }

        if (operacionTransferencia.preValidar() ){
            operacionTransferencia.ejecutar();
            if(operacionTransferencia.posValidar()) {
                operacionTransferencia.registrar(cuentaRegular);
                outputProvider.println("TRANSFERENCIA REALIZADA");
            } else {
                operacionTransferencia.restaurarEstadoAnterior();
            }
        }
    }




    private void verHistorial(ContextoUsuario contextoUsuario) {
        outputProvider.println("Mostrando historial...");
        ArrayList<RegistroOperacion> operacionesHistoricas = contextoUsuario.getUsuarioLogueado().getCuentaRegular().getHistorialOperaciones();
        outputProvider.println(FormateadorDeRegistroAImprimir.generarCabeceras(FormateadorDeRegistroAImprimir.Alineado.CENTRO));
        operacionesHistoricas.forEach(t->outputProvider.println(FormateadorDeRegistroAImprimir.formatearRegistro(t, FormateadorDeRegistroAImprimir.Alineado.CENTRO)));

    }


    private int seleccionMultipleGenerica(InputProvider consoleInputProvider,String titulo,String[] opciones){
        while(true) {
            outputProvider.println(titulo+" | ESC para salir.");
            for(int i=0;i<opciones.length;i++){
                outputProvider.println((i+1)+". "+opciones[i]);
            }

            String textoIntroducido = consoleInputProvider.leerOpcionString();
            if (textoIntroducido.equalsIgnoreCase("ESC")) return -1;
            int opcion;
            try {
                opcion =  Integer.parseInt(textoIntroducido);
                if(opcion>0 && opcion<opciones.length+1){
                    return opcion;
                } else {
                    outputProvider.println("Opción invalida");
                }
            } catch (NumberFormatException e) {
                outputProvider.println("Opción invalida");
            }
        }
    }

    private Optional<Usuario> manejarEntradaDeNumeroCuenta(InputProvider consoleInputProvider){
        boolean cuentaValida = false;
        while(!cuentaValida){
            outputProvider.println("Introducir numero de cuenta destino | ESC para salir.");
            String textoIntroducido = consoleInputProvider.leerOpcionString();
            if(textoIntroducido.equalsIgnoreCase("ESC")) return null;
            Optional<Usuario> usuario = RepositorioUsuarios.consultarUsuarioPorCuenta(textoIntroducido);
            if (usuario.isPresent()) {
                outputProvider.println("USUARIO ENCONTRADO");
                outputProvider.println(usuario.get().getNombreCompleto());
                return Optional.of(usuario.get());
            }
            outputProvider.println("No existe el numero de cuenta, intente nuevamente...");
        }
        return  null;
    }

    private BigDecimal manejarEntradaDeCifraMonetaria(InputProvider consoleInputProvider) {
        boolean cantidadVálida = false;
        while(!cantidadVálida){
            outputProvider.println("Introducir cantidad con enteros y centavos $$$.$$ | ESC para salir.");
            String cantidadIntroducida = consoleInputProvider.leerOpcionString();
            if(cantidadIntroducida.equalsIgnoreCase("ESC")) return null;
            cantidadVálida=cantidadIntroducida.matches("^\\d+\\.\\d{2}$");
            if (cantidadVálida) {
                return new BigDecimal(cantidadIntroducida);
            }
            outputProvider.println("Cantidad inválida, intente nuevamente...");
        }
        return  null;
    }


}