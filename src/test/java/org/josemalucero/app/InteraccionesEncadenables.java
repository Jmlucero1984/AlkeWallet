package org.josemalucero.app;

import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.usuario.Credencial;
import org.josemalucero.servicio.repositorios.RepositorioMonedas;

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
        consoleInputStub.addSerieDeRespuestasString(new String[]{"2","1",credencial.getNombre(), credencial.getApellido(), credencial.getClave(),credencial.getClave(),"2"});
        alkeWalletFake.runBySteps(3);
        return  this;
    }
    public InteraccionesEncadenables logInUsuarioYAsignarCuentaCLPAUsuario(Credencial credencial) {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"1","1",credencial.getNombre(), credencial.getApellido(), credencial.getClave(),"1","\n","9","2",});
        alkeWalletFake.runBySteps(5);
        return  this;
    }
    public InteraccionesEncadenables logInUsuarioYAsignarCuentaARSAUsuario(Credencial credencial) {
        consoleInputStub.addSerieDeRespuestasString(new String[]{"1","1",credencial.getNombre(), credencial.getApellido(), credencial.getClave(),"2","\n","9","2",});
        alkeWalletFake.runBySteps(5);
        return  this;
    }

    public InteraccionesEncadenables transfACuentaMismaMoneda(String numeroCuenta, String monto) {
        consoleInputStub.clearRespuestasString();
        consoleInputStub.addSerieDeRespuestasString(new String[]{"5",numeroCuenta,monto,"\n"});
        alkeWalletFake.runBySteps(3);
        return  this;
    }

    public InteraccionesEncadenables mostrarHistorial() {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"8","\n"});
        alkeWalletFake.runBySteps(1);
        return  this;
    }

    public InteraccionesEncadenables transfACuentaDifMonedaMontoMonedaOrigen(String numeroCuenta, String monto) {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"5",numeroCuenta,"1",monto,"\n"});
        alkeWalletFake.runBySteps(4);
        return  this;
    }
    public InteraccionesEncadenables transfACuentaDifMonedaMontoMonedaDestino(String numeroCuenta, String monto) {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"5",numeroCuenta,"2",monto,"\n"});
        alkeWalletFake.runBySteps(4);
        return  this;
    }

    public InteraccionesEncadenables logInHastaOperacionesUsuarioExistenteYConCuenta(Credencial credencial) {
        consoleInputStub.addSerieDeRespuestasString(new String[]{"1","1",credencial.getNombre(), credencial.getApellido(), credencial.getClave()});
        alkeWalletFake.runBySteps(2);
        return  this;
    }
    public InteraccionesEncadenables depositarEnCuenta(String monto) {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"3",monto,"\n"});
        alkeWalletFake.runBySteps(2);
        return  this;
    }

    public InteraccionesEncadenables retirarDeCuenta(String monto) {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"4",monto,"\n"});
        alkeWalletFake.runBySteps(2);
        return  this;
    }

    public InteraccionesEncadenables convertirCuentaAOtraMoneda(MonedaConvertible monedaConvertible) {
        int opcionDeMonedaDeDestino = RepositorioMonedas.getMonedasDB().indexOf(monedaConvertible)+1;
        consoleInputStub.addSerieDeRespuestasString(new String[]{"7",String.valueOf(opcionDeMonedaDeDestino),"\n"});
        alkeWalletFake.runBySteps(2);
        return  this;
    }
    public InteraccionesEncadenables logOutDesdeOperaciones() {

        consoleInputStub.addSerieDeRespuestasString(new String[]{"9","2"});
        alkeWalletFake.runBySteps(2);
        return  this;
    }
}
