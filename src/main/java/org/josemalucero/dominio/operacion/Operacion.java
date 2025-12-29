package org.josemalucero.dominio.operacion;
import org.josemalucero.dominio.cuenta.CuentaRegular;

import java.math.BigDecimal;

public abstract class Operacion {

    //protected final String id;
    protected final CuentaRegular cuentaRegular;
    //protected final LocalDateTime fecha;
    protected EstadoOperacion estado;

    public Operacion(CuentaRegular cuentaRegular) {
        this.cuentaRegular = cuentaRegular;

    }

    public abstract void ejecutar();


}
