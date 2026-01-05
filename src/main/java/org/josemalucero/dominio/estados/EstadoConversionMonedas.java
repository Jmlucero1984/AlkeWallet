package org.josemalucero.dominio.estados;

import org.josemalucero.servicio.InputProvider;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.OutputProvider;
import org.josemalucero.servicio.RepositorioMonedas;

import java.math.BigDecimal;
import java.util.Optional;

public class EstadoConversionMonedas extends EstadoUsuario {

    private MonedaConvertible monedaDePartida;
    private MonedaConvertible monedaDeDestino;

    public EstadoConversionMonedas(InputProvider inputProvider, OutputProvider outputProvider) {
        super(inputProvider, outputProvider);
    }

    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        if(monedaDePartida==null) {
            outputProvider.println("Moneda de PARTIDA-> para convertir");
            for ( int i = 0; i < RepositorioMonedas.getMonedasDB().size(); i++) {
                outputProvider.println("" + (i + 1) + ". " + RepositorioMonedas.getMonedasDB().get(i).getNombre()
                        + " | " + RepositorioMonedas.getMonedasDB().get(i).getCodigo());
            }

        } else {
            outputProvider.println("Moneda de ->DESTINO para convertir");
            for (int i = 0; i < RepositorioMonedas.getMonedasDB().size(); i++) {
                if(RepositorioMonedas.getMonedasDB().get(i)==monedaDePartida) continue;
                outputProvider.println("" + (i + 1) + ". " + RepositorioMonedas.getMonedasDB().get(i).getNombre()
                        + " | " + RepositorioMonedas.getMonedasDB().get(i).getCodigo());
            }

        }
        outputProvider.println("" + (RepositorioMonedas.getMonedasDB().size()+1) + ". CANCELAR");
        outputProvider.print("Seleccione una opción: ");

    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contextoUsuario) {

        if(opcion==RepositorioMonedas.getMonedasDB().size()+1)  contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
        int cantidadDeOpciones = RepositorioMonedas.getMonedasDB().size();
        if (monedaDePartida != null) cantidadDeOpciones--;
        if (opcion <= 0 || opcion > cantidadDeOpciones || (monedaDePartida != null && opcion ==RepositorioMonedas.getMonedasDB().indexOf(monedaDePartida)+1 )) {
            outputProvider.println("Opción inválida");
        } else if (opcion==cantidadDeOpciones){
            contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
        } else {
            MonedaConvertible eleccion = RepositorioMonedas.getMonedasDB().get(opcion - 1);
            outputProvider.println("Seleccionó " + eleccion.getNombre());

            if (monedaDePartida == null) {
                monedaDePartida = eleccion;
            } else {
                monedaDeDestino = eleccion;
                Optional<BigDecimal> cifraVerificada;
                cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getConsoleInputProvider()));
                if(!cifraVerificada.isEmpty()){
                  BigDecimal resultado =convertir(monedaDePartida,monedaDeDestino,cifraVerificada.get());
                  outputProvider.println(cifraVerificada.get()+" "+monedaDePartida.getCodigo()+" -> "+resultado+" "+monedaDeDestino.getCodigo());
                  contextoUsuario.cambiarEstado(new EstadoOperaciones(contextoUsuario.getConsoleInputProvider(), contextoUsuario.getOuputProvider()));
                }
            }
        }
    }

    private BigDecimal convertir(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto) {
        ConversorMoneda conversorMoneda = new ConversorMoneda();
        return conversorMoneda.convertirMoneda(origen,destino,monto);
    }

    @Override
    public String getNombreEstado() {
        return "CONVERSIÓN DE MONEDAS";
    }

    private BigDecimal manejarEntradaDeCifraMonetaria(InputProvider consoleInputProvider) {

        boolean cantidadVálida = false;

        while(!cantidadVálida){
            outputProvider.println("Introducir cantidad con enteros y centavos $$$.$$ | ESC para salir.");
            String cantidadIntroducida = consoleInputProvider.leerOpcionString();
            if(cantidadIntroducida.equalsIgnoreCase("ESC")) return null;
            cantidadVálida=cantidadIntroducida.matches("^\\d+\\.\\d{2}$");
            if (cantidadVálida) {
                return new BigDecimal(cantidadIntroducida);
            }
            outputProvider.println("Cantidad inválida, intente nuevamente...");
        }
        return  null;
    }
}
