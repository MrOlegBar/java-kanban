package server.handlers;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class taskSHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Headers requestHeaders = httpExchange.getRequestHeaders();
        List<String> contentTypeValues = requestHeaders.get("Content-type");
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());
        String key = path.split("=")[1];
        if (key.contains("&")) {
            key = key.replaceFirst("&.*$", "");
        }
        System.out.println(httpExchange.getRequestURI());
        System.out.println("Началась обработка запроса " + path + " от клиента.");
        String response;

        TaskManager manager = Managers.getDefaultManager(key);

        if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
            if ("GET".equals(method)) {
                if (path.endsWith("/tasks?key=" + key)) {

                    try (OutputStream os = httpExchange.getResponseBody()) {

                        response = gson.toJson(manager.getterPrioritizedTasks());

                        httpExchange.sendResponseHeaders(200, 0);
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                }
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                httpExchange.sendResponseHeaders(404, 0);
                response = "Используйте следующие методы: GET | PUT | DELETE";
                os.write(response.getBytes(DEFAULT_CHARSET));
            }
        }
    }
}
