package managers;

import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import exceptions.ManagerCreateException;
import exceptions.ManagerDeleteException;
import exceptions.ManagerSaveException;
import servers.KVTaskClient;
import tasks.EpicTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

public class HTTPTaskManager extends FileBackedTasksManager {
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
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public Integer toJson(String key) throws IOException, InterruptedException {
        final List<Task> manager = new ArrayList<>();
        if (getListOfTasks().size() != 0) {
            manager.addAll(getListOfTasks());
        }

        if (getListOfEpicTasks().size() != 0) {
            manager.addAll(getListOfEpicTasks());
        }

        if (getListOfSubTasks().size() != 0) {
            manager.addAll(getListOfSubTasks());
        }

        String taskManager = gson.toJson(manager);

        if (getListOfTaskHistory().size() != 0) {
            List<Integer> listOfIdsFromHistory = new ArrayList<>();
            List<Task> list = getListOfTaskHistory();
            for (Task task : list) {
                listOfIdsFromHistory.add(task.getId());
            }
            taskManager = taskManager.replaceFirst("\\s+]$", ",\n  {\n    \"listOfIdsFromHistory\": ");
            taskManager += gson.toJson(listOfIdsFromHistory) + "\n}\n]";
        }

        kVTaskClient.put(key, taskManager);
        return kVTaskClient.getResponse().statusCode();
    }

    public static TaskManager fromJson(String key) throws IOException {
        HTTPTaskManager hTTPTaskManager = null;
        String managerFromGson = null;

        try {
            hTTPTaskManager = new HTTPTaskManager(URI.create("http://localhost:8081/"));
            managerFromGson = hTTPTaskManager.getKVTaskClient().load(key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if ((hTTPTaskManager != null) && (managerFromGson != null) && (!managerFromGson.equals(""))) {
            managerFromGson = managerFromGson.replaceFirst("\\[\\s*", "");
            managerFromGson = managerFromGson.replaceFirst("\\s+]$", "");
            if (managerFromGson.contains("},")) {
                String[] array = managerFromGson.split("},\n\\s*");
                for (String gsonFormat : array) {
                    if (!gsonFormat.contains("}")) {
                        gsonFormat += "}";
                    }

                    if (gsonFormat.contains("listOfSubTaskId")) {
                        EpicTask epicTask = FileBackedTasksManager.getterEpictaskFromGson(gsonFormat);
                        hTTPTaskManager.createTask(epicTask);
                    } else if (gsonFormat.contains("epicTaskId")) {
                        EpicTask.SubTask subTask = gson.fromJson(gsonFormat, EpicTask.SubTask.class);
                        hTTPTaskManager.createTask(subTask);
                    } else if (gsonFormat.contains("listOfIdsFromHistory")) {
                        String ids = gsonFormat
                                .replaceFirst("\\{\\s*\"listOfIdsFromHistory\": \\[\\s*", "")
                                .replaceFirst("\\s*]\\s*}", "");

                        if (ids.contains(",")) {
                            String[] arrayOfIdsFromHistory = ids.split(",\\s*");
                            for (String id : arrayOfIdsFromHistory) {
                                if (hTTPTaskManager.getTaskById(Integer.parseInt(id)) != null) {
                                    hTTPTaskManager.getTaskById(Integer.parseInt(id));
                                } else if (hTTPTaskManager.getEpicTaskById(Integer.parseInt(id)) != null) {
                                        hTTPTaskManager.getEpicTaskById(Integer.parseInt(id));
                                } else if (hTTPTaskManager.getSubTaskById(Integer.parseInt(id)) != null) {
                                        hTTPTaskManager.getSubTaskById(Integer.parseInt(id));
                                } else {
                                    return null;
                                }
                            }
                        } else {
                            if (hTTPTaskManager.getTaskById(Integer.parseInt(ids)) != null) {
                                hTTPTaskManager.getTaskById(Integer.parseInt(ids));
                            } else if (hTTPTaskManager.getEpicTaskById(Integer.parseInt(ids)) != null) {
                                hTTPTaskManager.getEpicTaskById(Integer.parseInt(ids));
                            } else if (hTTPTaskManager.getSubTaskById(Integer.parseInt(ids)) != null) {
                                hTTPTaskManager.getSubTaskById(Integer.parseInt(ids));
                            } else {
                                return null;
                            }
                        }
                    } else {
                        Task task = gson.fromJson(gsonFormat, Task.class);
                        hTTPTaskManager.createTask(task);
                    }
                }
            } else {
                if (managerFromGson.contains("listOfSubTaskId")) {
                    EpicTask epicTask = FileBackedTasksManager.getterEpictaskFromGson(managerFromGson);
                    hTTPTaskManager.createTask(epicTask);
                } else if (managerFromGson.contains("epicTaskId")) {
                    EpicTask.SubTask subTask = gson.fromJson(managerFromGson, EpicTask.SubTask.class);
                    hTTPTaskManager.createTask(subTask);
                } else if (managerFromGson.contains("listOfIdsFromHistory")) {
                    String ids = managerFromGson
                            .replaceFirst("\\{\\s*\"listOfIdsFromHistory\": \\[\\s*", "")
                            .replaceFirst("\\s*]\\s*}", "");

                    if (ids.contains(",")) {
                        String[] arrayOfIdsFromHistory = ids.split(", ");
                        for (String id : arrayOfIdsFromHistory) {
                            if (hTTPTaskManager.getTaskById(Integer.parseInt(id)) != null) {
                                hTTPTaskManager.getTaskById(Integer.parseInt(id));
                            } else if (hTTPTaskManager.getEpicTaskById(Integer.parseInt(id)) != null) {
                                hTTPTaskManager.getEpicTaskById(Integer.parseInt(id));
                            } else if (hTTPTaskManager.getSubTaskById(Integer.parseInt(id)) != null) {
                                hTTPTaskManager.getSubTaskById(Integer.parseInt(id));
                            } else {
                                return null;
                            }
                        }
                    } else {
                        if (hTTPTaskManager.getTaskById(Integer.parseInt(ids)) != null) {
                            hTTPTaskManager.getTaskById(Integer.parseInt(ids));
                        } else if (hTTPTaskManager.getEpicTaskById(Integer.parseInt(ids)) != null) {
                            hTTPTaskManager.getEpicTaskById(Integer.parseInt(ids));
                        } else if (hTTPTaskManager.getSubTaskById(Integer.parseInt(ids)) != null) {
                            hTTPTaskManager.getSubTaskById(Integer.parseInt(ids));
                        } else {
                            return null;
                        }
                    }
                } else {
                    Task task = gson.fromJson(managerFromGson, Task.class);
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
    public void saveTask(Task task) throws IOException {
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
    public List<Task> getListOfTasks() {
        return super.getListOfTasks();
    }

    @Override
    public List<EpicTask> getListOfEpicTasks() {
        return super.getListOfEpicTasks();
    }

    @Override
    public List<EpicTask.SubTask> getListOfSubTasks() {
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
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        return super.getEpicTaskById(id);
    }

    @Override
    public EpicTask.SubTask getSubTaskById(int id) {
        return super.getSubTaskById(id);
    }

    /**
     * Создание задачи
     */
    @Override
    public Task createTask(Task task) throws ManagerCreateException, IOException {
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
    public List<Task> getListOfTaskHistory() {
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
    public Set<Task> getterPrioritizedTasks() {
        return super.getterPrioritizedTasks();
    }
}
