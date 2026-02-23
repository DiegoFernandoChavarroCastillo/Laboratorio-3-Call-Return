import java.net.*;
import java.util.concurrent.*;
import java.util.logging.*;

/**
 * EJERCICIO 5.2.1 (Sección 5.2)
 * Cliente que consulta la hora al servidor cada 5 segundos usando UDP.
 * - Si no recibe respuesta, mantiene la última hora recibida.
 * - Si el servidor se reinicia, vuelve a actualizarse automáticamente.
 */
public class DatagramTimeClientLoop {

    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 4445;
    private static final int TIMEOUT_MS = 2_000;  // espera máxima por respuesta
    private static final int INTERVAL_SECS = 5;

    private volatile String ultimaHora = "(sin datos aún)";

    public void start() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setSoTimeout(TIMEOUT_MS);

                // Solicitud vacía
                byte[] sendBuf = new byte[1];
                InetAddress address = InetAddress.getByName(SERVER_HOST);
                DatagramPacket request = new DatagramPacket(
                        sendBuf, sendBuf.length, address, SERVER_PORT);
                socket.send(request);

                // Esperar respuesta
                byte[] recvBuf = new byte[256];
                DatagramPacket response = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(response);

                String horaRecibida = new String(
                        response.getData(), 0, response.getLength());
                ultimaHora = horaRecibida;
                System.out.println("[Cliente] Hora actualizada: " + ultimaHora);

            } catch (SocketTimeoutException e) {
                System.out.println("[Cliente] Sin respuesta del servidor. "
                        + "Manteniendo última hora: " + ultimaHora);
            } catch (Exception e) {
                System.out.println("[Cliente] Error de red: " + e.getMessage()
                        + " | Última hora: " + ultimaHora);
            }
        }, 0, INTERVAL_SECS, TimeUnit.SECONDS);

        System.out.println("[Cliente] Consultando hora cada " + INTERVAL_SECS
                + "s. Presione Ctrl+C para salir.");

        // Mantener vivo el proceso
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdownNow));
        try {
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {}
    }

    public static void main(String[] args) {
        new DatagramTimeClientLoop().start();
    }
}
