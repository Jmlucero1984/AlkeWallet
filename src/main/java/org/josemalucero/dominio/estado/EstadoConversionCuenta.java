package org.josemalucero.dominio.estado;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.OperacionDeConversionDeCuenta;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;
import org.josemalucero.servicio.repositorios.RepositorioMonedas;
/**
 Se encarga de hacer la conversión de moneda de la cuenta hacia una moneda elegida por el usuario, corroborando que
 no sea la misma que la actual asociada a la cuenta.
 @author José Maria Lucero

 */
public class EstadoConversionCuenta extends EstadoUsuario{
    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoConversionCuenta(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**
     * Esta implementación particular se encarga de mostrar las monedas disponibles para convertir la cuenta,
     *  obviando mostrar aquella asociada a la cuenta actual, pero conservando la indexación relativa
     *  (puesto que se aplica un offset de 1) del repositorio de monedas.
     * @param contextoUsuario
     */
    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {
        outputProvider.println(Messages.get("seleccione.moneda.convertir.cuenta")+": ");
        MonedaConvertible monedaConvertible = contextoUsuario.getUsuarioLogueado().getCuentaRegular().getMonedaConvertible();

        for (int i = 0; i < RepositorioMonedas.getMonedasDB().size(); i++) {
            if(RepositorioMonedas.getMonedasDB().get(i)!=monedaConvertible){
               outputProvider.println("" + (i + 1) + ". " + RepositorioMonedas.getMonedasDB().get(i).getNombre()
                        + " | " + RepositorioMonedas.getMonedasDB().get(i).getCodigo());
            }
        }
        outputProvider.println("" + (RepositorioMonedas.getMonedasDB().size()+1) + ". "+ Messages.get("cancelar"));
        outputProvider.print(Messages.get("seleccione.opcion")+": ");
    }

    /**
     *  Verifica que la opción elegida, determinada por el valor numérico asociado, esté dentro del rango permitido,
     *  que no corresponda exactamente con el indice de la moneda actual de
     *  la cuenta, o que en última instancia, la elección corresponda a salir del estado actual.
     * @param opcionStr
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(String opcionStr, ContextoUsuario contextoUsuario) {

        try{
            int opcion=Integer.parseInt(opcionStr);
            int cantidadDeOpciones = RepositorioMonedas.getMonedasDB().size()+1;
            CuentaRegular cuantaDeUsuario = contextoUsuario.getUsuarioLogueado().getCuentaRegular();
            MonedaConvertible monedaActual = cuantaDeUsuario.getMonedaConvertible();
            if (opcion <= 0 || opcion > cantidadDeOpciones || (opcion  ==RepositorioMonedas.getMonedasDB().indexOf(monedaActual)+1 )) {
                outputProvider.printlnAlert(Messages.get("alerta.opcion.invalida"));
            } else if (opcion==cantidadDeOpciones){
                contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
            } else {

                MonedaConvertible monedaDestino =RepositorioMonedas.getMonedasDB().get(opcion-1);
                outputProvider.println(Messages.get("selecciono")+" " + monedaDestino.getNombre());
                OperacionDeConversionDeCuenta operacionDeConversionDeCuenta = new OperacionDeConversionDeCuenta(cuantaDeUsuario,monedaDestino,outputProvider);
                operacionDeConversionDeCuenta.ejecutar();
                operacionDeConversionDeCuenta.registrar(cuantaDeUsuario);
                outputProvider.printlnAlert(Messages.get("conversion.realizada"));
                contextoUsuario.confirmaContinuar();
                contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));

            }

        } catch (NumberFormatException e){
            outputProvider.printlnAlert(Messages.get("alerta.introduzca.opcion.valida"));
        }

    }

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */

    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.conversion.cuenta");
    }
}
