package com.pastelaria.calculadora;

public class CalculadoraPedido {
    private final Calculadora calculadora;

    public CalculadoraPedido() {
        this.calculadora = new Calculadora();
    }

    public double calcularTotal(double[] precos, int[] quantidades) {
        if (precos == null || quantidades == null) {
            throw new IllegalArgumentException("Preços e quantidades não podem ser vazios.");
        }
        if (precos.length == 0 || quantidades.length == 0) {
            throw new IllegalArgumentException("Pedido deve ter pelo menos 1 item.");
        }
        if (precos.length != quantidades.length) {
            throw new IllegalArgumentException("Array de preços e quantidade devem ter o mesmo tamanho.");
        }

        double total = 0;
        for (int i = 0; i < precos.length; i++) {
            total = calculadora.somar(total, calculadora.multiplicar(precos[i], quantidades[i]));
        }
        return total;
    }

    public double aplicarDesconto(double total, double percentual) {
        if (percentual < 0 || percentual > 100) {
            throw new IllegalArgumentException("% de desconto deve estar entre 0 e 100.");
        }
        if (total < 0) {
            throw new IllegalArgumentException("Total do pedido não pode ser negativo.");
        }

        double desconto = calculadora.multiplicar(total, calculadora.dividir(percentual, 100));
        return calculadora.subtrair(total, desconto);
    }

    public double calcularTroco (double total, double valorPago) {
        if (valorPago < total) {
            throw new IllegalArgumentException(String.format("Valor pago (R$ %.2f) é insuficiente para pagar o total (R$ %.2f).", valorPago, total));
        }
        return calculadora.subtrair(valorPago, total);
    }    
}
