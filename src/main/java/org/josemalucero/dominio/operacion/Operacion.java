package org.josemalucero.dominio.operacion;
import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.servicio.OutputProvider;

import java.math.BigDecimal;

public abstract class Operacion {

    //protected final String id;
    protected final CuentaRegular cuentaRegular;
    //protected final LocalDateTime fecha;
    protected EstadoOperacion estado;
    protected OutputProvider outputProvider;


    public Operacion(CuentaRegular cuentaRegular, OutputProvider outputProvider) {
        this.cuentaRegular = cuentaRegular;
        this.outputProvider = outputProvider;

    }


    public String getNombreOperacion(){
        return "OPERACION";
    }

    public abstract void ejecutar();


}
