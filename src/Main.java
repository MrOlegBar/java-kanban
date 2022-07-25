import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

/**
 * «Трекер задач»
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kVServer = new KVServer();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        kVServer.start();
        httpTaskServer.start();
    }
}