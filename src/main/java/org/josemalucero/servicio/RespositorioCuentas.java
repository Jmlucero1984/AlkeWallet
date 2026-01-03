package org.josemalucero.servicio;

import org.josemalucero.dominio.cuenta.Cuenta;
import org.josemalucero.dominio.cuenta.CuentaRegular;

import java.util.ArrayList;
import java.util.Random;

public class RespositorioCuentas {
    private static ArrayList<Cuenta> cuentasDB= new ArrayList<>();

    public static CuentaRegular adherirCuenta(CuentaRegular cuentaRegular){

        String serialNuevaCuenta;
        while(true){
            String tempSerialCuenta = generarSerial(3,4);
            if(!cuentasDB.stream().anyMatch(c -> c.getSerialCuenta().equals(tempSerialCuenta))){
                serialNuevaCuenta=tempSerialCuenta;
                break;
            };
        }

        cuentaRegular.setSerialCuenta(serialNuevaCuenta);
        return cuentaRegular;

    }
public static void clearCuentasDB(){
        cuentasDB.clear();
}

    public static String generarSerial(int cantBlocks, int charsPerBlock){
        Random random = new Random();
        int totalChars=cantBlocks*charsPerBlock+cantBlocks-1;
        char[] chainOfChars = new char[totalChars];
        int delta=0;
        for(int j=0; j<totalChars; j++) {
                // 'a' = 97, 'z' = 122
                delta++;
                if(j>0 && delta%(charsPerBlock+1)==0)  {
                    delta=0;
                    chainOfChars[j] = ' ';
                    continue;
                }

                chainOfChars[j] = (char) ('0' + random.nextInt(10));
        }
        return new String(chainOfChars);

    }
}
