package org.josemalucero.dominio.estados;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.RepositorioMonedas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Scanner;

public class EstadoConversionMonedas implements EstadoUsuario {

    private MonedaConvertible monedaDePartida;
    private MonedaConvertible monedaDeDestino;
    @Override
    public void mostrarMenu(ContextoUsuario contexto) {
        if(monedaDePartida==null) {
            System.out.println("Moneda de PARTIDA-> para convertir");

            for ( int i = 0; i < RepositorioMonedas.getMonedasDB().size(); i++) {
                System.out.println("" + (i + 1) + ". " + RepositorioMonedas.getMonedasDB().get(i).getNombre()
                        + " | " + RepositorioMonedas.getMonedasDB().get(i).getCodigo());
            }

        } else {
            System.out.println("Moneda de ->DESTINO para convertir");
            for (int i = 0; i < RepositorioMonedas.getMonedasDB().size(); i++) {
                if(RepositorioMonedas.getMonedasDB().get(i)==monedaDePartida) continue;
                System.out.println("" + (i + 1) + ". " + RepositorioMonedas.getMonedasDB().get(i).getNombre()
                        + " | " + RepositorioMonedas.getMonedasDB().get(i).getCodigo());
            }

        }
        System.out.println("" + (RepositorioMonedas.getMonedasDB().size()+1) + ". CANCELAR");
        System.out.print("Seleccione una opción: ");

    }

    @Override
    public void procesarOpcion(int opcion, ContextoUsuario contextoUsuario) {
        if(opcion==RepositorioMonedas.getMonedasDB().size()+1)  contextoUsuario.cambiarEstado(new EstadoOperaciones());
        int cantidadDeOpciones = RepositorioMonedas.getMonedasDB().size();
        if (monedaDePartida != null) cantidadDeOpciones--;
        if (opcion <= 0 || opcion > cantidadDeOpciones) {
            System.out.println("Opción inválida");
        } else {
            MonedaConvertible eleccion = RepositorioMonedas.getMonedasDB().get(opcion - 1);
            System.out.println("Seleccionó " + eleccion.getNombre());
            if (monedaDePartida == null) {
                monedaDePartida = eleccion;

            } else {
                monedaDeDestino = eleccion;
                Optional<BigDecimal> cifraVerificada;

                cifraVerificada = Optional.ofNullable(manejarEntradaDeCifraMonetaria(contextoUsuario.getScanner()));
                if(!cifraVerificada.isEmpty()){
                  BigDecimal resultado =convertir(monedaDePartida,monedaDeDestino,cifraVerificada.get());
                  System.out.println(cifraVerificada.get()+" "+monedaDePartida.getCodigo()+" -> "+resultado+" "+monedaDeDestino.getCodigo());
                  contextoUsuario.cambiarEstado(new EstadoOperaciones());
                }
            }
        }
    }

    private BigDecimal convertir(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto) {
        return  origen.getRatioDolar().divide(destino.getRatioDolar(),10, RoundingMode.HALF_UP).multiply(monto).setScale(2,RoundingMode.HALF_UP);
    }

    @Override
    public String getNombreEstado() {
        return "CONVERSIÓN DE MONEDAS";
    }

        private BigDecimal manejarEntradaDeCifraMonetaria(Scanner scanner) {

            boolean cantidadVálida = false;

            while(!cantidadVálida){
                System.out.println("Introducir cantidad con enteros y centavos $$$.$$ | ESC para salir.");
                String cantidadIntroducida = scanner.nextLine();
                if(cantidadIntroducida.equalsIgnoreCase("ESC")) return null;
                cantidadVálida=cantidadIntroducida.matches("^\\d+\\.\\d{2}$");
                if (cantidadVálida) {
                    return new BigDecimal(cantidadIntroducida);
                }
                System.out.println("Cantidad inválida, intente nuevamente...");
            }
            return  null;
        }
}
