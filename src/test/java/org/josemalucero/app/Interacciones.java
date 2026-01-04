package org.josemalucero.app;

import org.josemalucero.dominio.estados.EstadoUsuario;

import java.math.BigDecimal;

public class Interacciones {
    ConsoleInputStub consoleInputStub;
    AlkeWalletFake alkeWalletFake;

    public Interacciones(ConsoleInputStub consoleInputStub, AlkeWalletFake alkeWalletFake) {
        this.alkeWalletFake = alkeWalletFake;
        this.consoleInputStub = consoleInputStub;
    }

    public EstadoUsuario crearUsuario(String nombre, String apellido, String clave) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{2, 1, 2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{nombre, apellido, clave,clave});
        alkeWalletFake.runBySteps(3);
        return alkeWalletFake.contexto.getEstadoActual();
    }
    public EstadoUsuario logInUsuarioYAsignarCuentaCLPAUsuario(String nombre, String apellido, String clave) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1, 1, 1,9,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{nombre, apellido, clave});
        alkeWalletFake.runBySteps(5);
        return  alkeWalletFake.contexto.getEstadoActual();
    }
    public EstadoUsuario logInUsuarioYAsignarCuentaARSAUsuario(String nombre, String apellido, String clave) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1, 1, 2,9,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{nombre, apellido, clave,clave});
        alkeWalletFake.runBySteps(5);
        return  alkeWalletFake.contexto.getEstadoActual();
    }
    public EstadoUsuario logInHastaOperacionesUsuarioExistenteYConCuenta(String nombre, String apellido, String clave) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1,1});
        consoleInputStub.addSerieDeRespuestasString(new String[]{nombre, apellido, clave});
        alkeWalletFake.runBySteps(2);
        return alkeWalletFake.contexto.getEstadoActual();
    }
    public EstadoUsuario depositarEnCuenta(String cantidad) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{3});
        consoleInputStub.addSerieDeRespuestasString(new String[]{cantidad});
        alkeWalletFake.runBySteps(1);
        return alkeWalletFake.contexto.getEstadoActual();
    }

    public EstadoUsuario retirarDeCuenta(String cantidad) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{4});
        consoleInputStub.addSerieDeRespuestasString(new String[]{cantidad});
        alkeWalletFake.runBySteps(1);
        return alkeWalletFake.contexto.getEstadoActual();
    }
    public EstadoUsuario logOutDesdeOperaciones() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{9,2});
        alkeWalletFake.runBySteps(2);
        return alkeWalletFake.contexto.getEstadoActual();
    }
}
