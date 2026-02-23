import java.net.MalformedURLException;
import java.net.URL;

/**
 * EJERCICIO 1 (Sección 3.1)
 * Crea un objeto URL e imprime los 8 métodos de lectura de sus componentes.
 */
public class URLInfo {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://ldbn.escuelaing.edu.co:80/publications_bib.html?query=java#section1");

            System.out.println("=== Componentes de la URL ===");
            System.out.println("URL completa : " + url.toString());
            System.out.println("getProtocol  : " + url.getProtocol());
            System.out.println("getAuthority : " + url.getAuthority());
            System.out.println("getHost      : " + url.getHost());
            System.out.println("getPort      : " + url.getPort());
            System.out.println("getPath      : " + url.getPath());
            System.out.println("getQuery     : " + url.getQuery());
            System.out.println("getFile      : " + url.getFile());
            System.out.println("getRef       : " + url.getRef());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
