package org.josemalucero.app;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.operacion.OperacionTransferencia;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.RepositorioMonedas;
import org.josemalucero.servicio.RepositorioUsuarios;
import org.josemalucero.servicio.RespositorioCuentas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.math.BigDecimal;

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

    @AfterEach
    void tearDown(){
        RepositorioMonedas.clearMonedasDB();
        RepositorioUsuarios.clearUsuariosDB();
        RespositorioCuentas.clearCuentasDB();
    }




    @Test
    void alkeWalletTest_1() {
        consoleInputStub.addProximaRespuestaInt(1);
        alkeWalletFake.run();
       assertTrue(alkeWalletFake.contexto.getEstadoActual().getNombreEstado().equals("LOGIN"),"No coinciden la opcion elegida con el nombre de estado");

    }
    @Test
    void alkeWalletTest_2() {
        consoleInputStub.addProximaRespuestaInt(2);
        alkeWalletFake.run();
        assertTrue(alkeWalletFake.contexto.getEstadoActual().getNombreEstado().equals("REGISTRO DE NUEVO USUARIO"),"No coinciden la opcion elegida con el nombre de estado");

    }

    @Test
    void alkeWalletTest_Logueo() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1,1,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Jose","Lucero","Joselucero"});
        alkeWalletFake.runBySteps(3);

        assertTrue(alkeWalletFake.contexto.getEstadoActual().getNombreEstado().equals("OPERACIONES"),"No coinciden la opcion elegida con el nombre de estado");

    }
    @Test
    void alkeWalletTest_CreacionCuenta_Logueo() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{2,1,1,4});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Carlos","Casas","CCasas","CCasas","Carlos","Casas","CCasas"});
        alkeWalletFake.runBySteps(4);

        assertTrue(alkeWalletFake.contexto.getEstadoActual().getNombreEstado().equals("OPERACIONES"),"No coinciden la opcion elegida con el nombre de estado");

    }
    @Test
    void alkeWalletTest_CreacionCuenta_Logueo2() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{2,1,1,4});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Berta","Arranz","BArranz","BArranz","Berta","Arranz","BArranz"});
        alkeWalletFake.runBySteps(4);

        assertTrue(alkeWalletFake.contexto.getEstadoActual().getNombreEstado().equals("OPERACIONES"),"No coinciden la opcion elegida con el nombre de estado");
        consoleInputStub.addProximaRespuestaInt(8);
        alkeWalletFake.runBySteps(1);
        assertTrue(alkeWalletFake.contexto.getEstadoActual().getNombreEstado().equals("LOGIN"),"No coinciden la opcion elegida con el nombre de estado");

    }
    @Test
    void alkeWalletTest_interaccion_transferencia() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{2,1,1,2,3,8});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Arturo","Amelio","AAmelio","AAmelio","Arturo","Amelio","AAmelio","15000.00"});
        alkeWalletFake.runBySteps(4);
        String serialCuenta = contextoUsuario.getUsuarioLogueado().getCuentaRegular().getSerialCuenta();
        alkeWalletFake.runBySteps(1);
        assertEquals(contextoUsuario.getUsuarioLogueado().getCuentaRegular().getBalance(),new BigDecimal("15000.00"));

        alkeWalletFake.runBySteps(1);

        /* ESTADO LOGIN */
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1,1,3});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Jose","Lucero","Joselucero","15000.00"});
        alkeWalletFake.runBySteps(3);
        assertEquals(contextoUsuario.getUsuarioLogueado().getCuentaRegular().getBalance(),new BigDecimal("15000.00"));

        /* ESTADO OPERACIONES PARA TRANSFERIR */
        consoleInputStub.addSerieDeRespuestasInt(new int[]{5,1,8});
        consoleInputStub.setProximaRespuestaString(serialCuenta);
        consoleInputStub.setProximaRespuestaString("3000.00");
        alkeWalletFake.runBySteps(1);

        alkeWalletFake.runBySteps(2);

        assertTrue(alkeWalletFake.contexto.getEstadoActual().getNombreEstado().equals("LOGIN"),"No coinciden la opcion elegida con el nombre de estado");
        /* ESTADO LOGIN */
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Arturo","Amelio","AAmelio"});
        alkeWalletFake.runBySteps(1);
        assertEquals(contextoUsuario.getUsuarioLogueado().getCuentaRegular().getBalance(),new BigDecimal("18000.00"));


    }

}