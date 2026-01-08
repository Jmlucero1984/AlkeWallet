package org.josemalucero.app;

import org.josemalucero.servicio.providers.OutputProvider;

import java.math.BigDecimal;
import java.util.Stack;

public class ConsoleOutputStub implements OutputProvider {

    Stack<String> mensajesAlerta = new Stack<>();
    @Override
    public void println(Object object) {

    }

    @Override
    public void println(String string) {

    }

    @Override
    public void println(BigDecimal bigDecimal) {

    }

    @Override
    public void println(int intNum) {

    }

    @Override
    public void print(Object object) {

    }

    @Override
    public void print(String string) {

    }

    @Override
    public void printlnAlert(String alert) {
        mensajesAlerta.push(alert);
    }

    public String popAlert(){
        try {
            return mensajesAlerta.pop();
        } catch (Exception e){
            return "[ Sin Mensajes De Alerta ]";
        }
    }

    public void clearStack() {
        mensajesAlerta.clear();
    }

}
