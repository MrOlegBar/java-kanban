import server.KVServer;

import java.io.IOException;

/**
 * «Трекер задач»
 */
public class Main {
    public static void main(String[] args) throws IOException {
        KVServer kVServer = new KVServer();
        kVServer.start();
    }
}