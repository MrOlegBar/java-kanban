package manager;

import epictask.EpicTask;
import task.Task;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Класс для объекта-менеджера
 */
public class Manager {

    private static int id = 0;

    private final TreeMap<Integer, Task> taskStorage = new TreeMap<>();

    private final TreeMap<Integer, EpicTask> epicTaskStorage = new TreeMap<>();

    private final TreeMap<Integer, EpicTask.SubTask> subTaskStorage = new TreeMap<>();
    /**
     * get и set методы
     */
    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Manager.id = id;
    }

    public TreeMap<Integer, Task> getTaskStorage() {
        return taskStorage;
    }

    public TreeMap<Integer, EpicTask> getEpicTaskStorage() {
        return epicTaskStorage;
    }

    public TreeMap<Integer, EpicTask.SubTask> getSubTaskStorage() {
        return subTaskStorage;
    }
    /**
     * 0. Метод для добавления SubTask в список.
     */
    public void addSubtaskToList(EpicTask.SubTask subTask, ArrayList<EpicTask.SubTask> listSubtasks){
        listSubtasks.add(subTask);
    }
    /**
     * 1. Метод для сохранения задач всех типов.
     */
    public void saveToTaskStorage(Task task) {
        taskStorage.put(task.getId(), task);
    }

    public void saveToEpicTaskStorage(EpicTask epicTask) {
        epicTaskStorage.put(epicTask.getId(), epicTask);
    }

    public void saveToSubTaskStorage(EpicTask.SubTask subTask) {
        subTaskStorage.put(subTask.getId(), subTask);
    }
    /**
     * 2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):
     *  2.1 Получение списка всех задач;
     */
    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    public ArrayList<EpicTask> getListOfEpicTasks() {
        return new ArrayList<>(epicTaskStorage.values());
    }

    public ArrayList<EpicTask.SubTask> getListOfSubTasks() {
        return new ArrayList<>(subTaskStorage.values());
    }
    /**
     *  2.2 Удаление всех задач;
     */
    public void deleteAllTasks() {
        taskStorage.clear();
    }

    public void deleteAllEpicTasks() {
        epicTaskStorage.clear();
    }

    public void deleteAllSubTasks() {
        subTaskStorage.clear();
    }
    /**
     *  2.3 Получение по идентификатору;
     */
    public Task getTaskById(int id) {
        Task returnTask = null;
        if (taskStorage.get(id) != null) {
            returnTask = taskStorage.get(id);
        }
        return returnTask;
    }

    public EpicTask getEpicTaskById(int id) {
        EpicTask returnEpicTask = null;
        if (epicTaskStorage.get(id) != null) {
            returnEpicTask = epicTaskStorage.get(id);
        }
        return returnEpicTask;
    }

    public EpicTask.SubTask getSubTaskById(int id) {
        EpicTask.SubTask returnSubTask = null;
        if (subTaskStorage.get(id) != null) {
            returnSubTask = subTaskStorage.get(id);
        }
        return returnSubTask;
    }
    /**
     *  2.4 Создание. Сам объект должен передаваться в качестве параметра;
     */
    public Task createCopyOfTask(Task task) {
        return new Task(task);
    }

    public EpicTask createCopyOfEpicTask(EpicTask epicTask) {
        return new EpicTask(epicTask);
    }

    public EpicTask.SubTask createCopyOfSubTask(EpicTask.SubTask subTask) {
        return new EpicTask.SubTask(subTask);
    }
    /**
     *  2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра;
     */
    public void updateTask(int id, Task task) {
        taskStorage.put(id, task);
    }

    public void updateEpicTask(int id, EpicTask epicTask) {
        epicTaskStorage.put(id, epicTask);
    }

    public void updateSubTask(int id, EpicTask.SubTask subTask) {
        subTaskStorage.put(id, subTask);
    }
    /**
     *  2.6 Удаление по идентификатору.
     */
    public void removeTaskById(int id) {
        for (Integer idTask : taskStorage.keySet()) {
            if (id == idTask) {
                taskStorage.remove(id);
                break;
            }
        }
    }

    public void removeEpicTaskById(int id) {
        for (Integer idEpicTask : epicTaskStorage.keySet()) {
            if (id == idEpicTask) {
                epicTaskStorage.remove(id);
                break;
            }
        }
    }

    public void removeSubTaskById(int id) {
        for (Integer idSubTask : subTaskStorage.keySet()) {
            if (id == idSubTask) {
                subTaskStorage.remove(id);
                break;
            }
        }
    }
    /**
     * 3. Дополнительные методы:
     *  3.1 Получение списка всех подзадач определённого эпика.
     */
    public ArrayList<EpicTask.SubTask> getListOfSubTaskByEpicTask(int id) {
        ArrayList<EpicTask.SubTask> listSubtasks = null;
        for (Integer idEpicTask : epicTaskStorage.keySet()) {
            if (id == idEpicTask) {
                listSubtasks = epicTaskStorage.get(id).getSubTasks();
            }
        }
        return listSubtasks;
    }

    public enum Status {
        NEW,
        DONE,
        IN_PROGRESS
    }
}