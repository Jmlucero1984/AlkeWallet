package org.josemalucero.dominio.estado;

import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.operacion.*;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;
import org.josemalucero.servicio.repositorios.RepositorioUsuarios;

import java.math.BigDecimal;
import java.util.Optional;

public class EstadoTransferencias extends EstadoUsuario {

    TipoTransferencia tipoTransferencia = TipoTransferencia.UNDEFINED;
    Usuario usuarioDestino = null;
    DatosTransferencia datosTransferencia=null;
    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     *
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoTransferencias(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {
        if(tipoTransferencia==TipoTransferencia.UNDEFINED) {
            outputProvider.println(Messages.get("introduzca.numero.cuenta.destino")+" | "+Messages.get("escape.comando")+" para salir");
        } else {
            if(tipoTransferencia==TipoTransferencia.DISTINTA_MONEDA){
                outputProvider.println(Messages.get("seleccione.tipo.transferencia")+" | "+Messages.get("escape.comando")+" para salir");
                outputProvider.println("1. "+Messages.get("monto.moneda.cuenta.propia"));
                outputProvider.println("2. "+Messages.get("monto.moneda.cuenta.destino"));
            } else {
                outputProvider.println(Messages.get("introduzca.el.monto.a.transferir")+" | "+Messages.get("escape.comando")+" para salir");
                outputProvider.println(Messages.get("formato.esperado.enteros.centavos"));
            }
        }
    }

    @Override
    public void procesarOpcion(String opcion, ContextoUsuario contextoUsuario) {
        if(opcion.equalsIgnoreCase(Messages.get("escape.comando"))) {
            contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
            return;
        }
        if(tipoTransferencia==TipoTransferencia.UNDEFINED) {
            usuarioDestino = manejarEntradaDeNumeroCuenta(opcion);
            if(usuarioDestino!=null){
                outputProvider.println(Messages.get("usuario.encontrado"));
                if(usuarioDestino==contextoUsuario.getUsuarioLogueado()){
                    outputProvider.printlnAlert(Messages.get("alerta.autotransferencia"));
                    usuarioDestino = null;
                }else if(usuarioDestino.getCuentaRegular().getMonedaConvertible().getCodigo()
                        .equals(contextoUsuario.getUsuarioLogueado().getCuentaRegular().getMonedaConvertible().getCodigo())){
                    tipoTransferencia=TipoTransferencia.IGUAL_MONEDA;
                } else {
                    tipoTransferencia=TipoTransferencia.DISTINTA_MONEDA;

                }
            } else {
                outputProvider.printlnAlert(Messages.get("numoero.cuenta.inexistente.intente.nuevamente"));
            }
        } else {
            if(tipoTransferencia==TipoTransferencia.DISTINTA_MONEDA){
                try {
                    int opcionInt = Integer.parseInt(opcion);
                    switch (opcionInt) {
                        case 1:
                            tipoTransferencia = TipoTransferencia.MONEDA_ORIGEN;
                            break;
                        case 2:
                            tipoTransferencia = TipoTransferencia.MONEDA_DESTINO;
                            break;

                        default:
                            outputProvider.printlnAlert(Messages.get("introduzca.numero.entero.dentro.rango"));
                    }
                } catch (NumberFormatException e) {
                    outputProvider.printlnAlert(Messages.get("introduzca.opcion.valida"));
                }

            } else {
                BigDecimal cifraVerificada = verificarCifraMonetaria(opcion);
                if(cifraVerificada!=null){
                    datosTransferencia= new DatosTransferencia(contextoUsuario.getUsuarioLogueado().getCuentaRegular(),usuarioDestino.getCuentaRegular(),cifraVerificada, new ConversorMoneda());
                    OperacionTransferencia operacionTransferencia = obtenerOperacionTransferenciaEspecífica(tipoTransferencia,datosTransferencia,outputProvider);
                    if (operacionTransferencia.preValidar() ){
                        operacionTransferencia.ejecutar();
                        if(operacionTransferencia.postValidar()) {
                            operacionTransferencia.registrar(contextoUsuario.getUsuarioLogueado().getCuentaRegular());
                            outputProvider.println(Messages.get("transferencia.realizada"));
                            contextoUsuario.incrementar_transferencias_por_sesion();
                            contextoUsuario.getUsuarioLogueado().crearCuentRegular().incrementar_cantidad_transferencias_historicas();

                        } else {
                            operacionTransferencia.restaurarEstadoAnterior();
                        }
                        contextoUsuario.confirmaContinuar();
                        contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                    } else {
                        contextoUsuario.confirmaContinuar();
                    }
                }else{
                    outputProvider.printlnAlert(Messages.get("introduzca.cifra.valida"));
                }
            }
        }
    }



    private OperacionTransferencia obtenerOperacionTransferenciaEspecífica(TipoTransferencia tipoTransferencia, DatosTransferencia datosTransferencia, OutputProvider outputProvider){
        switch (tipoTransferencia) {
            case TipoTransferencia.IGUAL_MONEDA -> {
                return new OperacionTransferencia(datosTransferencia.getCuentaOrigen(), datosTransferencia.getCuentaDestino(), datosTransferencia.getMonto(),outputProvider);
            }
            case TipoTransferencia.MONEDA_ORIGEN -> {
                return new OperacionTransferenciaMonedaOrigen(datosTransferencia.getCuentaOrigen(), datosTransferencia.getCuentaDestino(), datosTransferencia.getMonto(),new ConversorMoneda(),outputProvider);
            }
            case TipoTransferencia.MONEDA_DESTINO -> {
                return new OperacionTransferenciaMonedaDestino(datosTransferencia.getCuentaOrigen(), datosTransferencia.getCuentaDestino(), datosTransferencia.getMonto(),new ConversorMoneda(),outputProvider);
            }
        };
        return null;
    }

    private Usuario manejarEntradaDeNumeroCuenta(String numero){

            Optional<Usuario> usuario = RepositorioUsuarios.consultarUsuarioPorCuenta(numero);
            if (usuario.isPresent()) {

                outputProvider.println(usuario.get().getNombreCompleto());
                return usuario.get();
            }

            return null;
    }

    @Override
    public String getNombreEstado() {
        switch (tipoTransferencia){
            case TipoTransferencia.UNDEFINED:
                return Messages.get("tipo.transferencia.entre.cuentas");
            case TipoTransferencia.DISTINTA_MONEDA:
                return Messages.get("tipo.transferencia.a.cuenta.distinta.moneda");
            case TipoTransferencia.MONEDA_DESTINO:
                return Messages.get("tipo.transferencia.a.cdm.moneda.destino");
            case TipoTransferencia.MONEDA_ORIGEN:
                return Messages.get("tipo.transferencia.a.cdm.moneda.origen");
            case TipoTransferencia.IGUAL_MONEDA:
                return Messages.get("tipo.transferencia.a.cuenta.igual.moneda");
            default:
                return Messages.get("transferencia");
        }

    }
}
