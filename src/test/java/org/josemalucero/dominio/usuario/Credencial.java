package org.josemalucero.dominio.usuario;

public class Credencial {
    private String nombre;
    private String apellido;
    private String clave;

    public Credencial(String nombre, String apellido, String clave) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.clave = clave;
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
}
