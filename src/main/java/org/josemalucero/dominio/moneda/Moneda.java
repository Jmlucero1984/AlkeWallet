package org.josemalucero.dominio.moneda;

import java.math.BigDecimal;

public class Moneda {
    protected String codigo;
    protected String nombre;


    public Moneda(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;

    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
