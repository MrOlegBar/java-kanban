package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exception.ManagerCreateException;
import exception.ManagerDeleteException;
import exception.ManagerGetException;
import exception.ManagerSaveException;
import manager.FileBackedTasksManager;
import task.EpicTask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8081;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static void main(String[] args) throws ManagerSaveException, IOException {

        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks/task", new HelloHandler());
        httpServer.createContext("/tasks/task?id={id}", new HelloHandler());

        httpServer.createContext("/tasks/epictask", new HelloHandler());
        httpServer.createContext("/tasks/epictask?id={id}", new HelloHandler());

        httpServer.createContext("/tasks/subtask", new HelloHandler());
        httpServer.createContext("/tasks/subtask?id={id}", new HelloHandler());

        httpServer.createContext("/tasks/subtask/epictask?id={id}", new HelloHandler());

        httpServer.createContext("/tasks/history", new HelloHandler());
        httpServer.createContext("/tasks", new HelloHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        private final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
            if (localDateTime != null) {
                jsonWriter.value(localDateTime.format(formatterWriter));
            } else {
                jsonWriter.value("null");
            }
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
        }

    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException, ManagerCreateException {
            FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("Autosave.csv"));
            Headers requestHeaders = httpExchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("Content-type");
            String method = httpExchange.getRequestMethod();
            String path = String.valueOf(httpExchange.getRequestURI());
            System.out.println(httpExchange.getRequestURI());
            System.out.println("Началась обработка запроса " + path + " от клиента.");
            String response;
            String body;

            if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                switch (method) {
                    case "GET":
                        if (path.endsWith("/tasks/task")) {

                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getListOfTasks());
                                } catch (ManagerGetException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.endsWith("/tasks/epictask")) {

                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getListOfEpicTasks());
                                } catch (ManagerGetException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        }  else if (path.endsWith("/tasks/subtask")) {

                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getListOfSubTasks());
                                } catch (ManagerGetException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.startsWith("/tasks/task?id=")) {

                            int id = Integer.parseInt(path.split("=")[1]);
                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getTaskById(id));
                                } catch (ManagerGetException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.startsWith("/tasks/epictask?id=")) {

                            int id = Integer.parseInt(path.split("=")[1]);
                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getEpicTaskById(id));
                                } catch (ManagerGetException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.startsWith("/tasks/subtask?id=")) {

                            int id = Integer.parseInt(path.split("=")[1]);
                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getSubTaskById(id));
                                } catch (ManagerGetException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.startsWith("/tasks/subtask/epictask?id=")) {

                            int id = Integer.parseInt(path.split("=")[1]);
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
                                } catch (ManagerGetException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.endsWith("/tasks")) {

                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getterPrioritizedTasks());
                                } catch (ManagerGetException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.endsWith("/tasks/history")) {

                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getListOfTaskHistory());
                                } catch (ManagerGetException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            httpExchange.close();
                        }
                        break;
                    case "POST":
                        if (path.endsWith("/tasks/task")) {

                            try (InputStream is = httpExchange.getRequestBody()) {
                                body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                            }

                            Task task = manager.getterTaskFromRequest(body);

                            try {
                                manager.createTask(task);
                            } catch (ManagerCreateException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.endsWith("/tasks/epictask")) {

                            try (InputStream is = httpExchange.getRequestBody()) {
                                body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                            }

                            EpicTask epicTask = manager.getterEpicTaskFromRequest(body);
                            manager.createTask(epicTask);

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.endsWith("/tasks/subtask")) {

                            try (InputStream is = httpExchange.getRequestBody()) {
                                body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                            }

                            EpicTask.SubTask subTask = manager.getterSubTaskFromRequest(body);

                            try {
                                manager.createTask(subTask);
                            } catch (ManagerCreateException | ManagerSaveException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.startsWith("/tasks/task?id=")) {

                            int id = Integer.parseInt(path.split("=")[1]);
                            try (InputStream is = httpExchange.getRequestBody()) {
                                body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                            }

                            Task taskData = manager.getterTaskFromRequest(body);
                            Task task = new Task(
                                    id
                                    , taskData.getName()
                                    , taskData.getDescription()
                                    , taskData.getStatus()
                                    , taskData.getStartTime()
                                    , taskData.getDuration());
                            try {
                                manager.updateTask(task);
                            } catch (ManagerCreateException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.startsWith("/tasks/epictask?id=")) {
                            
                            int id = Integer.parseInt(path.split("=")[1]);
                            try (InputStream is = httpExchange.getRequestBody()) {
                                body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                            }

                            EpicTask epictaskData = manager.getterEpicTaskFromRequest(body);
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
                            } catch (ManagerCreateException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }
                            
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.startsWith("/tasks/subtask?id=")) {

                            int id = Integer.parseInt(path.split("=")[1]);
                            try (InputStream is = httpExchange.getRequestBody()) {
                                body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                            }

                            EpicTask.SubTask subtaskData = manager.getterSubTaskFromRequest(body);
                            EpicTask.SubTask subTask = new EpicTask.SubTask(
                                    id
                                    , subtaskData.getEpicTaskId()
                                    , subtaskData.getName()
                                    , subtaskData.getDescription()
                                    , subtaskData.getStatus()
                                    , subtaskData.getStartTime()
                                    , subtaskData.getDuration());
                            try {
                                manager.updateSubTask(subTask);
                            } catch (ManagerCreateException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else {
                            httpExchange.sendResponseHeaders(404, 0);
                            httpExchange.close();
                        }
                        break;
                    case "DELETE":
                        if (path.endsWith("/tasks/task")) {

                            try {
                                manager.deleteAllTasks();
                            } catch (ManagerDeleteException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.endsWith("/tasks/epictask")) {

                            try {
                                manager.deleteAllEpicTasks();
                            } catch (ManagerDeleteException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.endsWith("/tasks/subtask")) {

                            try {
                                manager.deleteAllSubTasks();
                            } catch (ManagerDeleteException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.startsWith("/tasks/task?id=")) {

                            int id = Integer.parseInt(path.split("=")[1]);
                            try {
                                manager.removeTaskById(id);
                            } catch (ManagerDeleteException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.startsWith("/tasks/epictask?id=")) {

                            int id = Integer.parseInt(path.split("=")[1]);
                            try {
                                manager.removeEpicTaskById(id);
                            } catch (ManagerDeleteException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.startsWith("/tasks/subtask?id=")) {

                            int id = Integer.parseInt(path.split("=")[1]);
                            try {
                                manager.removeSubTaskById(id);
                            } catch (ManagerDeleteException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
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
}
