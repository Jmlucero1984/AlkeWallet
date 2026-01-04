package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.OperacionDeConversionDeCuenta;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.RepositorioMonedas;

import java.math.BigDecimal;

public class EstadoConversionCuenta implements EstadoUsuario{
    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        System.out.println("Seleccione moneda a la que desea convertir su cuenta:");
        MonedaConvertible monedaConvertible = contexto.getUsuarioLogueado().getCuentaRegular().getMonedaConvertible();

        for (int i = 0; i < RepositorioMonedas.getMonedasDB().size(); i++) {
            if(RepositorioMonedas.getMonedasDB().get(i)!=monedaConvertible){
                System.out.println("" + (i + 1) + ". " + RepositorioMonedas.getMonedasDB().get(i).getNombre()
                        + " | " + RepositorioMonedas.getMonedasDB().get(i).getCodigo());
            }
        }
        System.out.println("" + (RepositorioMonedas.getMonedasDB().size()+1) + ". CANCELAR");
    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contextoUsuario) {
        int cantidadDeOpciones = RepositorioMonedas.getMonedasDB().size()+1;
        CuentaRegular cuantaDeUsuario = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
        MonedaConvertible monedaActual = cuantaDeUsuario.getMonedaConvertible();
        if (opcion <= 0 || opcion > cantidadDeOpciones || (opcion  ==RepositorioMonedas.getMonedasDB().indexOf(monedaActual)+1 )) {
            System.out.println("Opción inválida");
        } else if (opcion==cantidadDeOpciones){
            contextoUsuario.cambiarEstado(new EstadoOperaciones());
        } else {

            MonedaConvertible monedaDestino =RepositorioMonedas.getMonedasDB().get(opcion-1);
            OperacionDeConversionDeCuenta operacionDeConversionDeCuenta = new OperacionDeConversionDeCuenta(cuantaDeUsuario,monedaDestino);
            operacionDeConversionDeCuenta.ejecutar();
            operacionDeConversionDeCuenta.registrar(cuantaDeUsuario);
            System.out.println("CONVERSIÓN REALIZADA");
            contextoUsuario.cambiarEstado(new EstadoOperaciones());

        }

    }

    @Override
    public String getNombreEstado() {
        return "CONVERSION DE CUENTA";
    }
}
