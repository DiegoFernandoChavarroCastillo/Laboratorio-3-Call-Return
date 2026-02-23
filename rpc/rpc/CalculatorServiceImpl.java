package rpc;

/**
 * RPC - Ejercicio 6.5 (Parte 2)
 * Implementación del servicio calculadora.
 */
public class CalculatorServiceImpl implements CalculatorService {
    @Override
    public int add(int a, int b) { return a + b; }

    @Override
    public int square(int n) { return n * n; }
}
