package rpc;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * RPC - Ejercicio 6.5 (Parte 5)
 * Stub de cliente: las llamadas parecen locales, pero van por red.
 */
public class CalculatorClientStub implements CalculatorService {
    private final String host;
    private final int port;

    public CalculatorClientStub(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public int add(int a, int b) {
        String id = UUID.randomUUID().toString();
        return parseResultOrThrow(id,
                send("id=" + id + ";method=add;params=" + a + "," + b));
    }

    @Override
    public int square(int n) {
        String id = UUID.randomUUID().toString();
        return parseResultOrThrow(id,
                send("id=" + id + ";method=square;params=" + n));
    }

    private String send(String request) {
        try (Socket socket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {
            out.write(request);
            out.newLine();
            out.flush();
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("RPC connection failed: " + e.getMessage(), e);
        }
    }

    private int parseResultOrThrow(String id, String responseLine) {
        if (responseLine == null) throw new RuntimeException("Empty response");
        var resp = RpcProtocol.parseLine(responseLine);
        if (!id.equals(resp.get("id")))
            throw new RuntimeException("Mismatched response id");
        if (!"true".equalsIgnoreCase(resp.get("ok")))
            throw new RuntimeException("RPC error: " + resp.getOrDefault("error", "unknown"));
        return Integer.parseInt(resp.get("result"));
    }
}
