package org.josemalucero.servicio.repositorios;


import org.josemalucero.dominio.moneda.MonedaConvertible;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Provee un recurso de almacenamiento y consulta de monedas para toda la aplicación.
 * @author José María Lucero
 */

public class RepositorioMonedas {

    private static ArrayList<MonedaConvertible> monedasDB= new ArrayList<>();

    public static ArrayList<MonedaConvertible> getMonedasDB() {
        return monedasDB;
    }

    /**
     * Crea las monedas básicas para poder operar en la plataforma.
     */
    public static void crearMonedasBasicas(){

        MonedaConvertible pesoChileno = new MonedaConvertible("CLP","Peso Chileno", new BigDecimal("0.0011"));
        MonedaConvertible pesoArgentino = new MonedaConvertible("ARS","Peso Argentino", new BigDecimal("0.00069"));
        MonedaConvertible euro = new MonedaConvertible("EUR","Euro", new BigDecimal("1.18"));
        MonedaConvertible dolar = new MonedaConvertible("USD","Dólar", new BigDecimal("1.00"));
        MonedaConvertible yuan = new MonedaConvertible("CNH","Yuan", new BigDecimal("0.14"));
        monedasDB.addAll(Arrays.asList(pesoChileno,pesoArgentino,euro,dolar,yuan));


    }

    /**
     * Busca una determinada moneda por su código y la devuelve.
     * @param codigoMoneda
     * @return {@link MonedaConvertible} encontrada.
     */
    public static MonedaConvertible encontrarMonedaPorCodigo(String codigoMoneda){
        return monedasDB.stream().filter(t->t.getCodigo().equals(codigoMoneda)).findAny().get();
    }
    /**
     * Vacía el repositorio de Monedas.
     */

    public static void clearMonedasDB(){
        monedasDB.clear();
    }
}
