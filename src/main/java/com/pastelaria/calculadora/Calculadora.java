package com.pastelaria.calculadora;

public class Calculadora {

    public double somar(double a, double b) {
        return a + b;
    }

    public double subtrair (double a, double b) {
        return a - b;
    }

    public double multiplicar (double a, double b) {
        return a * b;
    }

    public double dividir (double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Divisão por zero não é permitido.");
        }
        return a / b;
    }
}