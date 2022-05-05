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
    public ArrayList<Task> getListOfTasks(TreeMap<Integer, Task> treeMap) {
        return new ArrayList<>(treeMap.values());
    }

    public ArrayList<EpicTask> getListOfEpicTasks(TreeMap<Integer, EpicTask> treeMap) {
        return new ArrayList<>(treeMap.values());
    }

    public ArrayList<EpicTask.SubTask> getListOfSubTasks(TreeMap<Integer, EpicTask.SubTask> treeMap) {
        return new ArrayList<>(treeMap.values());
    }
    /**
     *  2.2 Удаление всех задач;
     */
    public void deleteAllTasks(TreeMap<Integer, Task> treeMap) {
        treeMap.clear();
    }

    public void deleteAllEpicTasks(TreeMap<Integer, EpicTask> treeMap) {
        treeMap.clear();
    }

    public void deleteAllSubTasks(TreeMap<Integer, EpicTask.SubTask> treeMap) {
        treeMap.clear();
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
    public ArrayList<EpicTask.SubTask> getCompleteListOfSubTaskByEpicTask(EpicTask epicTask) {
        return epicTask.getSubTasks();
    }
    /**
     * 4. Метод для управления статусом для эпик задач.
     * @return
     */
    public static Status getEpicTaskStatus(ArrayList<EpicTask.SubTask> subTasks) {
        Status statusEpicTask;
        int countNew = 0;
        int countDone = 0;

        for (EpicTask.SubTask subTask : subTasks) {
            if (subTask.getStatus().equals(Status.NEW)) {
                countNew++;
            }
            if (!subTask.getStatus().equals(Status.DONE)) {
                countDone++;
            }
        }
/**
 * Если у эпика нет подзадач или все они имеют статус NEW | DONE, то статус должен быть NEW | DONE.
 * Во всех остальных случаях статус должен быть IN_PROGRESS.
 */
        if ((subTasks.isEmpty()) || (countNew == subTasks.size())) {
            statusEpicTask = Status.NEW;
        } else if (countDone == subTasks.size()) {
            statusEpicTask = Status.DONE;
        } else {
            statusEpicTask = Status.IN_PROGRESS;
        }
        return statusEpicTask;
    }
    public enum Status {
        NEW,
        DONE,
        IN_PROGRESS
    }
}