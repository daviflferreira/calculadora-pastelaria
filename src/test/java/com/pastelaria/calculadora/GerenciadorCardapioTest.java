package com.pastelaria.calculadora;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GerenciadorCardapio — Testes Unitários")
class GerenciadorCardapioTest {

    private static final String ARQUIVO_TEMP = "cardapio_teste.json";
    private GerenciadorCardapio gerenciador;

    @BeforeEach
    void setUp() {
        new File(ARQUIVO_TEMP).delete();
        gerenciador = new GerenciadorCardapio(ARQUIVO_TEMP);
    }

    @AfterEach
    void tearDown() {
        new File(ARQUIVO_TEMP).delete();
    }

    @Test
    @DisplayName("Adicionar: produto é salvo e recuperável")
    void adicionarProdutoSalvaCorretamente() {
        Produto p = gerenciador.adicionarProduto("Pastel de Frango", 8.50);

        assertEquals(1, p.getId());
        assertEquals("Pastel de Frango", p.getNome());
        assertEquals(8.50, p.getPreco());
        assertEquals(1, gerenciador.listarTodos().size());
    }

    @Test
    @DisplayName("Adicionar: IDs são incrementais e únicos")
    void adicionarVariosProdutosIdsIncrementais() {
        gerenciador.adicionarProduto("Pastel de Frango", 8.50);
        gerenciador.adicionarProduto("Caldo de Cana", 6.00);
        gerenciador.adicionarProduto("Suco", 5.00);

        List<Produto> lista = gerenciador.listarTodos();
        assertEquals(3, lista.size());
        assertEquals(1, lista.get(0).getId());
        assertEquals(2, lista.get(1).getId());
        assertEquals(3, lista.get(2).getId());
    }

    @Test
    @DisplayName("Adicionar: nome vazio lança IllegalArgumentException")
    void adicionarNomeVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
            () -> gerenciador.adicionarProduto("", 8.50));
    }

    @Test
    @DisplayName("Adicionar: preço negativo lança IllegalArgumentException")
    void adicionarPrecoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
            () -> gerenciador.adicionarProduto("Pastel", -1.0));
    }

    @Test
    @DisplayName("Remover: produto existente retorna true e some da lista")
    void removerProdutoExistente() {
        gerenciador.adicionarProduto("Pastel de Frango", 8.50);
        boolean resultado = gerenciador.removerProduto(1);

        assertTrue(resultado);
        assertTrue(gerenciador.estaVazio());
    }

    @Test
    @DisplayName("Remover: ID inexistente retorna false")
    void removerProdutoInexistenteRetornaFalse() {
        assertFalse(gerenciador.removerProduto(999));
    }

    @Test
    @DisplayName("Buscar: ID existente retorna o produto correto")
    void buscarPorIdExistente() {
        gerenciador.adicionarProduto("Caldo de Cana", 6.00);

        Optional<Produto> resultado = gerenciador.buscarPorId(1);
        assertTrue(resultado.isPresent());
        assertEquals("Caldo de Cana", resultado.get().getNome());
    }

    @Test
    @DisplayName("Buscar: ID inexistente retorna Optional vazio")
    void buscarPorIdInexistenteRetornaVazio() {
        Optional<Produto> resultado = gerenciador.buscarPorId(99);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Persistência: dados salvos são carregados numa nova instância")
    void persistenciaDadosSaoRecarregados() {
        gerenciador.adicionarProduto("Pastel de Queijo", 7.00);
        gerenciador.adicionarProduto("Suco de Laranja", 5.00);

        GerenciadorCardapio novaInstancia = new GerenciadorCardapio(ARQUIVO_TEMP);
        List<Produto> lista = novaInstancia.listarTodos();

        assertEquals(2, lista.size());
        assertEquals("Pastel de Queijo", lista.get(0).getNome());
        assertEquals("Suco de Laranja",  lista.get(1).getNome());
    }

    @Test
    @DisplayName("Persistência: remoção é refletida ao recarregar")
    void persistenciaRemocaoEhRefletida() {
        gerenciador.adicionarProduto("Pastel de Frango", 8.50);
        gerenciador.adicionarProduto("Caldo de Cana", 6.00);
        gerenciador.removerProduto(1);

        GerenciadorCardapio novaInstancia = new GerenciadorCardapio(ARQUIVO_TEMP);
        assertEquals(1, novaInstancia.listarTodos().size());
        assertEquals("Caldo de Cana", novaInstancia.listarTodos().get(0).getNome());
    }

    @Test
    @DisplayName("Estado: cardápio novo começa vazio")
    void cardapioNovoEstaVazio() {
        assertTrue(gerenciador.estaVazio());
    }

    @Test
    @DisplayName("Estado: deixa de ser vazio após adicionar produto")
    void cardapioNaoEstaVazioAposAdicionar() {
        gerenciador.adicionarProduto("Pastel", 8.0);
        assertFalse(gerenciador.estaVazio());
    }
}