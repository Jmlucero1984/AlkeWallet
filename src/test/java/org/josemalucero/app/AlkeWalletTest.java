package org.josemalucero.app;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.ConsoleOutputProvider;
import org.josemalucero.servicio.repositorios.RepositorioMonedas;
import org.josemalucero.servicio.repositorios.RepositorioUsuarios;
import org.josemalucero.servicio.repositorios.RespositorioCuentas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AlkeWalletTest {
    ConsoleInputStub consoleInputStub;
    //ConsoleOutputStub consoleOutputStub;
    ConsoleOutputProvider consoleOutputProvider;
    ContextoUsuario contextoUsuario;
    AlkeWalletFake alkeWalletFake;

    @BeforeEach
    void setUp(){
        consoleInputStub = new ConsoleInputStub();
        consoleOutputProvider = new ConsoleOutputProvider();
        contextoUsuario = new ContextoUsuario(consoleInputStub,consoleOutputProvider);
        alkeWalletFake = new AlkeWalletFake(contextoUsuario, false);
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
       assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("LOGIN"),"No coinciden la opcion elegida con el nombre de estado");

    }
    @Test
    void alkeWalletTest_2() {
        consoleInputStub.addProximaRespuestaInt(2);
        alkeWalletFake.run();
        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("REGISTRO DE NUEVO USUARIO"),"No coinciden la opcion elegida con el nombre de estado");
    }

    @Test
    void alkeWalletTest_Logueo() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1,1,2});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Jose","Lucero","Joselucero","\n"});
        alkeWalletFake.runBySteps(3);
        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("OPERACIONES"),"No coinciden la opcion elegida con el nombre de estado");
    }

    @Test
    void alkeWalletTest_CreacionCuenta_Logueo() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{2,1,1,4});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Carlos","Casas","CCasas","CCasas","Carlos","Casas","CCasas","\n"});
        alkeWalletFake.runBySteps(4);
        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("OPERACIONES"),"No coinciden la opcion elegida con el nombre de estado");

    }
    @Test
    void alkeWalletTest_CreacionCuenta_Logueo2() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{2,1,1,4});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Berta","Arranz","BArranz","BArranz","Berta","Arranz","BArranz","\n"});
        alkeWalletFake.runBySteps(4);
        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("OPERACIONES"),"No coinciden la opcion elegida con el nombre de estado");
        consoleInputStub.addProximaRespuestaInt(9);
        alkeWalletFake.runBySteps(1);
        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("LOGIN"),"No coinciden la opcion elegida con el nombre de estado");
    }

    @Test
    void alkeWalletTest_interaccion_transferencia() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{2,1,1,2,3});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Arturo","Amelio","AAmelio","AAmelio","Arturo","Amelio","AAmelio","\n","15000.00","\n"});
        alkeWalletFake.runBySteps(5);
        String serialCuenta = contextoUsuario.getUsuarioLogueado().getCuentaRegular().getSerialCuenta();

        assertEquals(contextoUsuario.getUsuarioLogueado().getCuentaRegular().getBalance(),new BigDecimal("15000.00"));
        consoleInputStub.addProximaRespuestaInt(9);
        alkeWalletFake.runBySteps(1);


        /* ESTADO LOGIN */
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1,3});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Jose","Lucero","Joselucero","15000.00","\n"});
        alkeWalletFake.runBySteps(2);
        assertEquals(new BigDecimal("15000.00"),contextoUsuario.getUsuarioLogueado().getCuentaRegular().getBalance());

        /* ESTADO OPERACIONES PARA TRANSFERIR */
        consoleInputStub.addSerieDeRespuestasInt(new int[]{5,1,9});
        consoleInputStub.setProximaRespuestaString(serialCuenta);

        consoleInputStub.setProximaRespuestaString("3000.00");
        consoleInputStub.setProximaRespuestaString("\n");
        consoleInputStub.setProximaRespuestaString("\n");
        alkeWalletFake.runBySteps(3);


        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("LOGIN"),"No coinciden la opcion elegida con el nombre de estado");
        /* ESTADO LOGIN */
        consoleInputStub.addSerieDeRespuestasInt(new int[]{1});
        consoleInputStub.clearRespuestasString();
        consoleInputStub.addSerieDeRespuestasString(new String[]{"Arturo","Amelio","AAmelio","\n"});
        alkeWalletFake.runBySteps(1);
        assertEquals(new BigDecimal("18000.00"),contextoUsuario.getUsuarioLogueado().getCuentaRegular().getBalance());
    }
}