package com.pastelaria.calculadora;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class SistemaCaixa {

    private final GerenciadorCardapio gerenciador;
    private final CalculadoraPedido   calculadoraPedido;
    private final Scanner             scanner;

    public SistemaCaixa(GerenciadorCardapio gerenciador) {
        this.gerenciador       = gerenciador;
        this.calculadoraPedido = new CalculadoraPedido();
        this.scanner           = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║        CAIXA - CANTINHO DO PASTEL        ║");
        System.out.println("╚══════════════════════════════════════════╝");

        boolean rodando = true;
        while (rodando) {
            exibirMenuPrincipal();
            String opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> novoPedido();
                case "2" -> exibirCardapio();
                case "3" -> adicionarProduto();
                case "4" -> removerProduto();
                case "0" -> {
                    System.out.println("\nSistema encerrado. Até logo!");
                    rodando = false;
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n──────────────────────────────────");
        System.out.println("  1. Novo pedido");
        System.out.println("  2. Ver cardápio");
        System.out.println("  3. Adicionar produto ao cardápio");
        System.out.println("  4. Remover produto do cardápio");
        System.out.println("  0. Sair");
        System.out.print("\nEscolha: ");
    }

    private void exibirCardapio() {
        System.out.println("\n──── CARDÁPIO ────────────────────");
        if (gerenciador.estaVazio()) {
            System.out.println("  Nenhum produto cadastrado.");
            return;
        }
        gerenciador.listarTodos().forEach(p -> System.out.println("  " + p));
        System.out.println("──────────────────────────────────");
    }

    private void novoPedido() {
        if (gerenciador.estaVazio()) {
            System.out.println("\nCardápio vazio. Cadastre produtos primeiro (opção 3).");
            return;
        }

        exibirCardapio();
        System.out.println("\nDigite o ID do produto e a quantidade.");
        System.out.println("Exemplo:  2 3  (produto 2, quantidade 3)");
        System.out.println("Digite 0 para finalizar o pedido.\n");

        Map<Integer, Integer> itensPedido = new LinkedHashMap<>();

        while (true) {
            System.out.print("  Produto (ID qtd) ou 0: ");
            String linha = scanner.nextLine().trim();

            if (linha.equals("0")) break;

            String[] partes = linha.split("\\s+");
            if (partes.length != 2) {
                System.out.println("! Formato inválido. Ex: 2 3");
                continue;
            }

            try {
                int id  = Integer.parseInt(partes[0]);
                int qtd = Integer.parseInt(partes[1]);

                if (qtd <= 0) {
                    System.out.println("! Quantidade deve ser maior que zero.");
                    continue;
                }

                Optional<Produto> produto = gerenciador.buscarPorId(id);
                if (produto.isEmpty()) {
                    System.out.println("! Produto " + id + " não encontrado no cardápio.");
                    continue;
                }

                itensPedido.merge(id, qtd, Integer::sum);
                System.out.printf("%s × %d adicionado.%n", produto.get().getNome(), qtd);

            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números. Ex: 2 3");
            }
        }

        if (itensPedido.isEmpty()) {
            System.out.println("Nenhum item adicionado. Pedido cancelado.");
            return;
        }

        double[] precos = new double[itensPedido.size()];
        int[]    qtds   = new int[itensPedido.size()];
        int idx = 0;

        System.out.println("\n──── RESUMO DO PEDIDO ────────────");
        for (Map.Entry<Integer, Integer> entry : itensPedido.entrySet()) {
            Produto p = gerenciador.buscarPorId(entry.getKey()).get();
            precos[idx] = p.getPreco();
            qtds[idx]   = entry.getValue();
            System.out.printf("  %-25s × %d = R$ %.2f%n",
                p.getNome(), entry.getValue(), p.getPreco() * entry.getValue());
            idx++;
        }

        double total = calculadoraPedido.calcularTotal(precos, qtds);
        System.out.printf("%n  TOTAL: R$ %.2f%n", total);

        System.out.print("\nAplicar desconto? (0 para não): ");
        try {
            double desconto = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            if (desconto > 0) {
                total = calculadoraPedido.aplicarDesconto(total, desconto);
                System.out.printf("  TOTAL COM %.0f%% DE DESCONTO: R$ %.2f%n", desconto, total);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Desconto ignorado: " + e.getMessage());
        }

        System.out.print("\nValor pago pelo cliente: R$ ");
        try {
            double valorPago = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            double troco     = calculadoraPedido.calcularTroco(total, valorPago);
            System.out.printf("  TROCO: R$ %.2f%n", troco);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println(" ! " + e.getMessage());
        }

        System.out.println("──────────────────────────────────");
    }

    private void adicionarProduto() {
        System.out.print("\nNome do produto: ");
        String nome = scanner.nextLine().trim();
        if (nome.isBlank()) {
            System.out.println("Nome não pode ser vazio.");
            return;
        }

        System.out.print("Preço (ex: 8.50): R$ ");
        try {
            double preco = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            Produto novo = gerenciador.adicionarProduto(nome, preco);
            System.out.println("Produto adicionado: " + novo);
        } catch (NumberFormatException e) {
            System.out.println("Preço inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("  !  " + e.getMessage());
        }
    }

    private void removerProduto() {
        exibirCardapio();
        if (gerenciador.estaVazio()) return;

        System.out.print("\nID do produto a remover: ");
        try {
            int id     = Integer.parseInt(scanner.nextLine().trim());
            boolean ok = gerenciador.removerProduto(id);
            System.out.println(ok
                ? "Produto removido com sucesso."
                : "Produto não encontrado.");
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }
}