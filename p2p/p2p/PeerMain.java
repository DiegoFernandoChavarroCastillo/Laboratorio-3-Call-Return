package p2p;

/**
 * P2P - Parte 5: Main del peer.
 * Uso: PeerMain <peerId> <listenPort> <trackerHost>
 * Ej:  PeerMain peerA 7001 127.0.0.1
 */
public class PeerMain {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Uso: PeerMain <peerId> <listenPort> <trackerHost>");
            System.out.println("Ej:  PeerMain peerA 7001 127.0.0.1");
            return;
        }
        String peerId      = args[0];
        int    listenPort  = Integer.parseInt(args[1]);
        String trackerHost = args[2];
        TrackerClient tracker = new TrackerClient(trackerHost, 6000);
        new PeerNode(peerId, listenPort, tracker).start();
    }
}
