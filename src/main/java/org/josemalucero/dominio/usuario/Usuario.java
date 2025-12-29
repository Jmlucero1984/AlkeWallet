package org.josemalucero.dominio.usuario;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.servicio.RespositorioCuentas;

public class Usuario {
    private String nombre;
    private String apellido;
    private String clave;
    private CuentaRegular cuentaRegular;

    public Usuario(String nombre,String apellido, String clave) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.clave = clave;

    }

    public CuentaRegular crearCuentRegular(){
        this.cuentaRegular = new CuentaRegular();
        return RespositorioCuentas.adherirCuenta(this.cuentaRegular );

    }

    public CuentaRegular getCuentaRegular(){
        return cuentaRegular;
    }

    public String getNombreCompleto() {
        return nombre+" "+apellido;
    }

    public void setNombre(String nombre) {
        this.nombre= nombre;
    }

    public void setApellido(String apellido) {
        this.apellido= apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
