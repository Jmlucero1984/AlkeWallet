package org.josemalucero.dominio.usuario;

import org.josemalucero.dominio.cuenta.CuentaRegular;
import org.josemalucero.dominio.operacion.OperacionTransferencia;
import org.josemalucero.servicio.providers.ConsoleOutputProvider;
import org.josemalucero.servicio.repositorios.RepositorioMonedas;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TransferenciaTest {
    private Usuario usuario_origen;
    private Usuario usuario_destino;
    private ConsoleOutputProvider consoleOutputProvider;
    @InjectMocks
    private OperacionTransferencia operacionTransferenciaMock;




    @BeforeEach
    void setUp() {
        consoleOutputProvider = new ConsoleOutputProvider();
        RepositorioMonedas.crearMonedasBasicas();
        usuario_origen = new Usuario("Nombre_a","Apellido_a", "clave_nombre_a"/*RandomStringGenerators.getRandomString(8)*/);
        usuario_destino = new Usuario("Nombre_b","Apellido_b", "clave_nombre_b"/*RandomStringGenerators.getRandomString(8)*/);
        usuario_origen.crearCuentRegular();
        usuario_destino.crearCuentRegular();
        usuario_origen.getCuentaRegular().setMoneda(RepositorioMonedas.encontrarMonedaPorCodigo("ARS"));
        usuario_destino.getCuentaRegular().setMoneda(RepositorioMonedas.encontrarMonedaPorCodigo("ARS"));

    }
    @AfterEach
    void tearDown(){
        RepositorioMonedas.clearMonedasDB();
    }


    @RepeatedTest(10)
    void transferenciaConFondosSuficientes() {
        Random random = new Random();
        BigDecimal cantidadInicialUsuario_a = new BigDecimal(""+random.nextInt(5000,10000)+"."+ random.nextInt(0,99));
        BigDecimal cantidadATransferir = new BigDecimal(""+random.nextInt(0,2000)+"."+ random.nextInt(0,99));
        BigDecimal balanceCuentaUsuario_b = usuario_destino.getCuentaRegular().getBalance();
        usuario_origen.getCuentaRegular().depositar(cantidadInicialUsuario_a);
        OperacionTransferencia operacionTransferencia = new OperacionTransferencia(usuario_origen.getCuentaRegular(),usuario_destino.getCuentaRegular(),cantidadATransferir,consoleOutputProvider);
        operacionTransferencia.ejecutar();
        assertAll(

                () -> assertEquals(usuario_origen.getCuentaRegular().getBalance(),cantidadInicialUsuario_a.subtract(cantidadATransferir),"Saldo incorrecto en cuenta de origen"),
                () -> assertEquals(usuario_destino.getCuentaRegular().getBalance(),balanceCuentaUsuario_b.add(cantidadATransferir),"Saldo incorrecto en cuenta de destino" )
        );

        }



    @Test
    void validarTransferenciaConFondosInsuficientes() {
        BigDecimal cantidadATransferir = new BigDecimal("1000.00");
        OperacionTransferencia operacionTransferencia = new OperacionTransferencia(usuario_origen.getCuentaRegular(),usuario_destino.getCuentaRegular(),cantidadATransferir,consoleOutputProvider);
        assertFalse(operacionTransferencia.preValidar());
    }

    @Test
    void validarTransferenciaPorCantidadIgualACero() {
        BigDecimal cantidadATransferir = new BigDecimal("0.00");
        OperacionTransferencia operacionTransferencia = new OperacionTransferencia(usuario_origen.getCuentaRegular(),usuario_destino.getCuentaRegular(),cantidadATransferir,consoleOutputProvider);
        assertFalse(operacionTransferencia.preValidar());
    }

    @Test
    void simularErrorEnBalances() {
        CuentaRegular cuentaRegular_a = Mockito.mock(CuentaRegular.class);
        CuentaRegular cuentaRegular_b = Mockito.mock(CuentaRegular.class);
        cuentaRegular_a.depositar(new BigDecimal("1000.00"));

        when(cuentaRegular_a.getBalance())
                .thenReturn(new BigDecimal("1000.00"))
                .thenReturn(new BigDecimal("800.00")); // antes

        when(cuentaRegular_b.getBalance())
                .thenReturn(new BigDecimal("0.00"))
                .thenReturn(new BigDecimal("150.00"));

        operacionTransferenciaMock = new OperacionTransferencia(
                cuentaRegular_a,
                cuentaRegular_b,
                        new BigDecimal("200.00"),consoleOutputProvider
                );

        operacionTransferenciaMock.ejecutar();
        assertFalse(operacionTransferenciaMock.postValidar());
    }

    @Test
    void simularErrorEnBalanceOrigenDisminucionYRestaurar(){
        BigDecimal cantidadInicialCuenta_a = new BigDecimal(1000.00);
        BigDecimal cantidadInicialCuenta_b = new BigDecimal(750.00);
        BigDecimal cantidadATransferir = new BigDecimal(1000.00);
        usuario_origen.getCuentaRegular().depositar(cantidadInicialCuenta_a);
        usuario_destino.getCuentaRegular().depositar(cantidadInicialCuenta_b);
        OperacionTransferencia operacionTransferencia = new OperacionTransferencia(usuario_origen.getCuentaRegular(),usuario_destino.getCuentaRegular(),cantidadATransferir,consoleOutputProvider);
        operacionTransferencia.ejecutar();
        usuario_origen.getCuentaRegular().retirar(new BigDecimal(100.00));
        boolean resultadoValidacion = operacionTransferencia.postValidar();
        operacionTransferencia.restaurarEstadoAnterior();
        assertAll(
                () -> assertFalse(resultadoValidacion),
                () -> assertEquals(usuario_origen.getCuentaRegular().getBalance(),cantidadInicialCuenta_a,"Saldo incorrecto en cuenta de origen"),
                () -> assertEquals(usuario_destino.getCuentaRegular().getBalance(),cantidadInicialCuenta_b,"Saldo incorrecto en cuenta de destino" )
        );

    }
    @Test
    void simularErrorEnBalanceOrigenAumentoYRestaurar(){
        BigDecimal cantidadInicialCuenta_a = new BigDecimal(1000.00);
        BigDecimal cantidadInicialCuenta_b = new BigDecimal(750.00);
        BigDecimal cantidadATransferir = new BigDecimal(1000.00);
        usuario_origen.getCuentaRegular().depositar(cantidadInicialCuenta_a);
        usuario_destino.getCuentaRegular().depositar(cantidadInicialCuenta_b);
        OperacionTransferencia operacionTransferencia = new OperacionTransferencia(usuario_origen.getCuentaRegular(),usuario_destino.getCuentaRegular(),cantidadATransferir,consoleOutputProvider);
        operacionTransferencia.ejecutar();
        usuario_origen.getCuentaRegular().depositar(new BigDecimal(100.00));
        boolean resultadoValidacion = operacionTransferencia.postValidar();
        operacionTransferencia.restaurarEstadoAnterior();
        assertAll(
                () -> assertFalse(resultadoValidacion),
                () -> assertEquals(usuario_origen.getCuentaRegular().getBalance(),cantidadInicialCuenta_a,"Saldo incorrecto en cuenta de origen"),
                () -> assertEquals(usuario_destino.getCuentaRegular().getBalance(),cantidadInicialCuenta_b,"Saldo incorrecto en cuenta de destino" )
        );

    }
    @Test
    void simularErrorEnBalanceDestinoDisminucionYRestaurar(){
        BigDecimal cantidadInicialCuenta_a = new BigDecimal(1000.00);
        BigDecimal cantidadInicialCuenta_b = new BigDecimal(750.00);
        BigDecimal cantidadATransferir = new BigDecimal(1000.00);
        usuario_origen.getCuentaRegular().depositar(cantidadInicialCuenta_a);
        usuario_destino.getCuentaRegular().depositar(cantidadInicialCuenta_b);
        OperacionTransferencia operacionTransferencia = new OperacionTransferencia(usuario_origen.getCuentaRegular(),usuario_destino.getCuentaRegular(),cantidadATransferir,consoleOutputProvider);
        operacionTransferencia.ejecutar();
        usuario_destino.getCuentaRegular().retirar(new BigDecimal(100.00));
        boolean resultadoValidacion = operacionTransferencia.postValidar();
        operacionTransferencia.restaurarEstadoAnterior();
        assertAll(
                () -> assertFalse(resultadoValidacion),
                () -> assertEquals(usuario_origen.getCuentaRegular().getBalance(),cantidadInicialCuenta_a,"Saldo incorrecto en cuenta de origen"),
                () -> assertEquals(usuario_destino.getCuentaRegular().getBalance(),cantidadInicialCuenta_b,"Saldo incorrecto en cuenta de destino" )
        );

    }
    @Test
    void simularErrorEnBalanceDestinoAumentoYRestaurar(){
        BigDecimal cantidadInicialCuenta_a = new BigDecimal(1000.00);
        BigDecimal cantidadInicialCuenta_b = new BigDecimal(750.00);
        BigDecimal cantidadATransferir = new BigDecimal(1000.00);
        usuario_origen.getCuentaRegular().depositar(cantidadInicialCuenta_a);
        usuario_destino.getCuentaRegular().depositar(cantidadInicialCuenta_b);
        OperacionTransferencia operacionTransferencia = new OperacionTransferencia(usuario_origen.getCuentaRegular(),usuario_destino.getCuentaRegular(),cantidadATransferir,consoleOutputProvider);
        operacionTransferencia.ejecutar();
        usuario_destino.getCuentaRegular().depositar(new BigDecimal(100.00));
        boolean resultadoValidacion = operacionTransferencia.postValidar();
        operacionTransferencia.restaurarEstadoAnterior();
        assertAll(
                () -> assertFalse(resultadoValidacion),
                () -> assertEquals(usuario_origen.getCuentaRegular().getBalance(),cantidadInicialCuenta_a,"Saldo incorrecto en cuenta de origen"),
                () -> assertEquals(usuario_destino.getCuentaRegular().getBalance(),cantidadInicialCuenta_b,"Saldo incorrecto en cuenta de destino" )
        );

    }

}


