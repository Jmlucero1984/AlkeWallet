package org.josemalucero.dominio.cuenta;

import org.josemalucero.dominio.moneda.Moneda;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.operacion.RegistroOperacion;

import java.util.ArrayList;

/**
La clase {@code Cuenta} es la entidad básica con la propiedades
 y métodos elementales para registrar el activo digital, en este caso,
 la {@code MonedaConvertible}.
 <blockquote><b>Propiedades</b></blockquote>
 <li> {@code String serialCuenta} una cadena que permite identificar univocamente la cuenta.</li>
 <li> {@code MonedaConvertible monedaConvertible} representa una determinada moneda, indispensable para
 poder hacer conversiones futuras.</li>
 <li> {@code  ArrayList<RegistroOperacion> historialOperaciones} permite llevar el registro de
 todas las operaciones que se ejecutan sobre la cuenta.</li>
    @author José Maria Lucero
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
