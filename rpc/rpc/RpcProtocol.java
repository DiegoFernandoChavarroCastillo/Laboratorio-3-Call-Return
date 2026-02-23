package rpc;

import java.util.*;

/**
 * RPC - Ejercicio 6.5 (Parte 3)
 * Protocolo de texto simple para RPC:
 *   Request:  id=<uuid>;method=add;params=2,3
 *   Response: id=<uuid>;ok=true;result=5
 */
public final class RpcProtocol {
    private RpcProtocol() {}

    public static Map<String, String> parseLine(String line) {
        Map<String, String> map = new HashMap<>();
        for (String p : line.split(";")) {
            int eq = p.indexOf('=');
            if (eq > 0) {
                map.put(p.substring(0, eq).trim(), p.substring(eq + 1).trim());
            }
        }
        return map;
    }

    public static String buildResponse(String id, boolean ok, String result, String error) {
        if (ok) return "id=" + id + ";ok=true;result=" + result;
        return "id=" + id + ";ok=false;error=" + sanitize(error);
    }

    private static String sanitize(String s) {
        return s == null ? "" : s.replace(";", ",");
    }
}
