package org.josemalucero.servicio;

import org.josemalucero.dominio.moneda.Moneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.usuario.Usuario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class RepositorioMonedas {
    public static ArrayList<MonedaConvertible> getMonedasDB() {
        return monedasDB;
    }

    private static ArrayList<MonedaConvertible> monedasDB= new ArrayList<>();

    public static void crearMonedasBasicas(){

        MonedaConvertible pesoChileno = new MonedaConvertible("CLP","Peso Chileno", new BigDecimal("0.001"));
        MonedaConvertible pesoArgentino = new MonedaConvertible("ARS","Peso Argentino", new BigDecimal("0.00069"));
        MonedaConvertible euro = new MonedaConvertible("EUR","Euro", new BigDecimal("1.18"));
        MonedaConvertible dolar = new MonedaConvertible("USD","Dólar", new BigDecimal("1.00"));
        MonedaConvertible yuan = new MonedaConvertible("CNH","Yuan", new BigDecimal("0.14"));
        monedasDB.addAll(Arrays.asList(pesoChileno,pesoArgentino,euro,dolar,yuan));


    }
}
