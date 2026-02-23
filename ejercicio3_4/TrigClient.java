import java.io.*;
import java.net.*;

/**
 * EJERCICIO 4.3.2 - Cliente para TrigServer.
 */
public class TrigClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 35002;

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consola = new BufferedReader(
                     new InputStreamReader(System.in))) {

            System.out.println("Servidor: " + in.readLine());
            System.out.println("Ingrese un número o 'fun:sin'/'fun:cos'/'fun:tan' para cambiar función:");

            String entrada;
            while ((entrada = consola.readLine()) != null) {
                out.println(entrada);
                System.out.println("Servidor: " + in.readLine());
                if (entrada.equalsIgnoreCase("bye")) break;
            }
        }
    }
}
