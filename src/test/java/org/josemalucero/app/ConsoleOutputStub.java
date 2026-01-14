package org.josemalucero.app;

import org.josemalucero.servicio.providers.OutputProvider;

import java.math.BigDecimal;
import java.util.Stack;

public class ConsoleOutputStub implements OutputProvider {

    Stack<String> mensajesAlerta = new Stack<>();
    Stack<String> mensajesMenu = new Stack<>();
    Stack<String> mensajesInfo= new Stack<>();
    @Override
    public void println(Object object) {

    }

    @Override
    public void printMenuln(String menu) {
        System.out.println("---- new menu ----");
        System.out.println(menu);
        mensajesMenu.push(menu);
    }

    @Override
    public void printMenu(String menu) {
        System.out.println("---- new menu ----");
        System.out.print(menu);
        mensajesMenu.push(menu);
    }

    @Override
    public void printInfoln(String info) {
        System.out.println("---- new info ----");
        System.out.println(info);
        mensajesInfo.push(info);
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
        System.out.println("---- new alert ----");
        System.out.println(alert);
        mensajesAlerta.push(alert);
    }

    public String popMenu(){
        try {
            return mensajesMenu.pop();
        } catch (Exception e){
            return "[ Sin Mensajes De Menú ]";
        }
    }

    public String popAlert(){
        try {
            return mensajesAlerta.pop();
        } catch (Exception e){
            return "[ Sin Mensajes De Alerta ]";
        }
    }
    public String popInfo(){
        try {
            return mensajesInfo.pop();
        } catch (Exception e){
            return "[ Sin Mensajes De Info ]";
        }
    }

    public void clearAlertStack() {
        mensajesAlerta.clear();
    }

    public void clearMenuStack() {
        mensajesMenu.clear();
    }

    public void clearInfotack() {
        mensajesInfo.clear();
    }



}
