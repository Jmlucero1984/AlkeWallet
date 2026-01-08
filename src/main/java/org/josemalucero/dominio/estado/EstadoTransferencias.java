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

/**
 * Permite la realización de transferencias de montos desde la cuenta del usuario logueado.
 * @author José Maria Lucero
 */

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


    /**
     * {@inheritDoc}
     * <p>
     * En este caso, los mensajes van cambiando a medida que avanza el proceso de definición del tipo de transferencia. En primera
     * instancia se debe encontrar una cuenta para el nímero ingresado, luego definir si la misma está en igual o distinta moneda que la cuenta
     * del usuario que pretende hacer la transferencia. Posteriormente definir, si es el caso, en que moneda desea ingresar el monto, para, en última
     * instancia, ingresar el monto.
     * </p>
     * @param contextoUsuario
     */
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


    /**
     * {@inheritDoc}
     * <p>
     * En este caso, las acciones van cambiando a medida que avanza el proceso de definición del tipo de transferencia. En primera
     * instancia se debe encontrar una cuenta para el nímero ingresado, luego definir si la misma está en igual o distinta moneda que la cuenta
     * del usuario que pretende hacer la transferencia. Posteriormente definir, si es el caso, en que moneda desea ingresar el monto, para, en última
     * instancia, ingresar el monto.
     * </p>
     * @param opcion
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(String opcion, ContextoUsuario contextoUsuario) {
        if(opcion.equalsIgnoreCase(Messages.get("escape.comando"))) {
            contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
            return;
        }
        if(tipoTransferencia==TipoTransferencia.UNDEFINED) {
            usuarioDestino = manejarEntradaDeNumeroCuenta(opcion);

            if(usuarioDestino!=null){

                TipoTransferencia tipoTransferenciaRecibida=definirMonedasDeOrigenYDestinoDeTransferencia(contextoUsuario,outputProvider,usuarioDestino);
                if(tipoTransferenciaRecibida==null) {
                    usuarioDestino=null;
                } else {
                    tipoTransferencia = tipoTransferenciaRecibida;
                }
            } else {
                outputProvider.printlnAlert(Messages.get("numero.cuenta.inexistente.intente.nuevamente"));
            }
        } else {
            if(tipoTransferencia==TipoTransferencia.DISTINTA_MONEDA){
                tipoTransferencia= definirEntreSubtiposDeTransferenciaDeDistintaMoneda(opcion);
            } else {
                BigDecimal cifraVerificada = verificarCifraMonetaria(opcion);
                if(cifraVerificada!=null){
                    datosTransferencia= new DatosTransferencia(contextoUsuario.getUsuarioLogueado().getCuentaRegular(),usuarioDestino.getCuentaRegular(),cifraVerificada, new ConversorMoneda());
                    OperacionTransferencia operacionTransferencia = obtenerOperacionTransferenciaEspecífica(tipoTransferencia,datosTransferencia,outputProvider);
                    ejecutarTransferencia(operacionTransferencia,contextoUsuario);
                }else{
                    outputProvider.printlnAlert(Messages.get("introduzca.cifra.valida"));
                }
            }
        }
    }

    /**Evalua si el usuario encontrado está tratando de transferirse a sí mismo. Si no es el caso, evalúa
     * si la cuenta de destino esta en igual o distinta moneda. Finalmente devuelve el tipo general de
     * transferencia para seguir avanzando en el proceso de definición de la operación.
     *
     * @param contextoUsuario
     * @param outputProvider
     * @param usuarioDestino
     * @return {@link TipoTransferencia} como primera aproximación de entre todas las variantes.
     */

    private TipoTransferencia definirMonedasDeOrigenYDestinoDeTransferencia(ContextoUsuario contextoUsuario,OutputProvider outputProvider,Usuario usuarioDestino){

        outputProvider.println(Messages.get("usuario.encontrado"));
        String nombreMonedaCuentaDestino = usuarioDestino.getCuentaRegular().getMonedaConvertible().getNombre();
        outputProvider.println(Messages.get("la.cuenta.en")+" "+nombreMonedaCuentaDestino+" "+Messages.get("pertenece.a")+" "+usuarioDestino.getNombreCompleto());
        if(usuarioDestino==contextoUsuario.getUsuarioLogueado()){
            outputProvider.printlnAlert(Messages.get("alerta.autotransferencia"));
            return null;
        }else if(usuarioDestino.getCuentaRegular().getMonedaConvertible().getCodigo()
                .equals(contextoUsuario.getUsuarioLogueado().getCuentaRegular().getMonedaConvertible().getCodigo())){
            return TipoTransferencia.IGUAL_MONEDA;
        } else {
            return TipoTransferencia.DISTINTA_MONEDA;

        }

    }

    /**
     * Recibe la opción ingresada por el usuario para definir el subtipo de transferencia entre cuentas de distinta moneda,
     * sea {@link TipoTransferencia#MONEDA_DESTINO} o {@link TipoTransferencia#MONEDA_ORIGEN}
     * @param opcion
     * @return {@link TipoTransferencia} del subtipo de transferencias entre cuentas de distinta moneda.
     */
    private TipoTransferencia definirEntreSubtiposDeTransferenciaDeDistintaMoneda(String opcion){
        try {
            int opcionInt = Integer.parseInt(opcion);
            switch (opcionInt) {
                case 1:
                    return TipoTransferencia.MONEDA_ORIGEN;

                case 2:
                    return TipoTransferencia.MONEDA_DESTINO;
                default:
                    outputProvider.printlnAlert(Messages.get("introduzca.numero.entero.dentro.rango"));
            }
        } catch (NumberFormatException e) {
            outputProvider.printlnAlert(Messages.get("introduzca.opcion.valida"));
        }
        return null;
    }

    /**
     * Realiza la prevalidación, la propia ejecución de la {@link OperacionTransferencia}, y la postvalidación. De ser efectiva esta
     * última, incrementa las transferencias consumidas en cuenta y en sesión, de lo contrario, restaura la cuenta a la situación anterior.
     * @param operacionTransferencia
     * @param contextoUsuario
     */
    private void ejecutarTransferencia(OperacionTransferencia operacionTransferencia,ContextoUsuario contextoUsuario){
        if (operacionTransferencia.preValidar() ){
            operacionTransferencia.ejecutar();
            if(operacionTransferencia.postValidar()) {
                operacionTransferencia.registrar(contextoUsuario.getUsuarioLogueado().getCuentaRegular());
                contextoUsuario.getOuputProvider().println(Messages.get("transferencia.realizada"));
                incrementarTransferenciasConsumidas(contextoUsuario);
            } else {
                operacionTransferencia.restaurarEstadoAnterior();
            }
            contextoUsuario.confirmaContinuar();
            contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
        } else {
            contextoUsuario.confirmaContinuar();
        }
    }

    /**
     * Incrementa en las transferencias consumidas en sesión y por cuenta.
     * @param contextoUsuario
     */
    private void incrementarTransferenciasConsumidas(ContextoUsuario contextoUsuario){
        contextoUsuario.incrementar_transferencias_por_sesion();
        contextoUsuario.getUsuarioLogueado().getCuentaRegular().incrementar_cantidad_transferencias_historicas();
    }

    /**
     * Crea un tipo particular de {@link OperacionTransferencia} con todos los datos requeridos para su posterior ejecución de
     * acuerdo al {@link TipoTransferencia} que recibe entre sus argumentos.
     * @param tipoTransferencia
     * @param datosTransferencia
     * @param outputProvider
     * @return {@link OperacionTransferencia} obtenido según {@link TipoTransferencia}
     */

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

    /**
     * Recibe un número de cuenta y devuelve el usuario que posee una cuenta con ese número, si lo encuentra.
     * @param numero
     * @return {@link Usuario} a cuya cuenta segun número pertenece.
     */
    private Usuario manejarEntradaDeNumeroCuenta(String numero){

            Optional<Usuario> usuario = RepositorioUsuarios.consultarUsuarioPorCuenta(numero);
            if (usuario.isPresent()) {
                return usuario.get();
            }

            return null;
    }

    /**
     * {@inheritDoc}
     * Para este estado en particular, dependiendo de cada subestado, devuelve un valor que se correponde con
     * cada paso en el proceso de definición del tipo de transferencia a ejectuar.
     * @return {@inheritDoc}
     */

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
