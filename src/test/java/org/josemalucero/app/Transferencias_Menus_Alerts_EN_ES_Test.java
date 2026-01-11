package org.josemalucero.app;

import Helpers.MenuParser;
import org.josemalucero.dominio.estado.EstadoOperaciones;
import org.josemalucero.dominio.estado.EstadoTransferencias;
import org.josemalucero.dominio.moneda.MonedaConvertible;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.providers.Messages;
import org.josemalucero.servicio.repositorios.RepositorioUsuarios;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(LocaleInvocationContextProvider.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Transferencias_Menus_Alerts_EN_ES_Test {

    EstadoTransferencias estadoTransferencias;
    Usuario usuarioARS_a;
    Usuario usuarioARS_b;
    Usuario usuarioCLP;
    BigDecimal depositoInicial_usuarioARS_a;
    BigDecimal depositoInicial_usuarioARS_b;
    BigDecimal depositoInicial_usuarioCLP;

    ConsoleInputStub consoleInputStub;
    ConsoleOutputStub consoleOutputStub;
    ContextoUsuario contextoUsuario;


//    @BeforeAll
//    void globalSetUp(){
//        Locale locale =Locale.forLanguageTag("es");
//        Messages.init(locale);
//    }


    @BeforeEach
    void setUp(){
        depositoInicial_usuarioARS_a=new BigDecimal("10000.00");
        depositoInicial_usuarioARS_b=new BigDecimal("10000.00");
        depositoInicial_usuarioCLP=new BigDecimal("10000.00");
        usuarioARS_a = new Usuario("Jose","Lucero", "undefined");
        RepositorioUsuarios.agregarUsuario(usuarioARS_a);
        usuarioARS_a.crearCuentRegular().setMoneda(new MonedaConvertible("ARS", Messages.get("peso.argentino"), new BigDecimal("0.00069")));
        usuarioARS_a.getCuentaRegular().depositar(depositoInicial_usuarioARS_a);

        usuarioARS_b = new Usuario("Miguel","Perez", "undefined");
        RepositorioUsuarios.agregarUsuario(usuarioARS_b);
        usuarioARS_b.crearCuentRegular().setMoneda(new MonedaConvertible("ARS", Messages.get("peso.argentino"), new BigDecimal("0.00069")));
        usuarioARS_b.getCuentaRegular().depositar(depositoInicial_usuarioARS_b);

        usuarioCLP = new Usuario("Javiera","Machuca", "undefined");
        RepositorioUsuarios.agregarUsuario(usuarioCLP);
        usuarioCLP.crearCuentRegular().setMoneda(new MonedaConvertible("CLP", Messages.get("peso.chileno"), new BigDecimal("0.0011")));
        usuarioCLP.getCuentaRegular().depositar(depositoInicial_usuarioCLP);

        consoleInputStub = new ConsoleInputStub();
        consoleOutputStub = new ConsoleOutputStub();
        estadoTransferencias = new EstadoTransferencias(consoleInputStub,consoleOutputStub);
        contextoUsuario = new ContextoUsuario(consoleInputStub,consoleOutputStub);
        contextoUsuario.cambiarEstado(estadoTransferencias);


    }
    @TestTemplate
    void comandoEscapeTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.procesarOpcion("esc");
        System.out.println(contextoUsuario.getEstadoActual().getNombreEstado());
        assertEquals(Messages.get("nombre.estado.operaciones"),contextoUsuario.getEstadoActual().getNombreEstado());

    }

    @TestTemplate
    void alertaAutotransferenciaTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        String numeroCuenta = usuarioARS_a.getCuentaRegular().getNumeroCuenta();

        contextoUsuario.procesarOpcion(numeroCuenta);
        Assertions.assertAll(
                ()-> assertEquals(Messages.get("alerta.autotransferencia"),consoleOutputStub.popAlert()),
                ()-> assertEquals(Messages.get("nombre.estado.tipo.transferencia.entre.cuentas"),contextoUsuario.getEstadoActual().getNombreEstado())
        );

    }

    @TestTemplate
    void seleccionarOpcionTransferirDineroTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        assertEquals(Messages.get("nombre.estado.tipo.transferencia.entre.cuentas"),contextoUsuario.getEstadoActual().getNombreEstado());

    }

    @TestTemplate
    void estadoDetectaTransfEntreCuentasMismaMonedaTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioARS_b.getCuentaRegular().getNumeroCuenta());

        assertEquals(Messages.get("nombre.estado.tipo.transferencia.a.cuenta.igual.moneda"),contextoUsuario.getEstadoActual().getNombreEstado());

    }
    @TestTemplate
    void estadoDetectaTransfEntreCuentasMismaMonedaCifraVaciaTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioARS_b.getCuentaRegular().getNumeroCuenta());
        contextoUsuario.procesarOpcion("");

        Assertions.assertAll(
                ()->assertEquals(Messages.get("nombre.estado.tipo.transferencia.a.cuenta.igual.moneda"),contextoUsuario.getEstadoActual().getNombreEstado()),
                ()->assertEquals(Messages.get("alerta.cantidad.invalida")+". "+Messages.get("intente.nuevamente"),consoleOutputStub.popAlert())
        );
    }

    @TestTemplate
    void estadoDetectaTransfEntreCuentasMismaMonedaCifraNulaTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioARS_b.getCuentaRegular().getNumeroCuenta());
        contextoUsuario.procesarOpcion("0.00");

        Assertions.assertAll(
                ()->assertEquals(Messages.get("nombre.estado.tipo.transferencia.a.cuenta.igual.moneda"),contextoUsuario.getEstadoActual().getNombreEstado()),
                ()->assertEquals(Messages.get("alert.no.transferir.monto.nulo"),consoleOutputStub.popAlert())
        );
    }

    @TestTemplate
    void estadoDetectaTransfEntreCuentasMismaMonedaCifraNegativaTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioARS_b.getCuentaRegular().getNumeroCuenta());
        contextoUsuario.procesarOpcion("-100.00");

        Assertions.assertAll(
                ()->assertEquals(Messages.get("nombre.estado.tipo.transferencia.a.cuenta.igual.moneda"),contextoUsuario.getEstadoActual().getNombreEstado()),
                ()->assertEquals(Messages.get("alerta.no.puede.ingresar.numeros.negativos") +". "+Messages.get("intente.nuevamente"),consoleOutputStub.popAlert())
        );
    }

    @TestTemplate

    void estadoDetectaTransfEntreCuentasMismaMonedaCifraIncompletaTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);

        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioARS_b.getCuentaRegular().getNumeroCuenta());
        contextoUsuario.procesarOpcion(".0");

        Assertions.assertAll(
                ()->assertEquals(Messages.get("nombre.estado.tipo.transferencia.a.cuenta.igual.moneda"),contextoUsuario.getEstadoActual().getNombreEstado()),
                ()->assertEquals(Messages.get("alerta.cantidad.invalida") +". "+Messages.get("intente.nuevamente"),consoleOutputStub.popAlert())
        );
    }

    @TestTemplate
    void estadoDetectaTransfEntreCuentasMismaMonedaExitoTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioARS_b.getCuentaRegular().getNumeroCuenta());
        contextoUsuario.procesarOpcion("100.00");

        Assertions.assertAll(
                ()->assertEquals(Messages.get("nombre.estado.operaciones"),contextoUsuario.getEstadoActual().getNombreEstado()),
                ()->assertEquals(Messages.get("transferencia.realizada"),consoleOutputStub.popInfo())
        );
    }


    @TestTemplate
    void estadoDetectaTransfEntreCuentasDifMonedaTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioCLP.getCuentaRegular().getNumeroCuenta());

        assertEquals(Messages.get("nombre.estado.tipo.transferencia.a.cuenta.distinta.moneda"),contextoUsuario.getEstadoActual().getNombreEstado());

    }
    @TestTemplate
    void estadoDetectaTransfEntreCuentasCuentaInexistenteTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion("0000 0000");
        Assertions.assertAll(
                ()->assertEquals(Messages.get("nombre.estado.tipo.transferencia.entre.cuentas"),contextoUsuario.getEstadoActual().getNombreEstado()),
                ()->assertEquals(Messages.get("alerta.numero.cuenta.inexistente.intente.nuevamente"),consoleOutputStub.popAlert())
        );


    }

    @TestTemplate
    void estadoDetectaTransfEntreCuentasDifMonedaEligeEnMonedaCuentaDestinoTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioCLP.getCuentaRegular().getNumeroCuenta());
        contextoUsuario.mostrarInformacionContextual();
        opcion = MenuParser.getOptionNumber(Messages.get("monto.moneda.cuenta.destino"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        assertEquals(Messages.get("nombre.estado.tipo.transferencia.a.cdm.moneda.destino"),contextoUsuario.getEstadoActual().getNombreEstado());

    }

    @TestTemplate
    void estadoDetectaTransfEntreCuentasDifMonedaEligeEnMonedaCuentaOrigenTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioCLP.getCuentaRegular().getNumeroCuenta());
        contextoUsuario.mostrarInformacionContextual();
        opcion = MenuParser.getOptionNumber(Messages.get("monto.moneda.cuenta.origen"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        assertEquals(Messages.get("nombre.estado.tipo.transferencia.a.cdm.moneda.origen"),contextoUsuario.getEstadoActual().getNombreEstado());

    }

    @TestTemplate
    void TransfEntre_CDM_EligeEnMonedaCuentaOrigen_OpcionFueraRangoTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioCLP.getCuentaRegular().getNumeroCuenta());
        contextoUsuario.mostrarInformacionContextual();
        contextoUsuario.procesarOpcion("3");
        System.out.println();
        Assertions.assertAll(
                ()->assertEquals(Messages.get("nombre.estado.tipo.transferencia.a.cuenta.distinta.moneda"),contextoUsuario.getEstadoActual().getNombreEstado()),
                ()->assertEquals(Messages.get("alerta.introduzca.numero.entero.dentro.rango"),consoleOutputStub.popAlert())
                );
    }
    @TestTemplate
    void TransfEntre_CDM_EligeEnMonedaCuentaOrigen_ValorInálidoLetraTest(){
        contextoUsuario.setUsuarioLogueado(usuarioARS_a);
        contextoUsuario.cambiarEstado(new EstadoOperaciones(consoleInputStub,consoleOutputStub));
        contextoUsuario.mostrarInformacionContextual();
        String opcion = MenuParser.getOptionNumber(Messages.get("opcion.transferir.dinero"),consoleOutputStub.popMenu());
        contextoUsuario.procesarOpcion(opcion);
        contextoUsuario.procesarOpcion(usuarioCLP.getCuentaRegular().getNumeroCuenta());
        contextoUsuario.mostrarInformacionContextual();
        contextoUsuario.procesarOpcion("a");
        System.out.println();
        Assertions.assertAll(
                ()->assertEquals(Messages.get("nombre.estado.tipo.transferencia.a.cuenta.distinta.moneda"),contextoUsuario.getEstadoActual().getNombreEstado()),
                ()->assertEquals(Messages.get("alerta.introduzca.opcion.valida"),consoleOutputStub.popAlert())
        );
    }


}
