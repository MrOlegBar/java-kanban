package manager;

import task.EpicTask;
import task.Task;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Класс для объекта-менеджера
 */
public class Manager {
    private final TreeMap<Integer, Task> taskStorage = new TreeMap<>();
    private final TreeMap<Integer, EpicTask> epicTaskStorage = new TreeMap<>();
    private final TreeMap<Integer, EpicTask.SubTask> subTaskStorage = new TreeMap<>();
    private int id = 0;

    /**
     * Метод для добавления подзадач в список
     */
    public void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask) {
        if (epicTask.getListOfSubTaskId().contains(subTask.getId())) {
            epicTask.getListOfSubTaskId().remove(subTask.getId());
        }
        epicTask.getListOfSubTaskId().add(subTask.getId());
        subTask.setEpicTaskId(epicTask.getId());
    }

    /**
     * 4. Метод для управления статусом для EpicTask задач
     */
    public Task.Status getEpicTaskStatus(ArrayList<Integer> listOfSubTaskId) {
        Task.Status statusEpicTask;
        int countNew = 0;
        int countDone = 0;

        for (Integer id : listOfSubTaskId) {
            if (subTaskStorage.get(id).getStatus().equals(Task.Status.NEW)) {
                countNew++;
            }
            if (subTaskStorage.get(id).getStatus().equals(Task.Status.DONE)) {
                countDone++;
            }
        }

        if ((listOfSubTaskId.isEmpty()) || (countNew == listOfSubTaskId.size())) {
            statusEpicTask = Task.Status.NEW;
        } else if (countDone == listOfSubTaskId.size()) {
            statusEpicTask = Task.Status.DONE;
        } else {
            statusEpicTask = Task.Status.IN_PROGRESS;
        }
        return statusEpicTask;
    }

    /**
     * 1. Метод для сохранения задач
     */
    public void saveToTaskStorage(Task task) {
        id += 1;
        task.setId(id);
        taskStorage.put(id, task);
    }

    public void saveToEpicTaskStorage(EpicTask epicTask) {
        id += 1;
        epicTask.setId(id);
        epicTaskStorage.put(id, epicTask);
    }

    public void saveToSubTaskStorage(EpicTask.SubTask subTask) {
        id += 1;
        subTask.setId(id);
        subTaskStorage.put(id, subTask);
    }

    /**
     * 2.1 Получение списка всех задач
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
     * 2.2 Удаление всех задач
     */
    public void deleteAllTasks() {
        taskStorage.clear();
    }

    public void deleteAllEpicTasks() {
        for (int key : epicTaskStorage.keySet()) {
            epicTaskStorage.get(key).getListOfSubTaskId().clear();
        }
        epicTaskStorage.clear();
        subTaskStorage.clear();
    }

    /**
     * 2.3 Получение задачи по идентификатору
     */
    public Task getTaskById(int id) {
        return taskStorage.get(id);
    }

    public EpicTask getEpicTaskById(int id) {
        return epicTaskStorage.get(id);
    }

    public EpicTask.SubTask getSubTaskById(int id) {
        return subTaskStorage.get(id);
    }

    /**
     * 2.4 Создание задачи
     */
    public Task createCopyOfTask(Task task) {
        return new Task(task.getName(), task.getDescription(), task.getStatus());
    }

    public EpicTask createCopyOfEpicTask(EpicTask epicTask) {
        return new EpicTask(epicTask.getName(), epicTask.getDescription(), epicTask.getListOfSubTaskId(), epicTask.getStatus());
    }

    public EpicTask.SubTask createCopyOfSubTask(EpicTask.SubTask subTask) {
        return new EpicTask.SubTask(subTask.getEpicTaskId(), subTask.getName(), subTask.getDescription(), subTask.getStatus());
    }

    /**
     * 2.5 Обновление задачи
     */
    public void updateTask(Task task) {
        Task newTask = new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
        taskStorage.put(task.getId(), newTask);
    }

    public void updateEpicTask(EpicTask epicTask) {
        Task.Status epicTaskStatus = getEpicTaskStatus(epicTask.getListOfSubTaskId());
        EpicTask newEpicTask = new EpicTask(epicTask.getId(), epicTask.getName(), epicTask.getDescription()
                , epicTask.getListOfSubTaskId(), epicTaskStatus);
        epicTask.setStatus(epicTaskStatus);
        epicTaskStorage.put(epicTask.getId(), newEpicTask);
    }

    public void updateSubTask(EpicTask.SubTask subTask) {
        EpicTask.SubTask newSubTask = new EpicTask.SubTask(subTask.getId(), subTask.getEpicTaskId(), subTask.getName()
                , subTask.getDescription(), subTask.getStatus());
        subTaskStorage.put(subTask.getId(), newSubTask);

        Task.Status newStatus = getEpicTaskStatus(epicTaskStorage.get(subTask.getEpicTaskId()).getListOfSubTaskId());
        epicTaskStorage.get(subTask.getEpicTaskId()).setStatus(newStatus);
    }

    /**
     * 2.6 Удаление задачи
     */
    public void removeTaskById(int id) {
        taskStorage.remove(id);
    }

    public void removeEpicTaskById(int id) {
        for (int subTaskId : epicTaskStorage.get(id).getListOfSubTaskId()) {
            removeSubTaskById(subTaskId);
        }
        epicTaskStorage.remove(id);
    }

    public void removeSubTaskById(int id) {
        subTaskStorage.remove(id);
    }

    /**
     * 3.1 Получение списка всех подзадач определённого эпика
     */
    public ArrayList<EpicTask.SubTask> getListOfSubTaskByEpicTaskId(int id) {
        ArrayList<EpicTask.SubTask> listOfSubTaskByEpicTaskId = new ArrayList<>();
        for (int subTaskId : subTaskStorage.keySet()) {
            if (id == getSubTaskById(subTaskId).getEpicTaskId()) {
                listOfSubTaskByEpicTaskId.add(getSubTaskById(subTaskId));
            }
        }
        return listOfSubTaskByEpicTaskId;
    }
}