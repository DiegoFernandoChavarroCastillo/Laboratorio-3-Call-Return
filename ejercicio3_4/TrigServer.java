import java.io.*;
import java.net.*;

/**
 * EJERCICIO 4.3.2 (Sección 4.3.2)
 * Servidor que recibe números y calcula una función trigonométrica.
 * - Por defecto calcula COSENO.
 * - Si recibe "fun:sin", "fun:cos" o "fun:tan" cambia la función activa.
 * - Enviar "bye" termina la sesión.
 * Puerto: 35002
 */
public class TrigServer {

    private static final int PORT = 35002;

    public static void main(String[] args) throws IOException {
        System.out.println("[TrigServer] Escuchando en el puerto " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("[TrigServer] Cliente: "
                        + client.getInetAddress().getHostAddress());
                handleClient(client);
            }
        }
    }

    private static void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            // Estado: función actual (por defecto coseno)
            String funcionActual = "cos";
            out.println("Función activa: cos (cambia con fun:sin | fun:cos | fun:tan)");

            String linea;
            while ((linea = in.readLine()) != null) {
                linea = linea.trim();

                if (linea.equalsIgnoreCase("bye")) {
                    out.println("Hasta luego!");
                    break;
                }

                // Cambio de función
                if (linea.toLowerCase().startsWith("fun:")) {
                    String nuevaFun = linea.substring(4).toLowerCase();
                    if (nuevaFun.equals("sin") || nuevaFun.equals("cos") || nuevaFun.equals("tan")) {
                        funcionActual = nuevaFun;
                        out.println("Función cambiada a: " + funcionActual);
                        System.out.println("[TrigServer] Función → " + funcionActual);
                    } else {
                        out.println("ERROR: función desconocida '" + nuevaFun
                                + "'. Use sin, cos o tan.");
                    }
                    continue;
                }

                // Calcular
                try {
                    double x = Double.parseDouble(linea);
                    double resultado;
                    switch (funcionActual) {
                        case "sin": resultado = Math.sin(x); break;
                        case "tan": resultado = Math.tan(x); break;
                        default:    resultado = Math.cos(x); break;
                    }
                    String respuesta = funcionActual + "(" + x + ") = " + resultado;
                    System.out.println("[TrigServer] " + respuesta);
                    out.println(respuesta);
                } catch (NumberFormatException e) {
                    out.println("ERROR: '" + linea + "' no es un número válido.");
                }
            }
        } catch (IOException e) {
            System.err.println("[TrigServer] Error: " + e.getMessage());
        }
    }
}
