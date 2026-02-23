package p2p;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * P2P - Parte 2: Tracker central para descubrimiento de peers.
 * Puerto: 6000
 * Comandos:
 *   REGISTER peerId port  → registra un peer
 *   LIST                  → lista todos los peers
 */
public class TrackerServer {
    private final int port;
    private final Map<String, PeerInfo> peers = new ConcurrentHashMap<>();

    public TrackerServer(int port) { this.port = port; }

    public void start() throws IOException {
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("[TRACKER] Listening on " + port);
            while (true) {
                Socket client = ss.accept();
                new Thread(() -> handle(client)).start();
            }
        }
    }

    private void handle(Socket client) {
        try (client;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(client.getOutputStream()))) {

            String line = in.readLine();
            if (line == null) return;
            String[] parts = line.trim().split("\\s+");
            String cmd = parts[0];

            if ("REGISTER".equalsIgnoreCase(cmd)) {
                if (parts.length != 3) {
                    out.write("ERR Invalid REGISTER");
                } else {
                    String peerId = parts[1];
                    int peerPort  = Integer.parseInt(parts[2]);
                    String ip     = client.getInetAddress().getHostAddress();
                    peers.put(peerId, new PeerInfo(peerId, ip, peerPort));
                    out.write("OK");
                    System.out.println("[TRACKER] Registered: " + peerId + "@" + ip + ":" + peerPort);
                }
                out.newLine(); out.flush();
                return;
            }

            if ("LIST".equalsIgnoreCase(cmd)) {
                out.write(buildList());
                out.newLine(); out.flush();
                return;
            }

            out.write("ERR Unknown command");
            out.newLine(); out.flush();

        } catch (Exception e) {
            System.out.println("[TRACKER] Error: " + e.getMessage());
        }
    }

    private String buildList() {
        StringBuilder sb = new StringBuilder("PEERS ");
        boolean first = true;
        for (PeerInfo p : peers.values()) {
            if (!first) sb.append(",");
            sb.append(p.peerId).append("@").append(p.ip).append(":").append(p.port);
            first = false;
        }
        return sb.toString();
    }

    private static class PeerInfo {
        final String peerId, ip;
        final int port;
        PeerInfo(String peerId, String ip, int port) {
            this.peerId = peerId; this.ip = ip; this.port = port;
        }
    }

    public static void main(String[] args) throws Exception {
        new TrackerServer(6000).start();
    }
}
