package com.pastelaria.calculadora;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║        CANTINHO DO PASTEL                ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println("  1. Terminal");
            System.out.println("  2. Interface Gráfica");
            System.out.print("\nEscolha: ");

            String opcao = scanner.nextLine().trim();
            if ("2".equals(opcao)) {
                MainFX.main(args);
            } else {
                GerenciadorCardapio gerenciador = new GerenciadorCardapio("produtos.json");
                SistemaCaixa caixa = new SistemaCaixa(gerenciador);
                caixa.iniciar();
            }
        }
    }
}
