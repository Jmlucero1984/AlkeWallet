package org.josemalucero.app;

import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Credencial;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.repositorios.RepositorioMonedas;
import org.josemalucero.servicio.repositorios.RepositorioUsuarios;
import org.junit.jupiter.api.*;

import java.util.Locale;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AlkeWallet_Interacciones_Alerts_Test {

    ConsoleInputStub consoleInputStub;
    ConsoleOutputStub consoleOutputStub;

    ContextoUsuario contextoUsuario;
    AlkeWalletFake alkeWalletFake;
    String usuario_clp_nombre;
    String usuario_clp_apellido;
    String usuario_clp_clave;
    String usuario_ars_nombre;
    String usuario_ars_apellido;
    String usuario_ars_clave;
    InteraccionesEncadenables interacciones;
    Credencial credencial_usr_ars;
    Credencial credencial_usr_clp;


    @BeforeAll
    void setUpForAll(){
        Locale locale =Locale.forLanguageTag("es");
        Messages.init(locale);
        usuario_ars_nombre="Javier";
        usuario_ars_apellido="Monsalvo";
        usuario_ars_clave="Jmonsal";
        usuario_clp_nombre="Felipe";
        usuario_clp_apellido="Rojas";
        usuario_clp_clave="FeluRoj";
        credencial_usr_ars = new Credencial(usuario_ars_nombre,usuario_ars_apellido,usuario_ars_clave);
        credencial_usr_clp = new Credencial(usuario_clp_nombre,usuario_clp_apellido,usuario_clp_clave);
        consoleInputStub = new ConsoleInputStub();
        consoleOutputStub = new ConsoleOutputStub();// new ConsoleOutputStub();


        RepositorioMonedas.crearMonedasBasicas();
        RepositorioUsuarios.crearYAgregarUsuarioYAsignarCuenta(credencial_usr_ars,"ARS");
        RepositorioUsuarios.crearYAgregarUsuarioYAsignarCuenta(credencial_usr_clp,"CLP");

    }

    @BeforeEach
    void setUp() {
        contextoUsuario = new ContextoUsuario(consoleInputStub, consoleOutputStub);
        alkeWalletFake = new AlkeWalletFake(contextoUsuario, false,false);
        interacciones = new InteraccionesEncadenables(consoleInputStub, alkeWalletFake)
                .logInHastaOperacionesUsuarioExistenteYConCuenta(credencial_usr_ars);

    }

    @AfterEach
    void tearDown() {
        interacciones=null;
        consoleOutputStub.clearAlertStack();
    }

    @Test
    void deposito_valido() {

        String depositoStr = "100.00";
        interacciones.depositarEnCuenta(depositoStr);
        Assertions.assertEquals(Messages.get("deposito.realizado"),consoleOutputStub.popAlert());
    }
    @Test
    void deposito_nulo() {

        String depositoStr = "0";
        interacciones.depositarEnCuenta(depositoStr);
        Assertions.assertEquals(Messages.get("cantidad.invalida"),consoleOutputStub.popAlert());
    }

    @Test
    void deposito_no_negativos() {
        String depositoStr = "-1.00";
        interacciones.depositarEnCuenta(depositoStr);
        Assertions.assertEquals(Messages.get("alerta.no.puede.ingresar.numeros.negativos") +". "+Messages.get("intente.nuevamente"),consoleOutputStub.popAlert());
    }




}