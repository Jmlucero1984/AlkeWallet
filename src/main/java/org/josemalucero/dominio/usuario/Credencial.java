package org.josemalucero.dominio.usuario;

/**
 * Clase que permite empaquetar los datos básicos de usuario para manejo de sesión.
 * @author José María Lucero
 */
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