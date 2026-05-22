package com.pastelaria.calculadora;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GerenciadorCardapio {

    private final String caminhoArquivo;
    private final Gson gson;
    private List<Produto> produtos;

    public GerenciadorCardapio(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.produtos = new ArrayList<>();
        carregarDoArquivo();
    }

    private void carregarDoArquivo() {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            return;
        }
        try (Reader reader = new FileReader(arquivo)) {
            Type tipo = new TypeToken<List<Produto>>(){}.getType();
            List<Produto> carregados = gson.fromJson(reader, tipo);
            if (carregados != null) {
                produtos = carregados;
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar cardápio: " + e.getMessage());
        }
    }

    public void salvarNoArquivo() {
        try (Writer writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(produtos, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar cardápio: " + e.getMessage());
        }
    }

    public Produto adicionarProduto(String nome, double preco) {
        int novoId = produtos.stream()
                             .mapToInt(Produto::getId)
                             .max()
                             .orElse(0) + 1;
        Produto produto = new Produto(novoId, nome, preco);
        produtos.add(produto);
        salvarNoArquivo();
        return produto;
    }

    public boolean removerProduto(int id) {
        boolean removido = produtos.removeIf(p -> p.getId() == id);
        if (removido) {
            salvarNoArquivo();
        }
        return removido;
    }

    public Optional<Produto> buscarPorId(int id) {
        return produtos.stream()
                       .filter(p -> p.getId() == id)
                       .findFirst();
    }

    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos);
    }

    public boolean estaVazio() {
        return produtos.isEmpty();
    }
}