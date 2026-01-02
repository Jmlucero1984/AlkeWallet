package org.josemalucero.app;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.operacion.OperacionTransferencia;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class AlkeWalletTest {
    ConsoleInputStub consoleInputStub;
    ContextoUsuario contextoUsuario;
    AlkeWalletFake alkeWalletFake;

    @BeforeEach
    void setUp(){
        consoleInputStub = new ConsoleInputStub();
        contextoUsuario = new ContextoUsuario(consoleInputStub);
        alkeWalletFake = new AlkeWalletFake(contextoUsuario);
    }

    @Test
    void alkeWalletTest_1() {
        consoleInputStub.setProximaRespuestaInt(1);
        alkeWalletFake.run();
       assertTrue(alkeWalletFake.contexto.getEstadoActual().getNombreEstado().equals("LOGIN"),"No coinciden la opcion elegida con el nombre de estado");

    }
    @Test
    void alkeWalletTest_2() {
        consoleInputStub.setProximaRespuestaInt(2);
        alkeWalletFake.run();
        assertTrue(alkeWalletFake.contexto.getEstadoActual().getNombreEstado().equals("REGISTRO DE NUEVO USUARIO"),"No coinciden la opcion elegida con el nombre de estado");

    }

}