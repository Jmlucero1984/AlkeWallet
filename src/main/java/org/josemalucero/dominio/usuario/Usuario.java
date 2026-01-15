package org.josemalucero.dominio.usuario;

import org.josemalucero.dominio.cuenta.CuentaRegular;

import org.josemalucero.servicio.repositorios.RepositorioUsuarios;
import org.josemalucero.servicio.repositorios.RespositorioCuentas;


/**
 * Esta clase modela al usuario potencial que hará uso de la plataforma.
 * @author Jose María Lucero
 */
public class Usuario {
    private String nombre;
    private String apellido;
    private String claveHasheada;
    private CuentaRegular cuentaRegular;

    public Usuario(String nombre,String apellido, String claveHasheada) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.claveHasheada = claveHasheada;

    }

    /**
     * Crea una {@link CuentaRegular} indispensable para que el usuario pueda hacer uso de la plataforma. Posteriormente la adhiere
     * al {@link RepositorioUsuarios}.
     * @return {@link CuentaRegular} la cuenta recientemente creada y adherida.
     */
    public CuentaRegular crearCuentRegular(){
        this.cuentaRegular = new CuentaRegular();
        return RespositorioCuentas.adherirCuenta(this.cuentaRegular );

    }

    /**
     * Crea una {@link CuentaRegular} indispensable para que el usuario pueda hacer uso de la plataforma,
     * permitiendo definir el número de la misma. Posteriormente la adhiere al {@link RepositorioUsuarios}.
     * @param numeroCuenta
     * @return {@link CuentaRegular} la cuenta recientemente creada y adherida.
     */
    public CuentaRegular crearCuentRegular(String numeroCuenta){
        this.cuentaRegular = new CuentaRegular(numeroCuenta);
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
        return claveHasheada;
    }

    public void setClave(String claveHasheada) {
        this.claveHasheada = claveHasheada;
    }
}
