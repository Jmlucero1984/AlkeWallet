package org.josemalucero.dominio.operacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegistroOperacion {
    String descripcion;
    BigDecimal monto;
    BigDecimal balance;
    LocalDateTime dateTimeOperacion;

    public RegistroOperacion(String descripcion, BigDecimal monto, BigDecimal balance) {
        this.descripcion = descripcion;
        this.monto=monto;
        this.balance = balance;
        this.dateTimeOperacion = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return  dateTimeOperacion+" | "+descripcion+" | "+monto+" | "+balance;
    }
}
