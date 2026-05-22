package com.pastelaria.calculadora;

public class Main {
    public static void main(String[] args) {
        GerenciadorCardapio gerenciador = new GerenciadorCardapio("produtos.json");
        SistemaCaixa caixa = new SistemaCaixa(gerenciador);
        caixa.iniciar();
    }
}
