package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.OperacionDeConversionDeCuenta;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.InputProvider;
import org.josemalucero.servicio.OutputProvider;
import org.josemalucero.servicio.RepositorioMonedas;

public class EstadoConversionCuenta extends EstadoUsuario{

    public EstadoConversionCuenta(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        outputProvider.println("Seleccione moneda a la que desea convertir su cuenta:");
        MonedaConvertible monedaConvertible = contexto.getUsuarioLogueado().getCuentaRegular().getMonedaConvertible();

        for (int i = 0; i < RepositorioMonedas.getMonedasDB().size(); i++) {
            if(RepositorioMonedas.getMonedasDB().get(i)!=monedaConvertible){
               outputProvider.println("" + (i + 1) + ". " + RepositorioMonedas.getMonedasDB().get(i).getNombre()
                        + " | " + RepositorioMonedas.getMonedasDB().get(i).getCodigo());
            }
        }
        outputProvider.println("" + (RepositorioMonedas.getMonedasDB().size()+1) + ". CANCELAR");
    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contextoUsuario) {
        int cantidadDeOpciones = RepositorioMonedas.getMonedasDB().size()+1;
        CuentaRegular cuantaDeUsuario = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        MonedaConvertible monedaActual = cuantaDeUsuario.getMonedaConvertible();
        if (opcion <= 0 || opcion > cantidadDeOpciones || (opcion  ==RepositorioMonedas.getMonedasDB().indexOf(monedaActual)+1 )) {
            outputProvider.println("Opción inválida");
        } else if (opcion==cantidadDeOpciones){
            contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
        } else {

            MonedaConvertible monedaDestino =RepositorioMonedas.getMonedasDB().get(opcion-1);
            OperacionDeConversionDeCuenta operacionDeConversionDeCuenta = new OperacionDeConversionDeCuenta(cuantaDeUsuario,monedaDestino,outputProvider);
            operacionDeConversionDeCuenta.ejecutar();
            operacionDeConversionDeCuenta.registrar(cuantaDeUsuario);
            outputProvider.println("CONVERSIÓN REALIZADA");
            contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));

        }

    }

    @Override
    public String getNombreEstado() {
        return "CONVERSIÓN DE CUENTA";
    }
}
