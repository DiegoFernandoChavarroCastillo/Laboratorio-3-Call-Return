package rmichat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * EJERCICIO 6.4.1 - CHAT con RMI
 *
 * Cada instancia actúa simultáneamente como:
 *  - Servidor: publica un ChatService en el rmiregistry local.
 *  - Cliente: conecta al rmiregistry remoto para enviar mensajes.
 *
 * Uso:
 *   java -cp . rmichat.ChatNode <miNombre> <miPuerto> <ip_remota> <puerto_remoto> <nombre_remoto>
 *
 * Ejemplo (dos terminales en localhost):
 *   Terminal 1:  java -cp . rmichat.ChatNode Alice 24001 127.0.0.1 24002 Bob
 *   Terminal 2:  java -cp . rmichat.ChatNode Bob   24002 127.0.0.1 24001 Alice
 */
public class ChatNode extends UnicastRemoteObject implements ChatService {

    private final String myName;

    // Constructor requerido porque UnicastRemoteObject lanza RemoteException
    protected ChatNode(String myName) throws RemoteException {
        super();
        this.myName = myName;
    }

    /** Método remoto: otro peer llama este método para enviarnos un mensaje. */
    @Override
    public void receiveMessage(String fromPeer, String message) throws RemoteException {
        System.out.println("\n[" + fromPeer + " → " + myName + "] " + message);
        System.out.print("> ");
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 5) {
            System.out.println("Uso: ChatNode <miNombre> <miPuerto> <ipRemota> <puertoRemoto> <nombreRemoto>");
            System.out.println("Ej:  ChatNode Alice 24001 127.0.0.1 24002 Bob");
            return;
        }

        String myName       = args[0];
        int    myPort       = Integer.parseInt(args[1]);
        String remoteIp     = args[2];
        int    remotePort   = Integer.parseInt(args[3]);
        String remoteName   = args[4];

        // 1) Iniciar rmiregistry local y publicar nuestro objeto
        Registry myRegistry = LocateRegistry.createRegistry(myPort);
        ChatNode node = new ChatNode(myName);
        myRegistry.rebind(myName, node);
        System.out.println("[" + myName + "] Publicado en el puerto " + myPort);

        // 2) Intentar conectar al peer remoto (puede tardar si aún no arrancó)
        System.out.println("[" + myName + "] Esperando al peer " + remoteName
                + " en " + remoteIp + ":" + remotePort + "...");

        ChatService remotePeer = null;
        for (int intento = 0; intento < 30 && remotePeer == null; intento++) {
            try {
                Registry remoteRegistry = LocateRegistry.getRegistry(remoteIp, remotePort);
                remotePeer = (ChatService) remoteRegistry.lookup(remoteName);
                System.out.println("[" + myName + "] Conectado con " + remoteName + "!");
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }

        if (remotePeer == null) {
            System.err.println("No se pudo conectar con " + remoteName + " en 30 segundos.");
            return;
        }

        // 3) Bucle de consola para enviar mensajes
        BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Escribe mensajes (Enter para enviar, 'bye' para salir):");
        System.out.print("> ");

        String linea;
        while ((linea = consola.readLine()) != null) {
            if (linea.equalsIgnoreCase("bye")) {
                try { remotePeer.receiveMessage(myName, "*** " + myName + " se desconectó ***"); }
                catch (Exception ignored) {}
                break;
            }
            try {
                remotePeer.receiveMessage(myName, linea);
            } catch (RemoteException e) {
                System.out.println("[Sistema] El peer se desconectó: " + e.getMessage());
            }
            System.out.print("> ");
        }

        System.out.println("[" + myName + "] Chat finalizado.");
        System.exit(0);
    }
}
