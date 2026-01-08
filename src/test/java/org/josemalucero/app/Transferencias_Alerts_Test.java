package org.josemalucero.app;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.estado.EstadoTransferencias;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.passwords.BCryptPasswordEncoderService;
import org.josemalucero.servicio.providers.InputProvider;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.repositorios.RepositorioUsuarios;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Transferencias_Alerts_Test {

    EstadoTransferencias estadoTransferencias;
    Usuario usuario;
    ConsoleInputStub consoleInputStub;
    ConsoleOutputStub consoleOutputStub;
    ContextoUsuario contextoUsuario;

    @BeforeAll
    void globalSetUp(){
        Locale locale =Locale.forLanguageTag("en");
        Messages.init(locale);
    }


    @BeforeEach
    void setUp(){
        usuario = new Usuario("Jose","Lucero", "undefined");
        RepositorioUsuarios.agregarUsuario(usuario);
        usuario.crearCuentRegular().setMoneda(new MonedaConvertible("ARS", Messages.get("peso.argentino"), new BigDecimal("0.00069")));
        consoleInputStub = new ConsoleInputStub();
        consoleOutputStub = new ConsoleOutputStub();
        estadoTransferencias = new EstadoTransferencias(consoleInputStub,consoleOutputStub);
        contextoUsuario = new ContextoUsuario(consoleInputStub,consoleOutputStub);
        contextoUsuario.cambiarEstado(estadoTransferencias);
        contextoUsuario.setUsuarioLogueado(usuario);

    }

    @Test
    void comandoEscapeTest(){
        contextoUsuario.procesarOpcion("esc");
        System.out.println(contextoUsuario.getEstadoActual().getNombreEstado());
        assertEquals(Messages.get("nombre.estado.operaciones"),contextoUsuario.getEstadoActual().getNombreEstado());

    }

    @Test
    void alertaAutotrasnferenciaTest(){
        String numeroCuenta = usuario.getCuentaRegular().getNumeroCuenta();
        contextoUsuario.procesarOpcion(numeroCuenta);
        Assertions.assertAll(
                ()-> assertEquals(Messages.get("alerta.autotransferencia"),consoleOutputStub.popAlert()),
                ()-> assertEquals(Messages.get("tipo.transferencia.entre.cuentas"),contextoUsuario.getEstadoActual().getNombreEstado())
        );

    }
}
