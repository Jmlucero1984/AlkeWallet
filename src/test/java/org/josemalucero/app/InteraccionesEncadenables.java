package org.josemalucero.app;

import org.josemalucero.dominio.estados.EstadoUsuario;
import org.josemalucero.dominio.usuario.Credencial;

public class InteraccionesEncadenables{
    ConsoleInputStub consoleInputStub;
    AlkeWalletFake alkeWalletFake;

    public InteraccionesEncadenables(ConsoleInputStub consoleInputStub, AlkeWalletFake alkeWalletFake) {
        this.alkeWalletFake = alkeWalletFake;
        this.consoleInputStub = consoleInputStub;
        consoleInputStub.clearRespuestasInt();
        consoleInputStub.clearRespuestasString();
    }

    public InteraccionesEncadenables crearUsuario(Credencial credencial) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{2, 1, 2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{credencial.getNombre(), credencial.getApellido(), credencial.getClave(),credencial.getClave()});
        alkeWalletFake.runBySteps(3);
        return  this;
    }
    public InteraccionesEncadenables logInUsuarioYAsignarCuentaCLPAUsuario(Credencial credencial) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1, 1, 1,8,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{credencial.getNombre(), credencial.getApellido(), credencial.getClave(),credencial.getClave()});
        alkeWalletFake.runBySteps(6);
        return  this;
    }
    public InteraccionesEncadenables logInUsuarioYAsignarCuentaARSAUsuario(Credencial credencial) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1, 1, 2,8,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{credencial.getNombre(), credencial.getApellido(), credencial.getClave(),credencial.getClave()});
        alkeWalletFake.runBySteps(6);
        return  this;
    }

    public InteraccionesEncadenables transfACuentaMismaMoneda(String numeroCuenta, String monto) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{5});
        consoleInputStub.addSerieDeRespuestasString(new String[]{numeroCuenta,monto});
        alkeWalletFake.runBySteps(1);
        return  this;
    }

    public InteraccionesEncadenables mostrarHistorial() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{7});
        alkeWalletFake.runBySteps(1);
        return  this;
    }

    public InteraccionesEncadenables transfACuentaDifMonedaMontoMonedaOrigen(String numeroCuenta, String monto) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{5});
        consoleInputStub.addSerieDeRespuestasString(new String[]{numeroCuenta,"1",monto});
        alkeWalletFake.runBySteps(1);
        return  this;
    }
    public InteraccionesEncadenables transfACuentaDifMonedaMontoMonedaDestino(String numeroCuenta, String monto) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{5,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{numeroCuenta,"2",monto});
        alkeWalletFake.runBySteps(1);
        return  this;
    }

    public InteraccionesEncadenables logInHastaOperacionesUsuarioExistenteYConCuenta(Credencial credencial) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1,1});
        consoleInputStub.addSerieDeRespuestasString(new String[]{credencial.getNombre(), credencial.getApellido(), credencial.getClave(),credencial.getClave()});
        alkeWalletFake.runBySteps(2);
        return  this;
    }
    public InteraccionesEncadenables depositarEnCuenta(String monto) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{3});
        consoleInputStub.addSerieDeRespuestasString(new String[]{monto});
        alkeWalletFake.runBySteps(1);
        return  this;
    }

    public InteraccionesEncadenables retirarDeCuenta(String monto) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{4});
        consoleInputStub.addSerieDeRespuestasString(new String[]{monto});
        alkeWalletFake.runBySteps(1);
        return  this;
    }
    public InteraccionesEncadenables logOutDesdeOperaciones() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{8,2});
        alkeWalletFake.runBySteps(2);
        return  this;
    }
}
