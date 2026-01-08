package org.josemalucero.dominio.estado;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.operacion.OperacionDeposito;
import org.josemalucero.dominio.operacion.OperacionRetiro;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

import java.math.BigDecimal;

/**
 * Permite la realización de depósitos de montos en la cuenta del usuario logueado.
 *  @author José Maria Lucero
 */

public class EstadoDeposito extends EstadoUsuario{
    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     *
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoDeposito(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**
     * {@inheritDoc}
     * @param contextoUsuario
     */
    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {
        outputProvider.println(Messages.get("ingrese.cantidad.depositar")+" | "+Messages.get("escape.comando")+" para salir.");
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
            contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(),
                    contextoUsuario.getOuputProvider()));
        }

        CuentaRegular cuentaRegular = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        BigDecimal cifraVerificada = verificarCifraMonetaria(opcion);
        if(cifraVerificada!=null){
            OperacionDeposito operacionDeposito = new OperacionDeposito(cuentaRegular,cifraVerificada,outputProvider);
            ejecutarDeposito(operacionDeposito,contextoUsuario);
        }
    }

    /**
     * Realiza la prevalidación, la propia ejecución de la {@link OperacionDeposito}, y la postvalidación. De ser efectiva esta
     * última, incrementa los depósitos consumidos en cuenta y en sesión.
     * @param operacionDeposito
     * @param contextoUsuario
     */
    private void ejecutarDeposito(OperacionDeposito operacionDeposito,ContextoUsuario contextoUsuario){
        if (operacionDeposito.preValidar()) {
            operacionDeposito.ejecutar();
            if(operacionDeposito.postValidar()){
                operacionDeposito.registrar(contextoUsuario.getUsuarioLogueado().getCuentaRegular());
                contextoUsuario.getOuputProvider().printlnAlert(Messages.get("deposito.realizado"));
                incrementarDepositosConsumidos(contextoUsuario);
                contextoUsuario.confirmaContinuar();
                contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(),
                        contextoUsuario.getOuputProvider()));
            }
        }
    }


    /**
     * Incrementa en los depósitos consumidos en sesión y por cuenta.
     * @param contextoUsuario
     */
    private void incrementarDepositosConsumidos(ContextoUsuario contextoUsuario){
        contextoUsuario.incrementar_depositos_por_sesion();
        contextoUsuario.getUsuarioLogueado().getCuentaRegular().incrementar_cantidad_depositos_historicos();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.deposito.cuenta");
    }
}
