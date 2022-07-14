import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exception.ManagerSaveException;
import manager.FileBackedTasksManager;
import manager.TaskManager;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * «Трекер задач»
 */
public class Main {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();
    public static void main(String[] args) throws ManagerSaveException, IOException {
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks/task?id={id}", new HelloHandler());
        httpServer.createContext("/tasks/epictask", new HelloHandler());
        httpServer.createContext("/tasks/subtask", new HelloHandler());
        httpServer.createContext("/tasks", new HelloHandler());
        httpServer.createContext("/tasks/history", new HelloHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            TaskManager manager = FileBackedTasksManager.loadFromFile(new File("Autosave.csv"));


            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            System.out.println("Началась обработка запроса " + path + " от клиента.");
            String response;
            switch (method) {
                case "GET":
                    if (path.endsWith("/tasks/task")) {
                        System.out.println(new String(String.valueOf(httpExchange.getRequestURI())).split("=")[1]);

                        try (OutputStream os = httpExchange.getResponseBody()) {
                            httpExchange.sendResponseHeaders(200, 0);
                            os.write(gson.toJson(httpExchange.getHttpContext().getAttributes()).getBytes(DEFAULT_CHARSET));
                            return;
                        }
                    } else {
                        httpExchange.sendResponseHeaders(404, 0);
                        httpExchange.close();
                    }
                    response = "Вы использовали метод GET!";
                    break;
                case "POST":
                    response = "Вы использовали метод POST!";
                    break;
                case "DELETE":
                    response = "Вы использовали метод DELETE!";
                    break;
                default:
                    response = "Вы использовали какой-то другой метод!";
            }
        }
    }
}