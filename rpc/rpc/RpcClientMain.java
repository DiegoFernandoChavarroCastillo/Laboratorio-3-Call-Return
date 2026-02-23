package rpc;

/**
 * RPC - Ejercicio 6.5 (Parte 6)
 * Main de prueba del cliente RPC.
 */
public class RpcClientMain {
    public static void main(String[] args) {
        CalculatorService calc = new CalculatorClientStub("127.0.0.1", 5000);
        System.out.println("add(2,3)   = " + calc.add(2, 3));
        System.out.println("square(9)  = " + calc.square(9));
        System.out.println("add(10,20) = " + calc.add(10, 20));
        System.out.println("square(7)  = " + calc.square(7));
    }
}
