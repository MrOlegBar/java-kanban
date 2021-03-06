package server;

import com.google.gson.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exception.ManagerCreateException;
import exception.ManagerDeleteException;
import exception.ManagerGetException;
import exception.ManagerSaveException;
import manager.FileBackedTasksManager;
import manager.HTTPTaskManager;
import manager.Managers;
import manager.TaskManager;
import task.EpicTask;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new HTTPTaskManager.LocalDateTimeAdapter())
            .create();

    private final HttpServer httpServer;


    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new HelloHandler());
        httpServer.createContext("/tasks/epictask", new HelloHandler());
        httpServer.createContext("/tasks/subtask", new HelloHandler());
        httpServer.createContext("/tasks/subtask/epictask", new HelloHandler());
        httpServer.createContext("/tasks/history", new HelloHandler());
        httpServer.createContext("/tasks", new HelloHandler());
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void start() {
        httpServer.start();
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException, ManagerCreateException, NullPointerException {
            Headers requestHeaders = httpExchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("Content-type");
            String method = httpExchange.getRequestMethod();
            String path = String.valueOf(httpExchange.getRequestURI()); //tasks/task?key=key1
            String key = path.split("=")[1];
            if (key.contains("&")) {
                key = key.replaceFirst("&.*$", ""); //tasks/task?key={key}&id=
            }
            System.out.println(httpExchange.getRequestURI());
            System.out.println("Началась обработка запроса " + path + " от клиента.");
            String response;
            String body;

            TaskManager manager = Managers.getDefaultManager(key);

            if ((contentTypeValues != null) && (contentTypeValues.contains("application/json"))) {
                switch (method) {
                    case "GET":
                        if (path.endsWith("/tasks/task?key=" + key)) {

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
                        } else if (path.endsWith("/tasks/epictask?key=" + key)) {

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
                        }  else if (path.endsWith("/tasks/subtask?key=" + key)) {

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
                        } else if (path.startsWith("/tasks/task?key=" + key + "&id=")) {

                            int id = Integer.parseInt(path.split("=")[2]);
                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getTaskById(id));
                                    manager.getKVTaskClient().put(key, manager.managerToJson());
                                } catch (ManagerGetException | InterruptedException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.startsWith("/tasks/epictask?key=" + key + "&id=")) {

                            int id = Integer.parseInt(path.split("=")[2]);
                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getEpicTaskById(id));
                                    manager.getKVTaskClient().put(key, manager.managerToJson());
                                } catch (ManagerGetException | InterruptedException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.startsWith("/tasks/subtask?key=" + key + "&id=")) {

                            int id = Integer.parseInt(path.split("=")[2]);
                            try (OutputStream os = httpExchange.getResponseBody()) {

                                try {
                                    response = gson.toJson(manager.getSubTaskById(id));
                                    manager.getKVTaskClient().put(key, manager.managerToJson());
                                } catch (ManagerGetException | InterruptedException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.startsWith("/tasks/subtask/epictask?key=" + key + "&id=")) {

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
                                } catch (ManagerGetException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }

                                httpExchange.sendResponseHeaders(200, 0);
                                os.write(response.getBytes(DEFAULT_CHARSET));
                                return;
                            }
                        } else if (path.endsWith("/tasks?key=" + key)) {

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
                        } else if (path.endsWith("/tasks/history?key=" + key)) {

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
                        if (path.endsWith("/tasks/task?key=" + key)) {
                            try (InputStream is = httpExchange.getRequestBody()) {
                                body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                            }

                            Task task = gson.fromJson(body, Task.class);

                            try {
                                manager.createTask(task);
                                manager.getKVTaskClient().put(key, manager.managerToJson());

                            } catch (ManagerCreateException | InterruptedException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(manager.getKVTaskClient().response.statusCode(), 0);
                            httpExchange.close();
                            return;
                        } else if (path.endsWith("/tasks/epictask?key=" + key)) {

                            try (InputStream is = httpExchange.getRequestBody()) {
                                body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                            }

                            EpicTask epicTask = FileBackedTasksManager.getterEpictaskFromGson(body);
                            manager.createTask(epicTask);

                            try {
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            httpExchange.sendResponseHeaders(manager.getKVTaskClient().response.statusCode(), 0);
                            httpExchange.close();
                            return;
                        } else if (path.endsWith("/tasks/subtask?key=" + key)) {

                            try (InputStream is = httpExchange.getRequestBody()) {
                                body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                            }

                            EpicTask.SubTask subTask = gson.fromJson(body, EpicTask.SubTask.class);

                            try {
                                manager.createTask(subTask);
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (ManagerCreateException | ManagerSaveException | InterruptedException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(manager.getKVTaskClient().response.statusCode(), 0);
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
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (ManagerCreateException | InterruptedException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
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
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (ManagerCreateException | InterruptedException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }
                            
                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.startsWith("/tasks/subtask?key=" + key + "&id=")) {

                            int id = Integer.parseInt(path.split("=")[2]);
                            try (InputStream is = httpExchange.getRequestBody()) {
                                body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                            }

                            EpicTask.SubTask subtaskData = gson.fromJson(body, EpicTask.SubTask.class);
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
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (ManagerCreateException | InterruptedException e) {

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
                        if (path.endsWith("/tasks/task?key=" + key)) {

                            try {
                                manager.deleteAllTasks();
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (ManagerDeleteException | InterruptedException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.endsWith("/tasks/epictask?key=" + key)) {

                            try {
                                manager.deleteAllEpicTasks();
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (ManagerDeleteException | InterruptedException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.endsWith("/tasks/subtask?key=" + key)) {

                            try {
                                manager.deleteAllSubTasks();
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (ManagerDeleteException | InterruptedException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.startsWith("/tasks/task?key=" + key + "&id=")) {

                            int id = Integer.parseInt(path.split("=")[2]);
                            try {
                                manager.removeTaskById(id);
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (ManagerDeleteException | InterruptedException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.startsWith("/tasks/epictask?key=" + key + "&id=")) {

                            int id = Integer.parseInt(path.split("=")[2]);
                            try {
                                manager.removeEpicTaskById(id);
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (ManagerDeleteException | InterruptedException e) {

                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                    response = e.getMessage();
                                    os.write(response.getBytes(DEFAULT_CHARSET));
                                }
                            }

                            httpExchange.sendResponseHeaders(201, 0);
                            httpExchange.close();
                            return;
                        } else if (path.startsWith("/tasks/subtask?key=" + key + "&id=")) {

                            int id = Integer.parseInt(path.split("=")[2]);
                            try {
                                manager.removeSubTaskById(id);
                                manager.getKVTaskClient().put(key, manager.managerToJson());
                            } catch (ManagerDeleteException | InterruptedException e) {

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
