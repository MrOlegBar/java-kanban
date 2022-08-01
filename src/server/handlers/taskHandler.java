package server.handlers;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ManagerCreateException;
import exception.ManagerDeleteException;
import manager.Managers;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class taskHandler implements HttpHandler {
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
        String body;
        int statusCode = 404;

        TaskManager manager = Managers.getDefaultManager(key);

        if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
            switch (method) {
                case "GET":
                    if (path.endsWith("/tasks/task?key=" + key)) {

                        try (OutputStream os = httpExchange.getResponseBody()) {

                            response = gson.toJson(manager.getListOfTasks());

                            httpExchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            return;
                        }
                    } else if (path.startsWith("/tasks/task?key=" + key + "&id=")) {

                        int id = Integer.parseInt(path.split("=")[2]);
                        try (OutputStream os = httpExchange.getResponseBody()) {

                            response = gson.toJson(manager.getTaskById(id));

                            try {
                                statusCode = manager.toJson(key);
                            } catch (InterruptedException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                                response = e.getMessage();
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }

                            httpExchange.sendResponseHeaders(statusCode, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            return;
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        httpExchange.close();
                    }
                    break;
                case "POST":
                    if (path.endsWith("/tasks/task?key=" + key)) {
                        try (InputStream is = httpExchange.getRequestBody()) {
                            body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                        }

                        Task task = gson.fromJson(body, Task.class);

                        try {
                            manager.createTask(task);
                            statusCode = manager.toJson(key);

                        } catch (ManagerCreateException | InterruptedException e) {

                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(404, 0);
                                response = e.getMessage();
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        httpExchange.close();
                        return;
                    } else if (path.startsWith("/tasks/task?key=" + key + "&id=")) {

                        int id = Integer.parseInt(path.split("=")[2]);
                        try (InputStream is = httpExchange.getRequestBody()) {
                            body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                        }

                        Task taskData = gson.fromJson(body, Task.class);
                        Task task = new Task(
                                id
                                , taskData.getName()
                                , taskData.getDescription()
                                , taskData.getStatus()
                                , taskData.getStartTime()
                                , taskData.getDuration());
                        try {
                            manager.updateTask(task);
                            statusCode = manager.toJson(key);
                        } catch (ManagerCreateException | InterruptedException e) {

                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(404, 0);
                                response = e.getMessage();
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        httpExchange.close();
                        return;
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        httpExchange.close();
                    }
                    break;
                case "DELETE":
                    if (path.endsWith("/tasks/task?key=" + key)) {

                        try {
                            manager.deleteAllTasks();
                            statusCode = manager.toJson(key);
                        } catch (ManagerDeleteException | InterruptedException e) {

                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(404, 0);
                                response = e.getMessage();
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        httpExchange.close();
                        return;
                    } else if (path.startsWith("/tasks/task?key=" + key + "&id=")) {

                        int id = Integer.parseInt(path.split("=")[2]);
                        try {
                            manager.removeTaskById(id);
                            statusCode = manager.toJson(key);
                        } catch (ManagerDeleteException | InterruptedException e) {

                            try (OutputStream os = httpExchange.getResponseBody()) {
                                httpExchange.sendResponseHeaders(404, 0);
                                response = e.getMessage();
                                os.write(response.getBytes(DEFAULT_CHARSET));
                            }
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        httpExchange.close();
                        return;
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        httpExchange.close();
                    }
                    break;
                default:
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(404, 0);
                        response = "Используйте следующие методы: GET | PUT | DELETE";
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
            }
        }
    }
}