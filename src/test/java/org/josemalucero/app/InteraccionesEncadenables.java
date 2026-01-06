package org.josemalucero.app;

import org.josemalucero.dominio.estados.EstadoUsuario;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.usuario.Credencial;
import org.josemalucero.servicio.RepositorioMonedas;

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
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1, 1, 1,9,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{credencial.getNombre(), credencial.getApellido(), credencial.getClave(),"\n"});
        alkeWalletFake.runBySteps(5);
        return  this;
    }
    public InteraccionesEncadenables logInUsuarioYAsignarCuentaARSAUsuario(Credencial credencial) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1, 1, 2,9,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{credencial.getNombre(), credencial.getApellido(), credencial.getClave(),"\n"});
        alkeWalletFake.runBySteps(5);
        return  this;
    }

    public InteraccionesEncadenables transfACuentaMismaMoneda(String numeroCuenta, String monto) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{5});
        consoleInputStub.addSerieDeRespuestasString(new String[]{numeroCuenta,monto,"\n"});
        alkeWalletFake.runBySteps(1);
        return  this;
    }

    public InteraccionesEncadenables mostrarHistorial() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{8});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"\n"});
        alkeWalletFake.runBySteps(1);
        return  this;
    }

    public InteraccionesEncadenables transfACuentaDifMonedaMontoMonedaOrigen(String numeroCuenta, String monto) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{5});
        consoleInputStub.addSerieDeRespuestasString(new String[]{numeroCuenta,"1",monto,"\n"});
        alkeWalletFake.runBySteps(1);
        return  this;
    }
    public InteraccionesEncadenables transfACuentaDifMonedaMontoMonedaDestino(String numeroCuenta, String monto) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{5});
        consoleInputStub.addSerieDeRespuestasString(new String[]{numeroCuenta,"2",monto,"\n"});
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
        consoleInputStub.addSerieDeRespuestasString(new String[]{monto,"\n"});
        alkeWalletFake.runBySteps(1);
        return  this;
    }

    public InteraccionesEncadenables retirarDeCuenta(String monto) {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{4});
        consoleInputStub.addSerieDeRespuestasString(new String[]{monto,"\n"});
        alkeWalletFake.runBySteps(1);
        return  this;
    }

    public InteraccionesEncadenables convertirCuentaAOtraMoneda(MonedaConvertible monedaConvertible) {
        int opcionDeMonedaDeDestino = RepositorioMonedas.getMonedasDB().indexOf(monedaConvertible)+1;
        consoleInputStub.addSerieDeRespuestasInt(new int[]{7,opcionDeMonedaDeDestino});
        consoleInputStub.setProximaRespuestaString("\n");
        alkeWalletFake.runBySteps(2);
        return  this;
    }
    public InteraccionesEncadenables logOutDesdeOperaciones() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{9,2});
        alkeWalletFake.runBySteps(2);
        return  this;
    }
}
