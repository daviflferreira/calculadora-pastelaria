package com.pastelaria.calculadora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculadora — Testes Unitários")
class CalculadoraTest {

    private Calculadora calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new Calculadora();
    }

    @Test
    @DisplayName("Soma: dois positivos")
    void somaDoisPositivos() {
        assertEquals(10.0, calculadora.somar(4.0, 6.0));
    }

    @Test
    @DisplayName("Soma: positivo com negativo")
    void somaPositivoComNegativo() {
        assertEquals(1.0, calculadora.somar(4.0, -3.0));
    }

    @Test
    @DisplayName("Soma: dois negativos")
    void somaDoisNegativos() {
        assertEquals(-9.0, calculadora.somar(-4.0, -5.0));
    }

    @Test
    @DisplayName("Soma: qualquer número com zero retorna o mesmo número")
    void somaComZero() {
        assertEquals(7.0, calculadora.somar(7.0, 0.0));
    }

    @Test
    @DisplayName("Subtração: resultado positivo")
    void subtracaoResultadoPositivo() {
        assertEquals(3.0, calculadora.subtrair(8.0, 5.0));
    }

    @Test
    @DisplayName("Subtração: resultado negativo")
    void subtracaoResultadoNegativo() {
        assertEquals(-2.0, calculadora.subtrair(3.0, 5.0));
    }

    @Test
    @DisplayName("Subtração: subtrair zero mantém o valor")
    void subtracaoComZero() {
        assertEquals(5.0, calculadora.subtrair(5.0, 0.0));
    }

    @Test
    @DisplayName("Subtração: número menos ele mesmo é zero")
    void subtracaoMesmoNumero() {
        assertEquals(0.0, calculadora.subtrair(9.0, 9.0));
    }

    @Test
    @DisplayName("Multiplicação: dois positivos")
    void multiplicacaoDoisPositivos() {
        assertEquals(20.0, calculadora.multiplicar(4.0, 5.0));
    }

    @Test
    @DisplayName("Multiplicação: positivo por negativo retorna negativo")
    void multiplicacaoPositivoPorNegativo() {
        assertEquals(-12.0, calculadora.multiplicar(3.0, -4.0));
    }

    @Test
    @DisplayName("Multiplicação: qualquer número por zero retorna zero")
    void multiplicacaoPorZero() {
        assertEquals(0.0, calculadora.multiplicar(99.0, 0.0));
    }

    @Test
    @DisplayName("Multiplicação: dois negativos retorna positivo")
    void multiplicacaoDoisNegativos() {
        assertEquals(6.0, calculadora.multiplicar(-2.0, -3.0));
    }

    @Test
    @DisplayName("Divisão: dois positivos")
    void divisaoDoisPositivos() {
        assertEquals(4.0, calculadora.dividir(20.0, 5.0));
    }

    @Test
    @DisplayName("Divisão: resultado decimal")
    void divisaoResultadoDecimal() {
        assertEquals(2.5, calculadora.dividir(5.0, 2.0));
    }

    @Test
    @DisplayName("Divisão: zero dividido por qualquer número é zero")
    void divisaoZeroPorNumero() {
        assertEquals(0.0, calculadora.dividir(0.0, 5.0));
    }

    @Test
    @DisplayName("Divisão: por zero deve lançar ArithmeticException")
    void divisaoPorZeroLancaExcecao() {
        ArithmeticException excecao = assertThrows(
            ArithmeticException.class,
            () -> calculadora.dividir(10.0, 0.0)
        );
        assertEquals("Divisão por zero não é permitida.", excecao.getMessage());
    }
}