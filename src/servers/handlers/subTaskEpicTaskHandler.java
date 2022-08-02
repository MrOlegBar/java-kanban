package servers.handlers;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
import managers.TaskManager;
import tasks.EpicTask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class subTaskEpicTaskHandler implements HttpHandler {
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
        int statusCode = 404;

        TaskManager manager = Managers.getDefaultManager(key);

        if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
            if ("GET".equals(method)) {
                if (path.startsWith("/tasks/subtask/epictask?key=" + key + "&id=")) {

                    int id = Integer.parseInt(path.split("=")[2]);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        List<EpicTask.SubTask> listOfSubtask = new ArrayList<>();

                        try {
                            for (Integer subTaskId : manager.getEpicTaskById(id).getListOfSubTaskId()) {
                                for (EpicTask.SubTask subTask : manager.getListOfSubTasks()) {
                                    if (subTaskId == subTask.getId()) {
                                        listOfSubtask.add(subTask);
                                    }
                                }
                            }
                            response = gson.toJson(listOfSubtask);
                            statusCode = manager.toJson(key);
                        } catch (InterruptedException e) {
                            httpExchange.sendResponseHeaders(404, 0);
                            response = e.getMessage();
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    httpExchange.sendResponseHeaders(404, 0);
                    httpExchange.close();
                }
            } else {
                try (OutputStream os = httpExchange.getResponseBody()) {
                    httpExchange.sendResponseHeaders(404, 0);
                    response = "Используйте следующие метод: GET";
                    os.write(response.getBytes(DEFAULT_CHARSET));
                }
            }
        }
    }
}