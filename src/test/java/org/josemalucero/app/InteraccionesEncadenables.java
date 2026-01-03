package org.josemalucero.app;

import org.josemalucero.dominio.estados.EstadoUsuario;

public class InteraccionesEncadenables{
    ConsoleInputStub consoleInputStub;
    AlkeWalletFake alkeWalletFake;

    public InteraccionesEncadenables(ConsoleInputStub consoleInputStub, AlkeWalletFake alkeWalletFake) {
        this.alkeWalletFake = alkeWalletFake;
        this.consoleInputStub = consoleInputStub;
    }

    public InteraccionesEncadenables crearUsuario(String nombre, String apellido, String clave) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{2, 1, 2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{nombre, apellido, clave,clave});
        alkeWalletFake.runBySteps(3);
        return  this;
    }
    public InteraccionesEncadenables logInUsuarioYAsignarCuentaCLPAUsuario(String nombre, String apellido, String clave) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1, 1, 1,8,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{nombre, apellido, clave});
        alkeWalletFake.runBySteps(5);
        return  this;
    }
    public InteraccionesEncadenables logInUsuarioYAsignarCuentaARSAUsuario(String nombre, String apellido, String clave) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1, 1, 2,8,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{nombre, apellido, clave,clave});
        alkeWalletFake.runBySteps(5);
        return  this;
    }
    public InteraccionesEncadenables logInHastaOperacionesUsuarioExistenteYConCuenta(String nombre, String apellido, String clave) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1,1});
        consoleInputStub.addSerieDeRespuestasString(new String[]{nombre, apellido, clave});
        alkeWalletFake.runBySteps(2);
        return  this;
    }
    public InteraccionesEncadenables depositarEnCuenta(String cantidad) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{3});
        consoleInputStub.addSerieDeRespuestasString(new String[]{cantidad});
        alkeWalletFake.runBySteps(1);
        return  this;
    }

    public InteraccionesEncadenables retirarDeCuenta(String cantidad) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{4});
        consoleInputStub.addSerieDeRespuestasString(new String[]{cantidad});
        alkeWalletFake.runBySteps(1);
        return  this;
    }
    public InteraccionesEncadenables logOutDesdeOperaciones() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{8,2});
        alkeWalletFake.runBySteps(2);
        return  this;
    }
}
