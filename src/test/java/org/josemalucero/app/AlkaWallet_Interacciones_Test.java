package org.josemalucero.app;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.estados.EstadoUsuario;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.RepositorioMonedas;
import org.josemalucero.servicio.RepositorioUsuarios;
import org.josemalucero.servicio.RespositorioCuentas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlkaWallet_Interacciones_Test {


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
    void crearUsarioInteractionTest(){
        Interacciones interacciones = new Interacciones(consoleInputStub,alkeWalletFake);
        EstadoUsuario estadoActual = interacciones.crearUsuario("Pedro","Nuñez","PM");

        Assertions.assertAll(
                ()->assertTrue(RepositorioUsuarios.consultarUsuario("Pedro","Nuñez").isPresent()),
                ()->assertEquals(estadoActual.getNombreEstado(),"ENTRADA")
        );
    }

    @Test
    void logInUsuarioYAsignarCuentaCLPAUsuarioTest(){
        Interacciones interacciones = new Interacciones(consoleInputStub,alkeWalletFake);
        interacciones.crearUsuario("Pedro","Nuñez","PM");
        EstadoUsuario estadoActual= interacciones.logInUsuarioYAsignarCuentaCLPAUsuario("Pedro","Nuñez","PM");
        Optional<Usuario> usuarioCreado = RepositorioUsuarios.consultarUsuario("Pedro","Nuñez");
        Assertions.assertAll(
                ()->assertTrue(usuarioCreado.isPresent()),
                ()->assertTrue(usuarioCreado.get().getCuentaRegular().getMonedaConvertible().getCodigo().equals("CLP")),
                ()->assertEquals(estadoActual.getNombreEstado(),"ENTRADA")
        );
    }

    @Test
    void logInHastaOperacionesUsuarioConCuentaAndLogOutTest() {
        String nombre = "Pedro";
        String apellido = "Muñoz";
        String clave = "PM";
        Interacciones interacciones = new Interacciones(consoleInputStub, alkeWalletFake);
        interacciones.crearUsuario(nombre, apellido, clave);
        interacciones.logInUsuarioYAsignarCuentaCLPAUsuario(nombre, apellido, clave);
        EstadoUsuario estadoUsuario = interacciones.logInHastaOperacionesUsuarioExistenteYConCuenta(nombre,apellido,clave);
        Assertions.assertEquals("OPERACIONES",estadoUsuario.getNombreEstado());
        estadoUsuario = interacciones.logOutDesdeOperaciones();
        Assertions.assertEquals("ENTRADA",estadoUsuario.getNombreEstado());

    }

    @Test
    void depositoEnCuentaCLP() {
        String nombre = "Pedro";
        String apellido = "Muñoz";
        String clave = "PM";
        String depositoStr = "15000.00";
        BigDecimal deposito = new BigDecimal(depositoStr);
        Interacciones interacciones = new Interacciones(consoleInputStub, alkeWalletFake);
        interacciones.crearUsuario(nombre, apellido, clave);
        interacciones.logInUsuarioYAsignarCuentaCLPAUsuario(nombre, apellido, clave);
        EstadoUsuario estadoUsuario = interacciones.logInHastaOperacionesUsuarioExistenteYConCuenta(nombre,apellido,clave);
        Assertions.assertEquals("OPERACIONES",estadoUsuario.getNombreEstado());
        estadoUsuario= interacciones.depositarEnCuenta(depositoStr);
        Assertions.assertEquals("OPERACIONES",estadoUsuario.getNombreEstado());
        estadoUsuario = interacciones.logOutDesdeOperaciones();
        CuentaRegular cuentaRegular = RepositorioUsuarios.consultarUsuario(nombre,apellido).get().getCuentaRegular();
        Assertions.assertEquals("ENTRADA",estadoUsuario.getNombreEstado());
        Assertions.assertEquals(deposito,cuentaRegular.getBalance());
    }

    @Test
    void depositoYRetiroEnCuentaCLP() {
        String nombre = "Pedro";
        String apellido = "Muñoz";
        String clave = "PM";
        String depositoStr = "15000.00";
        BigDecimal deposito = new BigDecimal(depositoStr);
        String retiroStr = "1560.50";
        BigDecimal retiro = new BigDecimal(retiroStr);
        BigDecimal balanceEsperado = deposito.subtract(retiro);
        Interacciones interacciones = new Interacciones(consoleInputStub, alkeWalletFake);
        interacciones.crearUsuario(nombre, apellido, clave);
        interacciones.logInUsuarioYAsignarCuentaCLPAUsuario(nombre, apellido, clave);
        EstadoUsuario estadoUsuario = interacciones.logInHastaOperacionesUsuarioExistenteYConCuenta(nombre,apellido,clave);
        Assertions.assertEquals("OPERACIONES",estadoUsuario.getNombreEstado());
        estadoUsuario= interacciones.depositarEnCuenta(depositoStr);
        Assertions.assertEquals("OPERACIONES",estadoUsuario.getNombreEstado());
        estadoUsuario=interacciones.retirarDeCuenta(retiroStr);
        Assertions.assertEquals("OPERACIONES",estadoUsuario.getNombreEstado());
        estadoUsuario = interacciones.logOutDesdeOperaciones();
        CuentaRegular cuentaRegular = RepositorioUsuarios.consultarUsuario(nombre,apellido).get().getCuentaRegular();
        Assertions.assertEquals("ENTRADA",estadoUsuario.getNombreEstado());
        Assertions.assertEquals(balanceEsperado,cuentaRegular.getBalance());
    }



}
