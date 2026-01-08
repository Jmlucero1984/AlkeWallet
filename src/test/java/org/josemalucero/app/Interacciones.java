package org.josemalucero.app;

import org.josemalucero.dominio.estado.EstadoUsuario;

public class Interacciones {
    ConsoleInputStub consoleInputStub;
    AlkeWalletFake alkeWalletFake;

    public Interacciones(ConsoleInputStub consoleInputStub, AlkeWalletFake alkeWalletFake) {
        this.alkeWalletFake = alkeWalletFake;
        this.consoleInputStub = consoleInputStub;
    }

    public EstadoUsuario crearUsuario(String nombre, String apellido, String clave) {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"2","1",nombre, apellido, clave,clave,"2"});
        alkeWalletFake.runBySteps(3);
        return alkeWalletFake.contextoUsuario.getEstadoActual();
    }
    public EstadoUsuario logInUsuarioYAsignarCuentaCLPAUsuario(String nombre, String apellido, String clave) {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"1","1",nombre, apellido, clave,"1","\n","9","2"});
        alkeWalletFake.runBySteps(5);
        return  alkeWalletFake.contextoUsuario.getEstadoActual();
    }
    public EstadoUsuario logInUsuarioYAsignarCuentaARSAUsuario(String nombre, String apellido, String clave) {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"1","1",nombre, apellido, clave,"2","\n","9","2"});
        alkeWalletFake.runBySteps(5);
        return  alkeWalletFake.contextoUsuario.getEstadoActual();
    }
    public EstadoUsuario logInHastaOperacionesUsuarioExistenteYConCuenta(String nombre, String apellido, String clave) {
        consoleInputStub.addSerieDeRespuestasString(new String[]{"1","1",nombre, apellido, clave});
        alkeWalletFake.runBySteps(2);
        return alkeWalletFake.contextoUsuario.getEstadoActual();
    }
    public EstadoUsuario depositarEnCuenta(String cantidad) {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"3",cantidad,"\n"});
        alkeWalletFake.runBySteps(2);
        return alkeWalletFake.contextoUsuario.getEstadoActual();
    }

    public EstadoUsuario retirarDeCuenta(String cantidad) {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"4",cantidad,"\n"});
        alkeWalletFake.runBySteps(2);
        return alkeWalletFake.contextoUsuario.getEstadoActual();
    }
    public EstadoUsuario logOutDesdeOperaciones() {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"9","2"});
        alkeWalletFake.runBySteps(2);
        return alkeWalletFake.contextoUsuario.getEstadoActual();
    }
}
