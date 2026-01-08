package org.josemalucero.dominio.estado;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.operacion.OperacionDeposito;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

import java.math.BigDecimal;

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

    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {
        outputProvider.println(Messages.get("ingrese.cantidad.depositar")+" | "+Messages.get("escape.comando")+" para salir.");
        outputProvider.println(Messages.get("formato.esperado.enteros.centavos"));

    }

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
            if (operacionDeposito.preValidar()) {
                operacionDeposito.ejecutar();
                if(operacionDeposito.postValidar()){
                    operacionDeposito.registrar(cuentaRegular);
                    outputProvider.printlnAlert(Messages.get("deposito.realizado"));
                    contextoUsuario.incrementar_depositos_por_sesion();
                    cuentaRegular.incrementar_cantidad_depositos_historicos();
                    contextoUsuario.confirmaContinuar();
                    contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(),
                            contextoUsuario.getOuputProvider()));
                }
            }
        }
    }

    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.deposito.cuenta");
    }
}
