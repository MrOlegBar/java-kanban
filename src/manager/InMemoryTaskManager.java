package manager;

import task.EpicTask;
import task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Класс для объекта-менеджера
 */
public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> taskStorage = new TreeMap<>();
    private final Map<Integer, EpicTask> epicTaskStorage = new TreeMap<>();
    private final Map<Integer, EpicTask.SubTask> subTaskStorage = new TreeMap<>();
    private int id = 0;
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    /**
     * Метод для добавления подзадач в список
     */
    public void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask) {
        ArrayList<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
        int subTaskId = subTask.getId();
        int epicTaskId = epicTask.getId();

        if (listOfSubTaskId.contains(subTaskId)) {
            listOfSubTaskId.remove(subTaskId);
        }
        listOfSubTaskId.add(subTaskId);
        subTask.setEpicTaskId(epicTaskId);
    }

    /**
     * 1. Метод для сохранения задач
     */
    @Override
    public void saveTask(Task task) {
        int taskId = idGeneration(task);
        taskStorage.put(taskId, task);
    }

    @Override
    public void saveEpicTask(EpicTask epicTask) {
        int epicTaskId = idGeneration(epicTask);
        epicTaskStorage.put(epicTaskId, epicTask);
    }

    @Override
    public void saveSubTask(EpicTask.SubTask subTask) {
        int subTaskId = idGeneration(subTask);
        subTaskStorage.put(subTaskId, subTask);
    }

    /**
     * 2.1 Получение списка всех задач
     */
    @Override
    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    @Override
    public ArrayList<EpicTask> getListOfEpicTasks() {
        return new ArrayList<>(epicTaskStorage.values());
    }

    @Override
    public ArrayList<EpicTask.SubTask> getListOfSubTasks() {
        return new ArrayList<>(subTaskStorage.values());
    }

    /**
     * 2.2 Удаление всех задач
     */
    @Override
    public void deleteAllTasks() {
        for (var key : taskStorage.keySet()) {
            inMemoryHistoryManager.remove(key);
        }
        taskStorage.clear();
    }

    @Override
    public void deleteAllEpicTasks() {
        for (var key : epicTaskStorage.keySet()) {
            inMemoryHistoryManager.remove(key);
            EpicTask epicTask = epicTaskStorage.get(key);
            if (epicTask != null) {
                ArrayList<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
                listOfSubTaskId.clear();
            }
        }
        epicTaskStorage.clear();

        for (var key : subTaskStorage.keySet()) {
            inMemoryHistoryManager.remove(key);
        }
        subTaskStorage.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (var key : subTaskStorage.keySet()) {
            inMemoryHistoryManager.remove(key);
            EpicTask.SubTask subTask = subTaskStorage.get(key);
            if (subTask != null) {
                EpicTask epicTask = epicTaskStorage.get(subTask.getEpicTaskId());
                if (epicTask != null) {
                    ArrayList<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
                    listOfSubTaskId.clear();
                    updateEpicTask(epicTask);
                }
            }
        }
        subTaskStorage.clear();
    }

    /**
     * 2.3 Получение задачи по идентификатору
     */
    @Override
    public Task getTaskById(int id) {
        Task task = taskStorage.get(id);
        if (task != null) {
            inMemoryHistoryManager.add(task);
        }
        return task;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask epicTask = epicTaskStorage.get(id);
        if (epicTask != null) {
            inMemoryHistoryManager.add(epicTask);
        }
        return epicTask;
    }

    @Override
    public EpicTask.SubTask getSubTaskById(int id) {
        EpicTask.SubTask subTask = subTaskStorage.get(id);
        if (subTask != null) {
            inMemoryHistoryManager.add(subTask);
        }
        return subTask;
    }

    /**
     * 2.4 Создание задачи
     */
    @Override
    public Task createCopyOfTask(Task task) {
        return new Task(task.getName(), task.getDescription(), task.getStatus());
    }

    @Override
    public EpicTask createCopyOfTask(EpicTask epicTask) {
        return new EpicTask(epicTask.getName(), epicTask.getDescription(), epicTask.getListOfSubTaskId(), epicTask.getStatus());
    }

    @Override
    public EpicTask.SubTask createCopyOfTask(EpicTask.SubTask subTask) {
        return new EpicTask.SubTask(subTask.getEpicTaskId(), subTask.getName(), subTask.getDescription(), subTask.getStatus());
    }

    /**
     * 2.5 Обновление задачи
     */
    @Override
    public void updateTask(Task task) {
        int taskId = task.getId();
        String taskName = task.getName();
        String taskDescription = task.getDescription();
        Task.Status taskStatus = task.getStatus();
        Task newTask = new Task(taskId, taskName, taskDescription, taskStatus);
        taskStorage.put(taskId, newTask);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        int epicTaskId = epicTask.getId();
        String epicTaskName = epicTask.getName();
        String epicTaskDescription = epicTask.getDescription();
        ArrayList<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
        Task.Status epicTaskStatus = getEpicTaskStatus(listOfSubTaskId);
        EpicTask newEpicTask = new EpicTask(epicTaskId, epicTaskName, epicTaskDescription
                , listOfSubTaskId, epicTaskStatus);

        epicTask.setStatus(epicTaskStatus);
        epicTaskStorage.put(epicTaskId, newEpicTask);
    }

    @Override
    public void updateSubTask(EpicTask.SubTask subTask) {
        int subTaskId = subTask.getId();
        int epicTaskId = subTask.getEpicTaskId();
        String subTaskName = subTask.getName();
        String subTaskDescription = subTask.getDescription();
        Task.Status subTaskStatus = subTask.getStatus();
        EpicTask.SubTask newSubTask = new EpicTask.SubTask(subTaskId, epicTaskId, subTaskName, subTaskDescription
                , subTaskStatus);
        subTaskStorage.put(subTaskId, newSubTask);

        EpicTask epicTask = epicTaskStorage.get(epicTaskId);
        if (epicTask != null) {
            ArrayList<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
            Task.Status newStatus = getEpicTaskStatus(listOfSubTaskId);
            epicTask.setStatus(newStatus);
        }
    }

    /**
     * 2.6 Удаление задачи
     */
    @Override
    public void removeTaskById(int id) {
        inMemoryHistoryManager.remove(id);
        taskStorage.remove(id);

    }

    @Override
    public void removeEpicTaskById(int id) {
        EpicTask epicTask = epicTaskStorage.get(id);
        if (epicTask != null) {
            for (int subTaskId : epicTask.getListOfSubTaskId()) {
                inMemoryHistoryManager.remove(subTaskId);
                subTaskStorage.remove(subTaskId);
            }
            inMemoryHistoryManager.remove(id);
            epicTaskStorage.remove(id);
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        EpicTask.SubTask subTask = subTaskStorage.get(id);
        if (subTask != null) {
            int epicTaskId = subTask.getEpicTaskId();
            EpicTask epicTask = epicTaskStorage.get(epicTaskId);
            if (epicTask != null) {
                epicTask.getListOfSubTaskId().remove((Integer) id);
                inMemoryHistoryManager.remove(id);
                subTaskStorage.remove(id);
                updateEpicTask(epicTask);
            }
        }
    }

    /**
     * 3.1 Получение списка всех подзадач определённого эпика
     */
    public ArrayList<EpicTask.SubTask> getListOfSubTaskByEpicTaskId(int id) {
        ArrayList<EpicTask.SubTask> listOfSubTaskByEpicTaskId = new ArrayList<>();
        for (var subTaskId : subTaskStorage.keySet()) {
            EpicTask.SubTask subTask = subTaskStorage.get(subTaskId);
            if (subTask != null && id == subTask.getEpicTaskId()) {
                listOfSubTaskByEpicTaskId.add(subTask);
            }
        }
        return listOfSubTaskByEpicTaskId;
    }

    /**
     * История просмотров задач
     */
    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public void remove(int id) {
        inMemoryHistoryManager.remove(id);
    }

    /**
     * 4. Метод для управления статусом для EpicTask задач
     */
    public Task.Status getterEpicTaskStatus(ArrayList<Integer> listOfSubTaskId) {
        return getEpicTaskStatus(listOfSubTaskId);
    }

    private Task.Status getEpicTaskStatus(ArrayList<Integer> listOfSubTaskId) {
        Task.Status statusEpicTask;
        int countNew = 0;
        int countDone = 0;

        for (var id : listOfSubTaskId) {
            EpicTask.SubTask subTask = subTaskStorage.get(id);
            if (subTask != null && subTask.getStatus().equals(Task.Status.NEW)) {
                countNew++;
            }
            if (subTask != null && subTask.getStatus().equals(Task.Status.DONE)) {
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
     * Метод для генерирования ID
     */
    private int idGeneration(Task task) {
        id += 1;
        task.setId(id);
        return id;
    }
}