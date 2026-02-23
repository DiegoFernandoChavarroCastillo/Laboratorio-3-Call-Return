import java.net.*;
import java.util.Date;
import java.util.logging.*;

/**
 * EJERCICIO 5.2.1 (Sección 5.2) - Servidor de datagramas que responde la hora.
 * Escucha en el puerto 4445. Atiende solicitudes en bucle.
 */
public class DatagramTimeServerLoop {

    private static final int PORT = 4445;
    private static final Logger LOG =
            Logger.getLogger(DatagramTimeServerLoop.class.getName());

    public static void main(String[] args) {
        System.out.println("[TimeServer] Escuchando en el puerto " + PORT);

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            byte[] buf = new byte[256];

            while (true) {  // Servidor en bucle indefinido
                DatagramPacket request = new DatagramPacket(buf, buf.length);
                socket.receive(request);

                String timeStr = new Date().toString();
                byte[] data = timeStr.getBytes();

                InetAddress address = request.getAddress();
                int port = request.getPort();
                DatagramPacket response = new DatagramPacket(data, data.length, address, port);
                socket.send(response);

                System.out.println("[TimeServer] Respondí a " + address.getHostAddress()
                        + ":" + port + " → " + timeStr);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error en el servidor de tiempo", ex);
        }
    }
}
