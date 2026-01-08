package org.josemalucero.dominio.estado;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.operacion.OperacionDeposito;
import org.josemalucero.dominio.operacion.OperacionRetiro;
import org.josemalucero.dominio.operacion.OperacionTransferencia;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

import java.math.BigDecimal;

/**
 * Permite la realización de retiros de montos en la cuenta del usuario logueado.
 *  @author José Maria Lucero
 */

public class EstadoRetiro extends EstadoUsuario{
    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     *
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoRetiro(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**
     * {@inheritDoc}
     * @param contextoUsuario
     */
    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {
        outputProvider.println(Messages.get("ingrese.cantidad.retirar")+" | "+Messages.get("escape.comando")+" para salir.");
        outputProvider.println(Messages.get("formato.esperado.enteros.centavos"));
    }

    /**
     * {@inheritDoc}
     * @param opcion
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(String opcion, ContextoUsuario contextoUsuario) {
        if(opcion.equalsIgnoreCase(Messages.get("escape.comando"))) {
            contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
        }

        CuentaRegular cuentaRegular = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        BigDecimal cifraVerificada = verificarCifraMonetaria(opcion);
        if(cifraVerificada!=null){
            OperacionRetiro operacionRetiro = new OperacionRetiro(cuentaRegular,cifraVerificada,outputProvider);
            ejecutarRetiro(operacionRetiro,contextoUsuario);
        }
    }
    /**
     * Realiza la prevalidación, la propia ejecución de la {@link OperacionRetiro}, y la postvalidación. De ser efectiva esta
     * última, incrementa los retiros consumidos en cuenta y en sesión.
     * @param operacionRetiro
     * @param contextoUsuario
     */
    private void ejecutarRetiro(OperacionRetiro operacionRetiro, ContextoUsuario contextoUsuario){
        if (operacionRetiro.preValidar()) {
            operacionRetiro.ejecutar();
            if(operacionRetiro.postValidar()){
                operacionRetiro.registrar(contextoUsuario.getUsuarioLogueado().getCuentaRegular());
                contextoUsuario.getOuputProvider().println(Messages.get("retiro.realizado"));
                incrementarRetirosConsumidos(contextoUsuario);
                contextoUsuario.confirmaContinuar();
                contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(),
                        contextoUsuario.getOuputProvider()));
            }
        }
    }
    /**
     * Incrementa en los retiros consumidos en sesión y por cuenta.
     * @param contextoUsuario
     */
    private void incrementarRetirosConsumidos(ContextoUsuario contextoUsuario){
        contextoUsuario.incrementar_retiros_por_session();
        contextoUsuario.getUsuarioLogueado().getCuentaRegular().incrementar_cantidad_retiros_historicos();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.retiro.cuenta");
    }
}
