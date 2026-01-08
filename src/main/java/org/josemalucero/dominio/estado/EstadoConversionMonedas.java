package org.josemalucero.dominio.estado;

import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.providers.OutputProvider;
import org.josemalucero.servicio.repositorios.RepositorioMonedas;

import java.math.BigDecimal;
import java.util.Optional;


/**
 * Permite hacer conversiones entre diferentes monedas, seleccionando la moneda de origen, la de destino, y el monto
 * de la oepración de conversión. No tiene efecto alguno sobre la moneda asociada a la cuenta del usuario logueado ni sobre
 * el montdo de su saldo.
 *  @author José Maria Lucero

 */
public class EstadoConversionMonedas extends EstadoUsuario {

    private MonedaConvertible monedaDePartida;
    private MonedaConvertible monedaDeDestino;

    /**
     * Constructor de la clase que recibe los objetos para manejar la entrada y salida de datos en la interacción con el usuario.
     * @param inputProvider
     * @param outputProvider
     */
    public EstadoConversionMonedas(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    /**
     * Permite una doble llamada para posibilitar la elección de las monedas de origen y destino de la consulta
     * de conversión monetaria, a la vez que evita mostrar en la segunda instancia aquella elegida en la primera
     * @param contextoUsuario
     */
    @Override
    public void mostrarInformaciónContextual(ContextoUsuario contextoUsuario) {
        if(monedaDePartida==null) {
            outputProvider.println(Messages.get("moneda.partida.para.convertir"));
            for ( int i = 0; i < RepositorioMonedas.getMonedasDB().size(); i++) {
                outputProvider.println("" + (i + 1) + ". " + RepositorioMonedas.getMonedasDB().get(i).getNombre()
                        + " | " + RepositorioMonedas.getMonedasDB().get(i).getCodigo());
            }

        } else {
            outputProvider.println(Messages.get("moneda.destino.para.convertir"));
            for (int i = 0; i < RepositorioMonedas.getMonedasDB().size(); i++) {
                if(RepositorioMonedas.getMonedasDB().get(i)==monedaDePartida) continue;
                outputProvider.println("" + (i + 1) + ". " + RepositorioMonedas.getMonedasDB().get(i).getNombre()
                        + " | " + RepositorioMonedas.getMonedasDB().get(i).getCodigo());
            }

        }
        outputProvider.println("" + (RepositorioMonedas.getMonedasDB().size()+1) + ". "+Messages.get("cancelar"));
        outputProvider.print(Messages.get("seleccione.opcion"+" "));

    }

    /**
     * Define ambas monedas,tanto la de origen como la de destino de la conversión monetaria, verificando si la
     * primera ya ha sido definida. Finalmente, luego de verificar que la cifra introducida
     *  sea válida, realiza la conversión especificada.
     * @param opcionStr
     * @param contextoUsuario
     */
    @Override
    public void procesarOpcion(String opcionStr, ContextoUsuario contextoUsuario) {

        try{
            int opcion=Integer.parseInt(opcionStr);
            if(opcion==RepositorioMonedas.getMonedasDB().size()+1)  contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
            int cantidadDeOpciones = RepositorioMonedas.getMonedasDB().size();
            if (monedaDePartida != null) cantidadDeOpciones--;
            if (opcion <= 0 || opcion > cantidadDeOpciones || (monedaDePartida != null && opcion ==RepositorioMonedas.getMonedasDB().indexOf(monedaDePartida)+1 )) {
                outputProvider.printlnAlert(Messages.get("opcion.invalida"));
            } else if (opcion==cantidadDeOpciones){
                contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
            } else {
                MonedaConvertible eleccion = RepositorioMonedas.getMonedasDB().get(opcion - 1);
                outputProvider.println(Messages.get("selecciono")+" " + eleccion.getNombre());

                if (monedaDePartida == null) {
                    monedaDePartida = eleccion;
                } else {
                    monedaDeDestino = eleccion;
                    Optional<BigDecimal> cifraVerificada;
                    cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
                    if(!cifraVerificada.isEmpty()){
                      BigDecimal resultado =convertir(monedaDePartida,monedaDeDestino,cifraVerificada.get());
                      outputProvider.println("------------------------------------------------------------");
                      outputProvider.println("     "+cifraVerificada.get()+" "+monedaDePartida.getCodigo()+" -> "+resultado+" "+monedaDeDestino.getCodigo());
                      outputProvider.println("------------------------------------------------------------");
                      contextoUsuario.confirmaContinuar();
                      contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                    }
                }
            }
        } catch (NumberFormatException e){
            outputProvider.println(Messages.get("introduzca.opcion.valida"));
        }
    }

    /**
     * Realiza la conversión del monto especificado desde la moneda de origen a la de destino.
     * @param origen
     * @param destino
     * @param monto
     * @return {@code BigDecimal} de monto obtenido de la conversión a la moneda de destino.
     */
    private BigDecimal convertir(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto) {
        ConversorMoneda conversorMoneda = new ConversorMoneda();
        return conversorMoneda.convertirMoneda(origen,destino,monto);
    }

    /**
     * Verifica la validez de la cifra introducida mediante una operación {@code REGEX} que especifica un patrón
     * de números enteros, un punto separador, décimas y centésimas.
     *  Ejemplos de valores aceptados:
     *    <blockquote><pre>
     *   15098.15   100.00  0.00    ESC     esc
     *    </pre></blockquote>
     * @param consoleInputProvider
     * @return {@code BigDecimal} de monto ingresado por el usuario. {@code null} en caso de que el usuario decida
     * cancelar la operación.
     */
    private BigDecimal manejarEntradaDeCifraMonetaria(InputProvider consoleInputProvider) {

        boolean cantidadVálida = false;

        while(!cantidadVálida){
            outputProvider.println(Messages.get("introduzca.cant.enteros.centavos")+" | "+Messages.get("escape.comando")+" para salir.");
            String cantidadIntroducida = consoleInputProvider.leerOpcionString();
            if(cantidadIntroducida.equalsIgnoreCase(Messages.get("escape.comando"))) return null;
            cantidadVálida=cantidadIntroducida.matches("^\\d+\\.\\d{2}$");
            if (cantidadVálida) {
                return new BigDecimal(cantidadIntroducida);
            }
            outputProvider.printlnAlert(Messages.get("cantidad.invalida"));
        }
        return  null;
    }

    /**
     * Permite obtener el nombre del estado actual.
     * @return {@code String} del nombre del estado.
     */
    @Override
    public String getNombreEstado() {
        return Messages.get("nombre.estado.conversion.monedas");
    }

}

