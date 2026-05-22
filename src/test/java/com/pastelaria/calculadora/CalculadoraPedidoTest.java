package com.pastelaria.calculadora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CalculadoraPedido — Testes Unitários")
class CalculadoraPedidoTest {

    private CalculadoraPedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new CalculadoraPedido();
    }

    @Test
    @DisplayName("Total: pedido com um item")
    void totalUmItem() {
        assertEquals(8.0, pedido.calcularTotal(new double[]{8.0}, new int[]{1}));
    }

    @Test
    @DisplayName("Total: pedido com múltiplos itens")
    void totalMultiplosItens() {
        double[] precos = {8.0, 6.0, 5.0};
        int[] qtds      = {2,   1,   3  };
        assertEquals(37.0, pedido.calcularTotal(precos, qtds));
    }

    @Test
    @DisplayName("Total: arrays nulos lançam IllegalArgumentException")
    void totalArrayNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
            () -> pedido.calcularTotal(null, new int[]{1}));
    }

    @Test
    @DisplayName("Total: arrays vazios lançam IllegalArgumentException")
    void totalArrayVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
            () -> pedido.calcularTotal(new double[]{}, new int[]{}));
    }

    @Test
    @DisplayName("Total: arrays de tamanhos diferentes lançam IllegalArgumentException")
    void totalArraysTamanhosDiferentesLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
            () -> pedido.calcularTotal(new double[]{8.0, 5.0}, new int[]{1}));
    }

    @Test
    @DisplayName("Desconto: 10% sobre R$ 40,00 retorna R$ 36,00")
    void desconto10Porcento() {
        assertEquals(36.0, pedido.aplicarDesconto(40.0, 10.0));
    }

    @Test
    @DisplayName("Desconto: 0% não altera o valor")
    void descontoZeroPorCento() {
        assertEquals(50.0, pedido.aplicarDesconto(50.0, 0.0));
    }

    @Test
    @DisplayName("Desconto: 100% resulta em zero")
    void desconto100Porcento() {
        assertEquals(0.0, pedido.aplicarDesconto(50.0, 100.0));
    }

    @Test
    @DisplayName("Desconto: percentual negativo lança IllegalArgumentException")
    void descontoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
            () -> pedido.aplicarDesconto(50.0, -5.0));
    }

    @Test
    @DisplayName("Desconto: percentual acima de 100 lança IllegalArgumentException")
    void descontoAcima100LancaExcecao() {
        assertThrows(IllegalArgumentException.class,
            () -> pedido.aplicarDesconto(50.0, 110.0));
    }

    @Test
    @DisplayName("Troco: pagamento exato retorna zero")
    void trocoPagamentoExato() {
        assertEquals(0.0, pedido.calcularTroco(37.0, 37.0));
    }

    @Test
    @DisplayName("Troco: pagamento maior retorna a diferença correta")
    void trocoPagamentoMaior() {
        assertEquals(13.0, pedido.calcularTroco(37.0, 50.0));
    }

    @Test
    @DisplayName("Troco: pagamento insuficiente lança IllegalArgumentException")
    void trocoPagamentoInsuficienteLancaExcecao() {
        IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> pedido.calcularTroco(37.0, 20.0)
        );
        assertTrue(excecao.getMessage().contains("insuficiente"));
    }
}