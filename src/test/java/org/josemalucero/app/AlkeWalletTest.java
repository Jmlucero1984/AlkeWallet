package org.josemalucero.app;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.servicio.providers.ConsoleOutputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.repositorios.RepositorioMonedas;
import org.josemalucero.servicio.repositorios.RepositorioUsuarios;
import org.josemalucero.servicio.repositorios.RespositorioCuentas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AlkeWalletTest {
    ConsoleInputStub consoleInputStub;
    //ConsoleOutputStub consoleOutputStub;
    ConsoleOutputProvider consoleOutputProvider;
    ContextoUsuario contextoUsuario;
    AlkeWalletFake alkeWalletFake;

    @BeforeEach
    void setUp(){
        Locale locale =Locale.forLanguageTag("es");
        Messages.init(locale);
        consoleInputStub = new ConsoleInputStub();
        consoleOutputProvider = new ConsoleOutputProvider();
        contextoUsuario = new ContextoUsuario(consoleInputStub,consoleOutputProvider);
        alkeWalletFake = new AlkeWalletFake(contextoUsuario, false,true);
    }

    @AfterEach
    void tearDown(){
        RepositorioMonedas.clearMonedasDB();
        RepositorioUsuarios.clearUsuariosDB();
        RespositorioCuentas.clearCuentasDB();
    }

    @Test
    void alkeWalletTest_1() {
        consoleInputStub.addProximaRespuestaString("1");
        alkeWalletFake.run();
       assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("LOGIN"),"No coinciden la opcion elegida con el nombre de estado");

    }
    @Test
    void alkeWalletTest_2() {
        consoleInputStub.addProximaRespuestaString("2");
        alkeWalletFake.run();
        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("REGISTRO DE NUEVO USUARIO"),"No coinciden la opcion elegida con el nombre de estado");
    }

    @Test
    void alkeWalletTest_Logueo() {
        consoleInputStub.addSerieDeRespuestasString(new String[]{"1","1","Jose","Lucero","Joselucero","\n"});
        alkeWalletFake.runBySteps(3);
        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("OPERACIONES"),"No coinciden la opcion elegida con el nombre de estado");
    }

    @Test
    void alkeWalletTest_CreacionCuenta_Logueo() {
        consoleInputStub.addSerieDeRespuestasInt(new int[]{2,1,1,4});
        consoleInputStub.addSerieDeRespuestasString(new String[]{"2","1","Carlos","Casas","CCasas","CCasas","1","Carlos","Casas","CCasas","4","\n"});
        alkeWalletFake.runBySteps(4);
        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("OPERACIONES"),"No coinciden la opcion elegida con el nombre de estado");

    }
    @Test
    void alkeWalletTest_CreacionCuenta_Logueo2() {
        consoleInputStub.addSerieDeRespuestasString(new String[]{"2","1","Berta","Arranz","BArranz","BArranz","1","Berta","Arranz","BArranz","4","\n"});
        alkeWalletFake.runBySteps(4);
        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("OPERACIONES"),"No coinciden la opcion elegida con el nombre de estado");
        consoleInputStub.addProximaRespuestaString("9");
        alkeWalletFake.runBySteps(1);
        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("LOGIN"),"No coinciden la opcion elegida con el nombre de estado");
    }

    @Test
    void alkeWalletTest_interaccion_transferencia() {
        consoleInputStub.addSerieDeRespuestasString(new String[]{"2","1","Arturo","Amelio","AAmelio","AAmelio","1","Arturo","Amelio","AAmelio","2","\n","3","15000.00","\n"});
        alkeWalletFake.runBySteps(6);
        String serialCuenta = contextoUsuario.getUsuarioLogueado().getCuentaRegular().getSerialCuenta();

        assertEquals(contextoUsuario.getUsuarioLogueado().getCuentaRegular().getBalance(),new BigDecimal("15000.00"));
        consoleInputStub.addProximaRespuestaString("9");
        alkeWalletFake.runBySteps(1);


        /* ESTADO LOGIN */
        consoleInputStub.addSerieDeRespuestasString(new String[]{"1","Jose","Lucero","Joselucero","3","15000.00","\n"});
        alkeWalletFake.runBySteps(3);
        assertEquals(new BigDecimal("15000.00"),contextoUsuario.getUsuarioLogueado().getCuentaRegular().getBalance());

        /* ESTADO OPERACIONES PARA TRANSFERIR */
        consoleInputStub.addSerieDeRespuestasString(new String[]{"5",serialCuenta,"3000.00","\n","9"});

        alkeWalletFake.runBySteps(4);


        assertTrue(alkeWalletFake.contextoUsuario.getEstadoActual().getNombreEstado().equals("LOGIN"),"No coinciden la opcion elegida con el nombre de estado");
        /* ESTADO LOGIN */

        consoleInputStub.addSerieDeRespuestasString(new String[]{"1","Arturo","Amelio","AAmelio","\n"});
        alkeWalletFake.runBySteps(2);
        assertEquals(new BigDecimal("18000.00"),contextoUsuario.getUsuarioLogueado().getCuentaRegular().getBalance());
    }
}