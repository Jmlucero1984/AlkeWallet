package org.josemalucero.dominio.estado;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.operacion.OperacionDeposito;
import org.josemalucero.dominio.operacion.OperacionRetiro;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;

import java.math.BigDecimal;

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

    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {
        outputProvider.println(Messages.get("ingrese.cantidad.retirar")+" | "+Messages.get("escape.comando")+" para salir.");
        outputProvider.println(Messages.get("formato.esperado.enteros.centavos"));
    }

    @Override
    public void procesarOpcion(String opcion, ContextoUsuario contextoUsuario) {
        if(opcion.equalsIgnoreCase(Messages.get("escape.comando"))) {
            contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
        }

        CuentaRegular cuentaRegular = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        BigDecimal cifraVerificada = verificarCifraMonetaria(opcion);
        if(cifraVerificada!=null){
            OperacionRetiro operacionRetiro = new OperacionRetiro(cuentaRegular,cifraVerificada,outputProvider);
            if (operacionRetiro.preValidar()) {
                operacionRetiro.ejecutar();
                if(operacionRetiro.postValidar()){
                    operacionRetiro.registrar(cuentaRegular);
                    outputProvider.println(Messages.get("retiro.realizado"));
                    contextoUsuario.incrementar_retiros_por_session();
                    cuentaRegular.incrementar_cantidad_retiros_historicos();
                    contextoUsuario.confirmaContinuar();
                    contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(),
                            contextoUsuario.getOuputProvider()));
                }
            }
        }
    }

    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.retiro.cuenta");
    }
}
