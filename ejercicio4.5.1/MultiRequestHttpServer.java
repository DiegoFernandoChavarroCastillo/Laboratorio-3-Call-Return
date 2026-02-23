import java.io.*;
import java.net.*;
import java.nio.file.*;

/**
 * EJERCICIO 4.5.1 (Sección 4.5)
 * Servidor web que soporta múltiples solicitudes seguidas (no concurrentes).
 * Retorna archivos solicitados: páginas HTML e imágenes.
 * Sirve archivos desde el directorio "www/" relativo al directorio de ejecución.
 * Puerto: 35000
 *
 * Uso:
 *   1. Cree la carpeta www/ con archivos HTML e imágenes.
 *   2. Ejecute este servidor.
 *   3. Abra http://localhost:35000/index.html en el navegador.
 */
public class MultiRequestHttpServer {

    private static final int PORT = 35000;
    private static final File WWW_ROOT = new File("www");

    public static void main(String[] args) throws IOException {
        // Crear directorio www/ de ejemplo si no existe
        if (!WWW_ROOT.exists()) {
            WWW_ROOT.mkdirs();
            crearContenidoEjemplo();
        }

        System.out.println("[WebServer] Raíz: " + WWW_ROOT.getAbsolutePath());
        System.out.println("[WebServer] Escuchando en http://localhost:" + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Múltiples solicitudes secuenciales
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    atenderSolicitud(client);
                } catch (IOException e) {
                    System.err.println("[WebServer] Error en solicitud: " + e.getMessage());
                }
            }
        }
    }

    private static void atenderSolicitud(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));
        OutputStream rawOut = client.getOutputStream();

        // Leer primera línea de la solicitud HTTP
        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isBlank()) return;
        System.out.println("[WebServer] " + requestLine);

        // Consumir headers
        while (true) {
            String header = in.readLine();
            if (header == null || header.isBlank()) break;
        }

        // Parsear: GET /path HTTP/1.1
        String[] parts = requestLine.split(" ");
        if (parts.length < 2) {
            sendError(rawOut, 400, "Bad Request");
            return;
        }
        String method = parts[0];
        String path = parts[1];

        // Solo soportamos GET
        if (!method.equalsIgnoreCase("GET")) {
            sendError(rawOut, 405, "Method Not Allowed");
            return;
        }

        // Resolver ruta a archivo
        if (path.equals("/")) path = "/index.html";

        // Eliminar query string
        int queryIdx = path.indexOf('?');
        if (queryIdx >= 0) path = path.substring(0, queryIdx);

        File file = new File(WWW_ROOT, path).getCanonicalFile();

        // Seguridad: no salir del directorio raíz
        if (!file.getPath().startsWith(WWW_ROOT.getCanonicalPath())) {
            sendError(rawOut, 403, "Forbidden");
            return;
        }

        if (!file.exists() || !file.isFile()) {
            sendError(rawOut, 404, "Not Found: " + path);
            return;
        }

        // Detectar tipo de contenido
        String contentType = detectContentType(file.getName());
        byte[] body = Files.readAllBytes(file.toPath());

        // Enviar respuesta
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(rawOut));
        pw.print("HTTP/1.1 200 OK\r\n");
        pw.print("Content-Type: " + contentType + "\r\n");
        pw.print("Content-Length: " + body.length + "\r\n");
        pw.print("Connection: close\r\n");
        pw.print("\r\n");
        pw.flush();
        rawOut.write(body);
        rawOut.flush();

        System.out.println("[WebServer] 200 OK → " + file.getName() + " (" + body.length + " bytes)");
    }

    private static void sendError(OutputStream out, int code, String message) throws IOException {
        String body = "<html><body><h1>" + code + " " + message + "</h1></body></html>";
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
        pw.print("HTTP/1.1 " + code + " " + message + "\r\n");
        pw.print("Content-Type: text/html\r\n");
        pw.print("Content-Length: " + body.length() + "\r\n");
        pw.print("Connection: close\r\n");
        pw.print("\r\n");
        pw.print(body);
        pw.flush();
        System.out.println("[WebServer] " + code + " " + message);
    }

    private static String detectContentType(String filename) {
        String f = filename.toLowerCase();
        if (f.endsWith(".html") || f.endsWith(".htm")) return "text/html; charset=UTF-8";
        if (f.endsWith(".css"))  return "text/css";
        if (f.endsWith(".js"))   return "application/javascript";
        if (f.endsWith(".png"))  return "image/png";
        if (f.endsWith(".jpg") || f.endsWith(".jpeg")) return "image/jpeg";
        if (f.endsWith(".gif"))  return "image/gif";
        if (f.endsWith(".ico"))  return "image/x-icon";
        if (f.endsWith(".txt"))  return "text/plain";
        return "application/octet-stream";
    }

    private static void crearContenidoEjemplo() throws IOException {
        String html = "<!DOCTYPE html>\n<html lang='es'>\n<head>\n"
                + "  <meta charset='UTF-8'>\n"
                + "  <title>Mi Servidor Web Java</title>\n"
                + "  <style>body{font-family:sans-serif;max-width:600px;margin:40px auto;}\n"
                + "         h1{color:#2c5f8a;}</style>\n"
                + "</head>\n<body>\n"
                + "  <h1>Servidor Web en Java</h1>\n"
                + "  <p>Este servidor fue creado como parte del taller de redes.</p>\n"
                + "  <p>Soporta múltiples solicitudes y diferentes tipos de archivo.</p>\n"
                + "  <ul>\n"
                + "    <li><a href='/about.html'>Acerca de</a></li>\n"
                + "  </ul>\n"
                + "</body>\n</html>";

        String about = "<!DOCTYPE html>\n<html><head><meta charset='UTF-8'>"
                + "<title>Acerca de</title></head>"
                + "<body><h1>Acerca de</h1><p>Ejercicio 4.5.1 - Redes Java.</p>"
                + "<a href='/index.html'>← Inicio</a></body></html>";

        Files.writeString(new File(WWW_ROOT, "index.html").toPath(), html);
        Files.writeString(new File(WWW_ROOT, "about.html").toPath(), about);
        System.out.println("[WebServer] Contenido de ejemplo creado en www/");
    }
}
