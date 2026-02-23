import java.io.*;
import java.net.*;

/**
 * EJERCICIO 4.3.1 - Cliente para SquareServer.
 * Envía números al servidor y muestra el cuadrado retornado.
 */
public class SquareClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 35001;

    public static void main(String[] args) throws IOException {
        System.out.println("[SquareClient] Conectando a " + HOST + ":" + PORT);

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consola = new BufferedReader(
                     new InputStreamReader(System.in))) {

            System.out.println("Ingrese un número (o 'bye' para salir):");
            String entrada;
            while ((entrada = consola.readLine()) != null) {
                out.println(entrada);
                String respuesta = in.readLine();
                System.out.println("Servidor: " + respuesta);
                if (entrada.equalsIgnoreCase("bye")) break;
            }
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + e.getMessage());
        }
    }
}
