package org.josemalucero.dominio.cuenta;

import org.josemalucero.dominio.moneda.Moneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.RegistroOperacion;

import java.util.ArrayList;

public class Cuenta {
    String serialCuenta;
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

    MonedaConvertible monedaConvertible;

    public Cuenta() {

    }

    public String getSerialCuenta() {
        return serialCuenta;
    }

    public void setSerialCuenta(String serialCuenta) {
        this.serialCuenta = serialCuenta;
    }



}
