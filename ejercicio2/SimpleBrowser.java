import java.io.*;
import java.net.*;

/**
 * EJERCICIO 2 (Sección 3.2)
 * Aplicación browser: pregunta una URL al usuario, lee el contenido
 * y lo guarda en "resultado.html".
 */
public class SimpleBrowser {
    public static void main(String[] args) {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Ingrese la URL (ej: http://example.com): ");

        String input;
        try {
            input = console.readLine();
        } catch (IOException e) {
            System.err.println("Error leyendo la consola.");
            return;
        }

        if (input == null || input.isBlank()) {
            System.err.println("URL vacía. Saliendo.");
            return;
        }

        try {
            URL url = new URL(input);

            // Abrir conexión
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (SimpleBrowser/1.0)");
            conn.setConnectTimeout(8_000);
            conn.setReadTimeout(8_000);

            int status = conn.getResponseCode();
            System.out.println("HTTP Status: " + status);

            // Detectar encoding
            String contentType = conn.getContentType();
            String charset = "UTF-8";
            if (contentType != null) {
                for (String part : contentType.split(";")) {
                    part = part.trim();
                    if (part.toLowerCase().startsWith("charset=")) {
                        charset = part.substring("charset=".length());
                    }
                }
            }

            // Leer y guardar
            File salida = new File("resultado.html");
            try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), charset));
                 PrintWriter writer = new PrintWriter(
                        new OutputStreamWriter(new FileOutputStream(salida), "UTF-8"))) {

                String linea;
                int lineas = 0;
                while ((linea = reader.readLine()) != null) {
                    writer.println(linea);
                    lineas++;
                }
                System.out.println("Guardado " + lineas + " líneas en: "
                        + salida.getAbsolutePath());
                System.out.println("Abra resultado.html en su navegador para verlo.");
            }

        } catch (MalformedURLException e) {
            System.err.println("URL mal formada: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de red/IO: " + e.getMessage());
        }
    }
}
