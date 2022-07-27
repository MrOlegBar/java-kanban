package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ManagerCreateException;
import exception.ManagerDeleteException;
import exception.ManagerGetException;
import exception.ManagerSaveException;
import server.HttpTaskServer;
import server.KVTaskClient;
import task.EpicTask;
import task.Task;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

public class HTTPTaskManager extends FileBackedTasksManager implements Serializable {
    public KVTaskClient getKVTaskClient() {
        return kVTaskClient;
    }

    private final KVTaskClient kVTaskClient;

    public HTTPTaskManager(URI uri) throws IOException, InterruptedException {
        kVTaskClient = new KVTaskClient(uri);
    }

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateTimeAdapter())
            .create();

    public String managerToJson() {
        final List<Task> manager = new ArrayList<>();
        if (getListOfTasks().size() != 0) {
            System.out.println(getListOfTasks());
            manager.addAll(getListOfTasks());
            System.out.println(manager);
        }

        if (getListOfEpicTasks().size() != 0) {
            System.out.println(getListOfEpicTasks());
            manager.addAll(getListOfEpicTasks());
            System.out.println(manager);
        }

        if (getListOfSubTasks().size() != 0) {
            System.out.println(getListOfSubTasks());
            manager.addAll(getListOfSubTasks());
            System.out.println(manager);
        }

        try {
            if (getListOfTaskHistory().size() != 0) {
                System.out.println(getListOfTaskHistory());
                manager.addAll(getListOfTaskHistory());
                System.out.println(manager);
            }
            System.out.println(gson.toJson(manager));
        } catch (ManagerGetException e) {
            System.out.println(gson.toJson(manager));
            return gson.toJson(manager);
        }
        return gson.toJson(manager);
    }

    public static HTTPTaskManager managerFromJson(String key) throws IOException, InterruptedException {
        HTTPTaskManager hTTPTaskManager = new HTTPTaskManager(URI.create("http://localhost:8081/"));
        String managerFromGson = hTTPTaskManager.getKVTaskClient().load(key);
        System.out.println(managerFromGson);

        if (!managerFromGson.equals("")) {
            managerFromGson = managerFromGson.replaceFirst("\\[\\s*", "");
            managerFromGson = managerFromGson.replaceFirst("\\s*]", "");
            if (managerFromGson.contains("},")) {
                String[] array = managerFromGson.split("},\n\\s*");
                for (String gsonFormat : array) {
                    if (!gsonFormat.contains("}")) {
                        gsonFormat += "}";
                    }

                    if (gsonFormat.matches(".*listOfSubTaskId.*")) {
                        EpicTask epicTask = gson.fromJson(gsonFormat, EpicTask.class);
                        System.out.println(epicTask);
                        hTTPTaskManager.createTask(epicTask);
                    } else if (gsonFormat.matches(".*epicTaskId.*")) {
                        EpicTask.SubTask subTask = gson.fromJson(gsonFormat, EpicTask.SubTask.class);
                        System.out.println(subTask);
                        hTTPTaskManager.createTask(subTask);
                    } else {
                        Task task = gson.fromJson(gsonFormat, Task.class);
                        System.out.println(task);
                        hTTPTaskManager.createTask(task);
                    }
                }
            } else {
                if (managerFromGson.matches(".*listOfSubTaskId.*")) {
                    EpicTask epicTask = gson.fromJson(managerFromGson, EpicTask.class);
                    System.out.println(epicTask);
                    hTTPTaskManager.createTask(epicTask);
                } else if (managerFromGson.matches(".*epicTaskId.*")) {
                    EpicTask.SubTask subTask = gson.fromJson(managerFromGson, EpicTask.SubTask.class);
                    System.out.println(subTask);
                    hTTPTaskManager.createTask(subTask);
                } else {
                    Task task = gson.fromJson(managerFromGson, Task.class);
                    System.out.println(task);
                    hTTPTaskManager.createTask(task);
                }
            }
        }
        return hTTPTaskManager;
    }

        /**
         * Добавляет id подзадачи в список id подзадач Epic задачи, а id Epic задачи в подзадачу
         */
    @Override
    public void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask) {
        super.addSubtaskToEpicTask(subTask, epicTask);
    }

    /**
     * Метод для сохранения задач
     */
    @Override
    public void saveTask(Task task) throws InterruptedException, IOException {
        super.saveTask(task);
    }

    @Override
    public void saveEpicTask(EpicTask epicTask) {
        super.saveEpicTask(epicTask);
    }

    @Override
    public void saveSubTask(EpicTask.SubTask subTask) throws ManagerSaveException {
        super.saveSubTask(subTask);
    }

    /**
     * Получение списка всех задач
     */
    @Override
    public List<Task> getListOfTasks() throws ManagerGetException {
        return super.getListOfTasks();
    }

    @Override
    public List<EpicTask> getListOfEpicTasks() throws ManagerGetException {
        return super.getListOfEpicTasks();
    }

    @Override
    public List<EpicTask.SubTask> getListOfSubTasks() throws ManagerGetException {
        return super.getListOfSubTasks();
    }

    /**
     * Удаление всех задач
     */
    @Override
    public void deleteAllTasks() throws ManagerDeleteException {
        super.deleteAllTasks();
    }

    @Override
    public void deleteAllEpicTasks() throws ManagerDeleteException {
        super.deleteAllEpicTasks();
    }

    @Override
    public void deleteAllSubTasks() throws ManagerDeleteException {
        super.deleteAllSubTasks();
    }

    /**
     * Получение задачи по идентификатору
     */
    @Override
    public Task getTaskById(int id) throws ManagerGetException {
        return super.getTaskById(id);
    }

    @Override
    public EpicTask getEpicTaskById(int id) throws ManagerGetException {
        return super.getEpicTaskById(id);
    }

    @Override
    public EpicTask.SubTask getSubTaskById(int id) throws ManagerGetException {
        return super.getSubTaskById(id);
    }

    @Override
    public void createTaskHistoryFromString(int id) {
        super.createTaskHistoryFromString(id);
    }

    @Override
    public void createEpicTaskHistoryFromString(int id) {
        super.createEpicTaskHistoryFromString(id);
    }

    @Override
    public void createSubTaskHistoryFromString(int id) {
        super.createSubTaskHistoryFromString(id);
    }

    /**
     * Создание задачи
     */
    @Override
    public Task createTask(Task task) throws ManagerCreateException, IOException, InterruptedException {
        return super.createTask(task);
    }

    @Override
    public EpicTask createTask(EpicTask epicTask) {
        return super.createTask(epicTask);
    }

    @Override
    public EpicTask.SubTask createTask(EpicTask.SubTask subTask) throws ManagerCreateException, ManagerSaveException {
        return super.createTask(subTask);
    }

    /**
     * Обновление задачи
     */
    @Override
    public void updateTask(Task task) throws ManagerCreateException {
        super.updateTask(task);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws ManagerCreateException {
        super.updateEpicTask(epicTask);
    }

    @Override
    public void updateSubTask(EpicTask.SubTask subTask) throws ManagerCreateException {
        super.updateSubTask(subTask);
    }

    @Override
    public void createTaskFromString(Task task) throws ManagerCreateException, IOException, InterruptedException {
        super.createTaskFromString(task);
    }

    @Override
    public void createEpicTaskFromString(EpicTask epicTask) throws ManagerCreateException {
        super.createEpicTaskFromString(epicTask);
    }

    @Override
    public void createSubTaskFromString(EpicTask.SubTask subTask) throws ManagerCreateException {
        super.createSubTaskFromString(subTask);
    }

    /**
     * Удаление задачи
     */
    @Override
    public void removeTaskById(int id) throws ManagerDeleteException {
        super.removeTaskById(id);
    }

    @Override
    public void removeEpicTaskById(int id) throws ManagerDeleteException {
        super.removeEpicTaskById(id);
    }

    @Override
    public void removeSubTaskById(int id) throws ManagerDeleteException {
        super.removeSubTaskById(id);
    }

    /**
     * Получение списка всех подзадач определённого эпика
     */
    @Override
    public List<EpicTask.SubTask> getListOfSubTaskByEpicTaskId(int id) {
        return super.getListOfSubTaskByEpicTaskId(id);
    }

    /**
     * История просмотров задач
     */
    @Override
    public List<Task> getListOfTaskHistory() throws ManagerGetException {
        return super.getListOfTaskHistory();
    }

    @Override
    public void removeTaskFromTaskHistory(int id) {
        super.removeTaskFromTaskHistory(id);
    }

    /**
     * Метод для управления статусом для EpicTask задач
     */
    @Override
    public Task.Status getterEpicTaskStatus(List<Integer> listOfSubTaskId) {
        return super.getterEpicTaskStatus(listOfSubTaskId);
    }

    /**
     * Метод для управления датой, когда предполагается приступить к выполнению задачи
     */
    @Override
    public LocalDateTime getterEpicTaskStartTime(List<Integer> listOfSubTaskId) {
        return super.getterEpicTaskStartTime(listOfSubTaskId);
    }

    /**
     * Метод для управления продолжительностью(в минутах) задачи для EpicTask задач
     */
    @Override
    public long getterEpicTaskDuration(List<Integer> listOfSubTaskId) {
        return super.getterEpicTaskDuration(listOfSubTaskId);
    }

    /**
     * Метод для управления датой, когда предполагается закончить выполнение задачи
     */
    @Override
    public LocalDateTime getterEpicTaskEndTime(List<Integer> listOfSubTaskId) {
        return super.getterEpicTaskEndTime(listOfSubTaskId);
    }

    /**
     * Проверяет задачу на пересечение по времени выполнения с уже созданными задачами
     */
    @Override
    public void checkIntersectionByTaskTime(Task task) throws ManagerCreateException {
        super.checkIntersectionByTaskTime(task);
    }

    /**
     * Метод для возвращения списка задач и подзадач в заданном порядке
     */
    @Override
    public Set<Task> getterPrioritizedTasks() throws ManagerGetException {
        return super.getterPrioritizedTasks();
    }

    @Override
    public EpicTask getterEpicTaskFromRequest(String body) {
        return super.getterEpicTaskFromRequest(body);
    }

    @Override
    public EpicTask.SubTask getterSubTaskFromRequest(String body) {
        return super.getterSubTaskFromRequest(body);
    }
}
