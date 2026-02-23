package rmichat;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * EJERCICIO 6.4.1 - CHAT con RMI
 * Interface remota que define el método para recibir mensajes.
 */
public interface ChatService extends Remote {
    /**
     * Recibe un mensaje de un peer remoto.
     * @param fromPeer identificador del remitente
     * @param message  contenido del mensaje
     * @throws RemoteException en caso de error de comunicación
     */
    void receiveMessage(String fromPeer, String message) throws RemoteException;
}
