package org.josemalucero.app;

import Helpers.RandomBigDecimalValuesGenerator;
import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.estados.EstadoUsuario;
import org.josemalucero.dominio.moneda.ConversorMoneda;
import org.josemalucero.dominio.usuario.ContextoUsuario;
import org.josemalucero.dominio.usuario.Credencial;
import org.josemalucero.dominio.usuario.Usuario;
import org.josemalucero.servicio.RepositorioMonedas;
import org.josemalucero.servicio.RepositorioUsuarios;
import org.josemalucero.servicio.RespositorioCuentas;
import org.junit.jupiter.api.*;

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
        EstadoUsuario estadoActual = interacciones.crearUsuario("Pedro","Muñoz","Pedromuñoz");

        Assertions.assertAll(
                ()->assertTrue(RepositorioUsuarios.consultarUsuario("Pedro","Muñoz").isPresent()),
                ()->assertEquals(estadoActual.getNombreEstado(),"ENTRADA")
        );
    }

    @Test
    void logInUsuarioYAsignarCuentaCLPAUsuarioTest(){
        Interacciones interacciones = new Interacciones(consoleInputStub,alkeWalletFake);
        interacciones.crearUsuario("Pedro","Muñoz","Pedromuñoz");
        EstadoUsuario estadoActual= interacciones.logInUsuarioYAsignarCuentaCLPAUsuario("Pedro","Muñoz","Pedromuñoz");
        Optional<Usuario> usuarioCreado = RepositorioUsuarios.consultarUsuario("Pedro","Muñoz");
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
        String clave = "Pedromuñoz";
        Interacciones interacciones = new Interacciones(consoleInputStub, alkeWalletFake);
        interacciones.crearUsuario(nombre, apellido, clave);
        interacciones.logInUsuarioYAsignarCuentaCLPAUsuario(nombre, apellido, clave);
        EstadoUsuario estadoUsuario = interacciones.logInHastaOperacionesUsuarioExistenteYConCuenta(nombre,apellido,clave);
        Assertions.assertEquals("OPERACIONES",estadoUsuario.getNombreEstado());
        estadoUsuario = interacciones.logOutDesdeOperaciones();
        Assertions.assertEquals("ENTRADA",estadoUsuario.getNombreEstado());

    }

    @Test
    void depositoEnCuentaCLPTest() {
        String nombre = "Pedro";
        String apellido = "Muñoz";
        String clave = "Pedromuñoz";
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
    void depositoYRetiroEnCuentaCLPTest() {
        String nombre = "Pedro";
        String apellido = "Muñoz";
        String clave = "Pedromuñoz";
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

    @Test
    void depositoYRetiroEnCuentaCLPEncadenadasTest() {
        String nombre = "Pedro";
        String apellido = "Muñoz";
        String clave = "Pedromuñoz";
        String depositoStr = "15000.00";
        BigDecimal deposito = new BigDecimal(depositoStr);
        String retiroStr = "1560.50";
        BigDecimal retiro = new BigDecimal(retiroStr);
        BigDecimal balanceEsperado = deposito.subtract(retiro);
        Credencial credencialUsuario = new Credencial(nombre,apellido,clave);
        InteraccionesEncadenables interaccionesEncadenadas = new InteraccionesEncadenables(consoleInputStub, alkeWalletFake);
        interaccionesEncadenadas.crearUsuario(credencialUsuario)
                .logInUsuarioYAsignarCuentaCLPAUsuario(credencialUsuario)
                .logInHastaOperacionesUsuarioExistenteYConCuenta(credencialUsuario)
                .depositarEnCuenta(depositoStr)
                .retirarDeCuenta(retiroStr)
                .logOutDesdeOperaciones();
        CuentaRegular cuentaRegular = RepositorioUsuarios.consultarUsuario(nombre,apellido).get().getCuentaRegular();
        Assertions.assertEquals(balanceEsperado,cuentaRegular.getBalance());
    }

    @Test
    void transACuentaMismaMonedaTest() {
        String nombre_usuario_cuenta_origen = "Pedro";
        String apellido_usuario_cuenta_origen = "Muñoz";
        String clave_usuario_cuenta_origen  = "Pedromuñoz";
        Credencial usuarioCuentaOrigen = new Credencial(nombre_usuario_cuenta_origen,apellido_usuario_cuenta_origen,clave_usuario_cuenta_origen);

        String nombre_usuario_cuenta_destino = "Javiera";
        String apellido_usuario_cuenta_destino = "Reynoso";
        String clave_usuario_cuenta_destino = "Javierita";
        Credencial usuarioCuentaDestino = new Credencial(nombre_usuario_cuenta_destino,apellido_usuario_cuenta_destino,clave_usuario_cuenta_destino);

        String depositoStr = RandomBigDecimalValuesGenerator.generarBigDecimal(7500,20000);
        BigDecimal deposito = new BigDecimal(depositoStr);

        String tranferenciaStr = RandomBigDecimalValuesGenerator.generarBigDecimal(5000,7500);
        BigDecimal transferencia = new BigDecimal(tranferenciaStr);

         InteraccionesEncadenables creacionUsuarioDestino = new InteraccionesEncadenables(consoleInputStub, alkeWalletFake)
                .crearUsuario(usuarioCuentaDestino)
                .logInUsuarioYAsignarCuentaCLPAUsuario(usuarioCuentaDestino)
                 .logInHastaOperacionesUsuarioExistenteYConCuenta(usuarioCuentaDestino);
        String numeroCuentaDestino = alkeWalletFake.contexto.getUsuarioLogueado().getCuentaRegular().getNumeroCuenta();
        Usuario usuarioDestino = alkeWalletFake.contexto.getUsuarioLogueado();
        creacionUsuarioDestino.logOutDesdeOperaciones();

        InteraccionesEncadenables depositarYTransferirDesdeCuanteOrigen = new InteraccionesEncadenables(consoleInputStub, alkeWalletFake)
                .crearUsuario(usuarioCuentaOrigen)
                .logInUsuarioYAsignarCuentaCLPAUsuario(usuarioCuentaOrigen)
                .logInHastaOperacionesUsuarioExistenteYConCuenta(usuarioCuentaOrigen)
                .depositarEnCuenta(depositoStr)
               // .mostrarHistorial()
                .transfACuentaMismaMoneda(numeroCuentaDestino,tranferenciaStr);
               // .mostrarHistorial();
        Usuario usuarioOrigen = alkeWalletFake.contexto.getUsuarioLogueado();
        depositarYTransferirDesdeCuanteOrigen.logOutDesdeOperaciones();

        InteraccionesEncadenables controlBalanceUsuarioDestino  = new InteraccionesEncadenables(consoleInputStub,alkeWalletFake)
                .logInHastaOperacionesUsuarioExistenteYConCuenta(usuarioCuentaDestino);
                //.mostrarHistorial();
        BigDecimal montoTranferido = alkeWalletFake.contexto.getUsuarioLogueado().getCuentaRegular().getBalance();
        controlBalanceUsuarioDestino.logOutDesdeOperaciones();
        BigDecimal balanceTotal = usuarioOrigen.getCuentaRegular().getBalance().add(usuarioDestino.getCuentaRegular().getBalance());
               // .subtract(new BigDecimal("0.10"));
        Assertions.assertAll(
                ()->assertEquals(transferencia,montoTranferido,"Lo debitado de una cuenta no coincide con lo acreditado en la otra"),
                ()->assertEquals(balanceTotal,deposito,"El balance total de la operacion no cierra")
        );



    }


    @RepeatedTest(10)
    void transACuentaDifMonedaMontoEnMonedaOrigenTest() {
        BigDecimal toleranciaRedondeoAlBalancear= new BigDecimal("0.01");
        String nombre_usuario_cuenta_origen = "Pedro";
        String apellido_usuario_cuenta_origen = "Muñoz";
        String clave_usuario_cuenta_origen  = "Pedromuñoz";
        Credencial usuarioCuentaOrigen = new Credencial(nombre_usuario_cuenta_origen,apellido_usuario_cuenta_origen,clave_usuario_cuenta_origen);

        String nombre_usuario_cuenta_destino = "Javiera";
        String apellido_usuario_cuenta_destino = "Reynoso";
        String clave_usuario_cuenta_destino = "Javierita";
        Credencial usuarioCuentaDestino = new Credencial(nombre_usuario_cuenta_destino,apellido_usuario_cuenta_destino,clave_usuario_cuenta_destino);

        String depositoStr = RandomBigDecimalValuesGenerator.generarBigDecimal(7500,20000);

        String tranferenciaStr = RandomBigDecimalValuesGenerator.generarBigDecimal(5000,7500);
        BigDecimal transferencia = new BigDecimal(tranferenciaStr);

        InteraccionesEncadenables creacionUsuarioDestino = new InteraccionesEncadenables(consoleInputStub, alkeWalletFake)
                .crearUsuario(usuarioCuentaDestino)
                .logInUsuarioYAsignarCuentaCLPAUsuario(usuarioCuentaDestino)
                .logInHastaOperacionesUsuarioExistenteYConCuenta(usuarioCuentaDestino);
        String numeroCuentaDestino = alkeWalletFake.contexto.getUsuarioLogueado().getCuentaRegular().getNumeroCuenta();
        Usuario usuarioDestino = alkeWalletFake.contexto.getUsuarioLogueado();
        creacionUsuarioDestino.logOutDesdeOperaciones();

        InteraccionesEncadenables depositarYTransferirDesdeCuanteOrigen = new InteraccionesEncadenables(consoleInputStub, alkeWalletFake)
                .crearUsuario(usuarioCuentaOrigen)
                .logInUsuarioYAsignarCuentaARSAUsuario(usuarioCuentaOrigen)
                .logInHastaOperacionesUsuarioExistenteYConCuenta(usuarioCuentaOrigen)
                .depositarEnCuenta(depositoStr)
                // .mostrarHistorial()
                .transfACuentaDifMonedaMontoMonedaOrigen(numeroCuentaDestino,tranferenciaStr);
        // .mostrarHistorial();
        Usuario usuarioOrigen = alkeWalletFake.contexto.getUsuarioLogueado();
        depositarYTransferirDesdeCuanteOrigen.logOutDesdeOperaciones();

        InteraccionesEncadenables controlBalanceUsuarioDestino  = new InteraccionesEncadenables(consoleInputStub,alkeWalletFake)
                .logInHastaOperacionesUsuarioExistenteYConCuenta(usuarioCuentaDestino);
        //.mostrarHistorial();
        BigDecimal montoTranferido = alkeWalletFake.contexto.getUsuarioLogueado().getCuentaRegular().getBalance();
        controlBalanceUsuarioDestino.logOutDesdeOperaciones();

        // .subtract(new BigDecimal("0.10"));
        ConversorMoneda conversorMoneda =new ConversorMoneda();
        BigDecimal montoReconvertido = conversorMoneda.convertirMoneda(usuarioDestino.getCuentaRegular().getMonedaConvertible(),usuarioOrigen.getCuentaRegular().getMonedaConvertible(),montoTranferido);
        BigDecimal diferenciaCicloConversion = transferencia.subtract(montoReconvertido).abs();
        System.out.println("DIFERENCIA: "+diferenciaCicloConversion);
        Assertions.assertAll(
                ()->assertTrue(diferenciaCicloConversion.compareTo(toleranciaRedondeoAlBalancear)<=0,"Lo debitado de una cuenta no coincide con lo acreditado en la otra")
        );



    }

    @RepeatedTest(10)
    void transACuentaDifMonedaMontoEnMonedaDestinoTest() {
        BigDecimal toleranciaRedondeoAlBalancear= new BigDecimal("0.01");
        String nombre_usuario_cuenta_origen = "Pedro";
        String apellido_usuario_cuenta_origen = "Muñoz";
        String clave_usuario_cuenta_origen  = "Pedromuñoz";
        Credencial usuarioCuentaOrigen = new Credencial(nombre_usuario_cuenta_origen,apellido_usuario_cuenta_origen,clave_usuario_cuenta_origen);

        String nombre_usuario_cuenta_destino = "Javiera";
        String apellido_usuario_cuenta_destino = "Reynoso";
        String clave_usuario_cuenta_destino = "Javierita";
        Credencial usuarioCuentaDestino = new Credencial(nombre_usuario_cuenta_destino,apellido_usuario_cuenta_destino,clave_usuario_cuenta_destino);

        String depositoStr = RandomBigDecimalValuesGenerator.generarBigDecimal(7500,20000);
        BigDecimal deposito = new BigDecimal(depositoStr);

        String tranferenciaStr = RandomBigDecimalValuesGenerator.generarBigDecimal(5000,7500);

        InteraccionesEncadenables creacionUsuarioDestino = new InteraccionesEncadenables(consoleInputStub, alkeWalletFake)
                .crearUsuario(usuarioCuentaDestino)
                .logInUsuarioYAsignarCuentaCLPAUsuario(usuarioCuentaDestino)
                .logInHastaOperacionesUsuarioExistenteYConCuenta(usuarioCuentaDestino);
        String numeroCuentaDestino = alkeWalletFake.contexto.getUsuarioLogueado().getCuentaRegular().getNumeroCuenta();
        Usuario usuarioDestino = alkeWalletFake.contexto.getUsuarioLogueado();
        creacionUsuarioDestino.logOutDesdeOperaciones();

        InteraccionesEncadenables depositarYTransferirDesdeCuanteOrigen = new InteraccionesEncadenables(consoleInputStub, alkeWalletFake)
                .crearUsuario(usuarioCuentaOrigen)
                .logInUsuarioYAsignarCuentaARSAUsuario(usuarioCuentaOrigen)
                .logInHastaOperacionesUsuarioExistenteYConCuenta(usuarioCuentaOrigen)
                .depositarEnCuenta(depositoStr)
                 //.mostrarHistorial()
                .transfACuentaDifMonedaMontoMonedaDestino(numeroCuentaDestino,tranferenciaStr);
     // .mostrarHistorial();
        BigDecimal balancePostTransferencia = alkeWalletFake.contexto.getUsuarioLogueado().getCuentaRegular().getBalance();
        Usuario usuarioOrigen = alkeWalletFake.contexto.getUsuarioLogueado();
        depositarYTransferirDesdeCuanteOrigen.logOutDesdeOperaciones();

        InteraccionesEncadenables controlBalanceUsuarioDestino  = new InteraccionesEncadenables(consoleInputStub,alkeWalletFake)
                .logInHastaOperacionesUsuarioExistenteYConCuenta(usuarioCuentaDestino);
               // .mostrarHistorial();
        BigDecimal montoTranferido = alkeWalletFake.contexto.getUsuarioLogueado().getCuentaRegular().getBalance();
        controlBalanceUsuarioDestino.logOutDesdeOperaciones();

        // .subtract(new BigDecimal("0.10"));
        ConversorMoneda conversorMoneda =new ConversorMoneda();
        BigDecimal montoReconvertido = conversorMoneda.convertirMoneda(usuarioDestino.getCuentaRegular().getMonedaConvertible(),usuarioOrigen.getCuentaRegular().getMonedaConvertible(),montoTranferido);
        BigDecimal diferenciaCicloConversion = deposito.subtract(balancePostTransferencia.add(montoReconvertido)).abs();
        System.out.println("DIFERENCIA: "+diferenciaCicloConversion);
        Assertions.assertAll(
                ()->assertTrue(diferenciaCicloConversion.compareTo(toleranciaRedondeoAlBalancear)<=0,"Lo debitado de una cuenta no coincide con lo acreditado en la otra")
        );

    }

}
