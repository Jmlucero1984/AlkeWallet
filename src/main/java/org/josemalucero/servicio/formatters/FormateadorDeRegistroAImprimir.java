package org.josemalucero.servicio.formatters;

import org.josemalucero.dominio.operacion.RegistroOperacion;
import org.josemalucero.servicio.providers.Messages;


/**
 * Clase que permite la tabulación de cadenas de texto dentro de una cierta cantidad de espacios disponibles.
 * @author José María Lucero
 */
public class FormateadorDeRegistroAImprimir {
    /**
     * Estas constantes determinan los espacios disponibles para colocar el texto, lo que eventualmente puede
     * determinar si la cadena de texto debe ser truncada.
     */
    private static final int MAX_DESCRIPTION_CHARS = 70;
    private static final int MAX_BALANCE_CHARS = 30;
    private static final int MAX_MONTO_CHARS = 30;
    private static final int MAX_DATE_CHARS = 40;
    public enum Alineado {IZQUIERDA,CENTRO, DERECHA};

    /**
     * Se encarga de realizar el alineado horizontal del texto dentro de una cierta cantidad de espacios de acuerdo al tipo
     * de elemento de la descripcion de un registro, para finalmente concatenarlo todo en una sola línea, con unos determinados
     * separadores de columnas.
     * <p>
     * Alineado.CENTRO
     *        <blockquote><pre>
     │   04.JANUARY.2026 19:17:58    │    DEPÓSITO EN CUENTA    │    10000.00    │  10000.00
     *        </pre></blockquote>
     * Alineado.DERECHA
     *        <blockquote><pre>
     │      04.JANUARY.2026 19:17:58 │       DEPÓSITO EN CUENTA │       10000.00 │     10000.00
     *        </pre></blockquote>
     * Alineado.IZQUIERDA
     *        <blockquote><pre>
     │ 04.JANUARY.2026 19:17:58      │ DEPÓSITO EN CUENTA       │ 10000.00       │ 10000.00
     *        </pre></blockquote>
     *        </p>
     * @param registroOperacion
     * @param alineado
     * @return {@link String} que representa toda una línea de registros tabulados y separados por un caracter específico.
     *
     */
    public static String formatearRegistro(RegistroOperacion registroOperacion, Alineado alineado) {
        String formattedOutput = alinearTexto(MAX_DATE_CHARS,registroOperacion.getFormattedDateTime(),alineado) +" │ "+
                alinearTexto(MAX_DESCRIPTION_CHARS,registroOperacion.getDescripcion(),alineado) +" │ "+
                alinearTexto(MAX_MONTO_CHARS,registroOperacion.getMonto().toString(),alineado) +" │ "+
                alinearTexto(MAX_BALANCE_CHARS,registroOperacion.getBalance().toString(),alineado);
        return formattedOutput;

    }






    /**
     * Genera una línea de texto con las cabeceras de cada columna para la confección de
     * una tabla y otra con unos caracteres especiales a modo de separador horizontal.
     * <p>
     *        <blockquote><pre>
     FECHA Y HORA    │   DESCRIPCIÓN   │    MONTO     │   BALANCE
     ────────────────────────────────────────────────────────────
     *        </pre></blockquote>
     * </p>
     * @param alineado
     * @return {@link String} de dos líneas separadas por un salto de línea.
     */
    public static String generarCabeceras(Alineado alineado) {
        char[] guiones =new char[MAX_DATE_CHARS+MAX_DESCRIPTION_CHARS+MAX_MONTO_CHARS+MAX_BALANCE_CHARS+3*3];
        for (int i = 0; i < guiones.length; i++) {
            guiones[i]='─';
        }
        String formattedOutput = alinearTexto(MAX_DATE_CHARS, Messages.get("formmatter.fecha.hora"),alineado) +" │ "+
                alinearTexto(MAX_DESCRIPTION_CHARS,Messages.get("formmatter.descripcion") ,alineado) +" │ "+
                alinearTexto(MAX_MONTO_CHARS,Messages.get("formmatter.monto"),alineado) +" │ "+
                alinearTexto(MAX_BALANCE_CHARS,Messages.get("formmatter.balance"),alineado)+"\n"+ new String(guiones);
        return formattedOutput;
    }

    /**
     * Centra el texto recibido como argumento dentro de los espacios disponibles. En caso de exceder el espacio disponible
     * se procede a truncar el texto y colocar [...]
     * @param espaciosDisponibles
     * @param texto
     * @param alineado
     * @return {@link String} con el texto centrado.
     */

    public  static String alinearTexto(int espaciosDisponibles, String texto, Alineado alineado){
        if (texto.length() > espaciosDisponibles-2) {
            texto = texto.substring(0, espaciosDisponibles-3);
            texto +="...";
        }
        int espaciosLibres = espaciosDisponibles-texto.length();
        char[] textoChars =texto.toCharArray();
        char[] outputChars = new char[espaciosDisponibles];
        for (int i = 0; i < outputChars.length; i++) {
            outputChars[i]=' ';
        }

        if(alineado==Alineado.CENTRO){
            int offset = espaciosLibres/2;

           for(int i=0;i<textoChars.length;i++){
               outputChars[i+offset]=textoChars[i];

           }


        } else if (alineado==Alineado.IZQUIERDA){
            for(int i=0;i<textoChars.length;i++){
                outputChars[i]=textoChars[i];

            }
        } else if (alineado==Alineado.DERECHA){
            int offset = espaciosLibres;
            for(int i=0;i<textoChars.length;i++){
                outputChars[i+offset]=textoChars[i];

            }
        }
        return new String(outputChars);

    }

}
