package com.pastelaria.calculadora;

public class Produto {
    private int id;
    private String nome;
    private double preco;

    public Produto() {}

    public Produto(int id, String nome, double preco) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public int getId() { return id; }
    public String getNome()  { return nome; }
    public double getPreco() { return preco; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(double preco) { this.preco = preco; }

    @Override
    public String toString() {
        return String.format("[%d] %-25s R$ %.2f", id, nome, preco);
    }
}
