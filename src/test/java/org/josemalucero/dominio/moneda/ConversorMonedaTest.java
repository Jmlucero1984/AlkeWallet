package org.josemalucero.dominio.moneda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ConversorMonedaTest {

    MonedaConvertible origen;
    MonedaConvertible destino;


    @Test
    void conversorMonedaTest() {
        origen = new MonedaConvertible("CLP","PesoChileno",new BigDecimal("0.0011"));
        destino = new MonedaConvertible("USD","DolarEstadounidense",new BigDecimal("1.00"));
        BigDecimal monto = new BigDecimal(1000.00);
        BigDecimal convertido = new ConversorMoneda().convertirMoneda(origen,destino,monto);
        BigDecimal valorEsperado = new BigDecimal("1.10");
        assertEquals(valorEsperado,convertido,"No coinciden los valores esperado y obtenido");


    }

    @ParameterizedTest
    @MethodSource("datosConversorMoneda")
    void conversorMonedaParametrizadoTest(MonedaConvertible origen, MonedaConvertible destino, BigDecimal monto, BigDecimal valorEsperado) {

            BigDecimal convertido = new ConversorMoneda().convertirMoneda(origen, destino, monto);

            assertEquals(valorEsperado, convertido, "No coinciden los valores esperado y obtenido");
    }


    static Stream<Arguments> datosConversorMoneda() {
        return Stream.of(
                Arguments.of(
                        new MonedaConvertible("CLP","PesoChileno", new BigDecimal("0.0011")),
                        new MonedaConvertible("USD","DolarEstadounidense", new BigDecimal("1.00")),
                        new BigDecimal("1000"),
                        new BigDecimal("1.10")
                ),
                Arguments.of(
                        new MonedaConvertible("USD","DolarEstadounidense", new BigDecimal("1.00")),
                        new MonedaConvertible("CLP","PesoChileno", new BigDecimal("0.0011")),
                        new BigDecimal("5.00"),
                        new BigDecimal("4545.45")
                ),
                Arguments.of(
                        new MonedaConvertible("CLP","PesoChileno", new BigDecimal("0.0011")),
                        new MonedaConvertible("ARS","PesoArgentino", new BigDecimal("0.00069")),
                        new BigDecimal("1000"),
                        new BigDecimal("1594.20")
                ),
                Arguments.of(
                        new MonedaConvertible("ARS","PesoArgentino", new BigDecimal("0.00069")),
                        new MonedaConvertible("CLP","PesoChileno", new BigDecimal("0.0011")),
                        new BigDecimal("594.23"),
                        new BigDecimal("372.74")
                )

        );
    }

}