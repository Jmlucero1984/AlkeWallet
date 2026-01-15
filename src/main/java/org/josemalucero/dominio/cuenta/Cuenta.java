package org.josemalucero.dominio.cuenta;


import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.RegistroOperacion;

import java.util.ArrayList;

/**
 * Entidad más básica que modela una cuenta, definiendo su identificador, el tipo de moneda y un historial de operaciones
 *  @author José Maria Lucero
 */

public class Cuenta {

    String serialCuenta;
    MonedaConvertible monedaConvertible;
    ArrayList<RegistroOperacion> historialOperaciones = new ArrayList<>();

    public MonedaConvertible getMonedaConvertible() {
        return monedaConvertible;
    }

    public void registrarOperacion(RegistroOperacion registroOperacion){
        historialOperaciones.add(registroOperacion);
    }
    public ArrayList<RegistroOperacion> getHistorialOperaciones () {
        return historialOperaciones;
    }

    public void setMoneda(MonedaConvertible monedaConvertible) {
        this.monedaConvertible = monedaConvertible;
    }

    public String getSerialCuenta() {
        return serialCuenta;
    }

    public void setSerialCuenta(String serialCuenta) {
        this.serialCuenta = serialCuenta;
    }



}
