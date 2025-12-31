package org.josemalucero.servicio;

import org.josemalucero.dominio.operacion.RegistroOperacion;

public class FormateadorDeRegistroAImprimir {
    private static final int MAX_DESCRIPTION_CHARS = 70;
    private static final int MAX_BALANCE_CHARS = 30;
    private static final int MAX_MONTO_CHARS = 30;
    private static final int MAX_DATE_CHARS = 40;
    public enum Alineado {IZQUIERDA,CENTRO, DERECHA};
    private enum Fill {PRE,POST};


    public static String FormatearRegistro(RegistroOperacion registroOperacion, Alineado alineado) {
        String formattedOutput = alinearTexto(MAX_DATE_CHARS,registroOperacion.getFormattedDateTime(),alineado) +" | "+
                alinearTexto(MAX_DESCRIPTION_CHARS,registroOperacion.getDescripcion(),alineado) +" | "+
                alinearTexto(MAX_MONTO_CHARS,registroOperacion.getMonto().toString(),alineado) +" | "+
                alinearTexto(MAX_BALANCE_CHARS,registroOperacion.getBalance().toString(),alineado);
        return formattedOutput;

    }

    public static String GenerarCabeceras(Alineado alineado) {
        char[] guiones =new char[MAX_DATE_CHARS+MAX_DESCRIPTION_CHARS+MAX_MONTO_CHARS+MAX_BALANCE_CHARS+3*3];
        for (int i = 0; i < guiones.length; i++) {
            guiones[i]='-';
        }
        String formattedOutput = alinearTexto(MAX_DATE_CHARS,"FECHA Y HORA",alineado) +" | "+
                alinearTexto(MAX_DESCRIPTION_CHARS,"DESCRIPCIÓN",alineado) +" | "+
                alinearTexto(MAX_MONTO_CHARS,"MONTO",alineado) +" | "+
                alinearTexto(MAX_BALANCE_CHARS,"BALANCE",alineado)+"\n"+ new String(guiones);
        return formattedOutput;
    }



    private  static String alinearTexto(int espaciosDisponibles, String texto, Alineado alineado){
        if (texto.length() > espaciosDisponibles-2) {
            texto = texto.substring(0, espaciosDisponibles-2);
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
