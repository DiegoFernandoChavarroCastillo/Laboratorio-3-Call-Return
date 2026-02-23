package p2p;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * P2P - Parte 3: Cliente para comunicarse con el tracker.
 */
public class TrackerClient {
    private final String host;
    private final int port;

    public TrackerClient(String host, int port) {
        this.host = host; this.port = port;
    }

    public void register(String peerId, int peerPort) {
        String resp = request("REGISTER " + peerId + " " + peerPort);
        if (!resp.startsWith("OK"))
            throw new RuntimeException("Register failed: " + resp);
    }

    public Map<String, HostPort> listPeers() {
        String resp = request("LIST");
        if (!resp.startsWith("PEERS"))
            throw new RuntimeException("LIST failed: " + resp);
        String payload = resp.substring("PEERS".length()).trim();
        Map<String, HostPort> out = new HashMap<>();
        if (payload.isBlank()) return out;
        for (String e : payload.split(",")) {
            String[] a  = e.split("@");
            String[] hp = a[1].split(":");
            out.put(a[0], new HostPort(hp[0], Integer.parseInt(hp[1])));
        }
        return out;
    }

    private String request(String line) {
        try (Socket s = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(s.getOutputStream()));
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(s.getInputStream()))) {
            out.write(line); out.newLine(); out.flush();
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Tracker unreachable: " + e.getMessage(), e);
        }
    }

    public static class HostPort {
        public final String host;
        public final int port;
        public HostPort(String host, int port) { this.host = host; this.port = port; }
    }
}
