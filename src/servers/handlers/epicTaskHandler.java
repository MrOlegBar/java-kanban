package servers.handlers;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ManagerCreateException;
import exceptions.ManagerDeleteException;
import managers.FileBackedTasksManager;
import managers.Managers;
import managers.TaskManager;
import tasks.EpicTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class epicTaskHandler implements HttpHandler {
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
                    if (path.endsWith("/tasks/epictask?key=" + key)) {

                        try (OutputStream os = httpExchange.getResponseBody()) {

                            response = gson.toJson(manager.getListOfEpicTasks());

                            httpExchange.sendResponseHeaders(200, 0);
                            os.write(response.getBytes(DEFAULT_CHARSET));
                            return;
                        }
                    } else if (path.startsWith("/tasks/epictask?key=" + key + "&id=")) {

                        int id = Integer.parseInt(path.split("=")[2]);
                        try (OutputStream os = httpExchange.getResponseBody()) {

                            try {
                                response = gson.toJson(manager.getEpicTaskById(id));
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
                    if (path.endsWith("/tasks/epictask?key=" + key)) {

                        try (InputStream is = httpExchange.getRequestBody()) {
                            body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                        }

                        EpicTask epicTask = FileBackedTasksManager.getterEpictaskFromGson(body);
                        manager.createTask(epicTask);
                        try {
                            statusCode = manager.toJson(key);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        httpExchange.sendResponseHeaders(statusCode, 0);
                        httpExchange.close();
                        return;
                    } else if (path.startsWith("/tasks/epictask?key=" + key + "&id=")) {

                        int id = Integer.parseInt(path.split("=")[2]);
                        try (InputStream is = httpExchange.getRequestBody()) {
                            body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                        }

                        EpicTask epictaskData = gson.fromJson(body, EpicTask.class);
                        EpicTask epicTask = new EpicTask(
                                id
                                , epictaskData.getName()
                                , epictaskData.getDescription()
                                , epictaskData.getListOfSubTaskId()
                                , epictaskData.getStatus()
                                , epictaskData.getStartTime()
                                , epictaskData.getDuration());
                        try {
                            manager.updateEpicTask(epicTask);
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
                    if (path.endsWith("/tasks/epictask?key=" + key)) {

                        try {
                            manager.deleteAllEpicTasks();
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
                    } else if (path.startsWith("/tasks/epictask?key=" + key + "&id=")) {

                        int id = Integer.parseInt(path.split("=")[2]);
                        try {
                            manager.removeEpicTaskById(id);
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