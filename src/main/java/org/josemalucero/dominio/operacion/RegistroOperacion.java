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
