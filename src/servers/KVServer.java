package servers;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {
    public static final int PORT = 8081;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> storage = new HashMap<>();

    public KVServer() throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
        System.out.println("KVServer-сервер запущен на " + PORT + " порту!");
    }

    private void register(HttpExchange httpExchange) throws IOException {
        try {
            if ("GET".equals(httpExchange.getRequestMethod())) {
                sendText(httpExchange, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void save(HttpExchange httpExchange) throws IOException {
        try {
            System.out.println("\n/save");
            if (!hasAuth(httpExchange)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                httpExchange.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(httpExchange.getRequestMethod())) {
                String key = httpExchange.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(httpExchange);
                if (value.isEmpty()) {

                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                storage.put(key, value);
                System.out.println("Значение для ключа " + key + " успешно обновлено!");
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void load(HttpExchange httpExchange) throws IOException {
        try {
            System.out.println("\n/load");
            if (!hasAuth(httpExchange)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                httpExchange.sendResponseHeaders(403, 0);
                return;
            }
            if ("GET".equals(httpExchange.getRequestMethod())) {
                String key = httpExchange.getRequestURI().getPath().substring("/load/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /load/{key}");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                if (storage.get(key) != null) {
                    String value = storage.get(key);
                    sendText(httpExchange, value);
                    if (value.isEmpty()) {
                        System.out.println("Value для возвращения пустой. Сохраните значение Value");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    System.out.println("Значение для ключа " + key + " успешно возвращено!");
                    httpExchange.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("Value для такого Key отсутствует. Укажите правильное значение Key");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
            } else {
                System.out.println("/load ждёт GET-запрос, а получил: " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    public void start() {
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange httpExchange) {
        String rawQuery = httpExchange.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] response = text.getBytes(UTF_8);
        httpExchange
                .getResponseHeaders()
                .add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.length);
        httpExchange
                .getResponseBody()
                .write(response);
    }
}