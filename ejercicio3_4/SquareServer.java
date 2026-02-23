import java.io.*;
import java.net.*;

/**
 * EJERCICIO 4.3.1 (Sección 4.3.1)
 * Servidor que recibe un número y responde con su cuadrado.
 * Puerto: 35001
 * 
 * Protocolo: cliente envía un número (texto), servidor responde con el cuadrado.
 * Enviar "bye" termina la sesión.
 */
public class SquareServer {

    private static final int PORT = 35001;

    public static void main(String[] args) throws IOException {
        System.out.println("[SquareServer] Escuchando en el puerto " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Acepta múltiples clientes de forma secuencial
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("[SquareServer] Cliente conectado: "
                        + client.getInetAddress().getHostAddress());
                handleClient(client);
            }
        }
    }

    private static void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            String linea;
            while ((linea = in.readLine()) != null) {
                if (linea.equalsIgnoreCase("bye")) {
                    out.println("Hasta luego!");
                    break;
                }
                try {
                    double numero = Double.parseDouble(linea.trim());
                    double cuadrado = numero * numero;
                    System.out.println("[SquareServer] " + numero + "² = " + cuadrado);
                    out.println("Cuadrado de " + numero + " = " + cuadrado);
                } catch (NumberFormatException e) {
                    out.println("ERROR: '" + linea + "' no es un número válido.");
                }
            }
        } catch (IOException e) {
            System.err.println("[SquareServer] Error con cliente: " + e.getMessage());
        }
    }
}
