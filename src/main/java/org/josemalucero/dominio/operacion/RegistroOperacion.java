package org.josemalucero.dominio.operacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa un registro con la información necesaria para hacer la trazabilidad de las operaciones sobre una cuenta.
 * @author Jose María Lucero
 */
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

    /**
     * Permite la obtención de la fecha y hora precisa del registro de una operación, en un formato
     * adecuado y acotado para presentarlo en pantalla.
     * @return {@link String} de una fecha y hora con un formato específico.
     */
    public String getFormattedDateTime(){
        return String.format("%02d",dateTimeOperacion.getDayOfMonth())+"."+
                dateTimeOperacion.getMonth()+"."+
                dateTimeOperacion.getYear()+" "+
                String.format("%02d",dateTimeOperacion.getHour())+":"+
                String.format("%02d",dateTimeOperacion.getMinute())+":"+
                String.format("%02d",dateTimeOperacion.getSecond());

    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public BigDecimal getBalance() {
        return balance;
    }



    @Override
    public String toString() {
        return  getFormattedDateTime()+" | "+descripcion+" | "+monto+" | "+balance;
    }
}
