package org.josemalucero.servicio.repositorios;

import org.josemalucero.dominio.cuenta.Cuenta;
import org.josemalucero.dominio.cuenta.CuentaRegular;

import java.util.ArrayList;
import java.util.Random;
/**
 * Provee un recurso de almacenamiento de {@link CuentaRegular} toda la aplicación.
 * @author José María Lucero
 */

public class RespositorioCuentas {

    private static ArrayList<Cuenta> cuentasDB= new ArrayList<>();

    /**
     * Adhiere la cuenta al repositorio. Para ello primero debe generar un N° de Cuenta, verificar qye
     * no se repita en las cuentas ya adheridas, asignarselo a la cuenta y finalmente agregarla a la base de datos.
     * @param cuentaRegular
     * @return {@link CuentaRegular} que ha sido adherida a la base de datos.
     */
    public static CuentaRegular adherirCuenta(CuentaRegular cuentaRegular){
        if(cuentaRegular.getSerialCuenta()==null) {
            String serialNuevaCuenta;
            while (true) {
                String tempSerialCuenta = generarSerial(3, 4);
                if (!cuentasDB.stream().anyMatch(c -> c.getSerialCuenta().equals(tempSerialCuenta))) {
                    serialNuevaCuenta = tempSerialCuenta;
                    break;
                }
                ;
            }

            cuentaRegular.setSerialCuenta(serialNuevaCuenta);
        }
        cuentasDB.add(cuentaRegular);
        return cuentaRegular;

    }


    /**
     * Genera una cadena de números aleatorios en formato de bloques, de una cantidad de números por bloque.
     * @param cantBlocks
     * @param charsPerBlock
     * @return {@link String} de números aleatorios con un formato determinado.
     */

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

    /**
     * Vacía el repositorio de Cuentas.
     */
    public static void clearCuentasDB(){
        cuentasDB.clear();
    }
}
