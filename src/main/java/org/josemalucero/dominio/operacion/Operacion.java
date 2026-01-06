package org.josemalucero.dominio.operacion;
import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.servicio.OutputProvider;

/**
 * Entidad básica que modela una operación genérica dentro del sistema, sobre una {@link CuentaRegular}.
 * @author José Maria Lucero
 */

public abstract class Operacion {


    protected final CuentaRegular cuentaRegular;


    protected OutputProvider outputProvider;


    public Operacion(CuentaRegular cuentaRegular, OutputProvider outputProvider) {
        this.cuentaRegular = cuentaRegular;
        this.outputProvider = outputProvider;

    }

    /**
     * Devuelve el nombre de la operación.
     * @return {@link String} de la descripción de la operación.
     */
    public String getNombreOperacion(){
        return "OPERACION";
    }

    /**
     * Permite realizar la propia operación sobre la cuenta.
     */
    public abstract void ejecutar();


}
