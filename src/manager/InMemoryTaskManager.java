package manager;

import task.EpicTask;
import task.Task;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Класс для объекта-менеджера
 */
public class InMemoryTaskManager implements TaskManager {
    private final TreeMap<Integer, Task> taskStorage = new TreeMap<>();
    private final TreeMap<Integer, EpicTask> epicTaskStorage = new TreeMap<>();
    private final TreeMap<Integer, EpicTask.SubTask> subTaskStorage = new TreeMap<>();
    private int id = 0;
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    public HistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }


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
    private Task.Status getEpicTaskStatus(ArrayList<Integer> listOfSubTaskId) {
        Task.Status statusEpicTask;
        int countNew = 0;
        int countDone = 0;

        for (Integer id : listOfSubTaskId) {
            EpicTask.SubTask subTask = subTaskStorage.get(id);
            if (!subTask.equals(null) && subTask.getStatus().equals(Task.Status.NEW)) {
                countNew++;
            }
            if (!subTask.equals(null) && subTask.getStatus().equals(Task.Status.DONE)) {
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

    public Task.Status getterEpicTaskStatus(ArrayList<Integer> listOfSubTaskId) {
        return getEpicTaskStatus(listOfSubTaskId);
    }
    /**
     * 1. Метод для сохранения задач
     */
    private int idGeneration(Task task) {
        id += 1;
        task.setId(id);
        return id;
    }

    @Override
    public void saveTask(Task task) {
        taskStorage.put(idGeneration(task), task);
    }

    @Override
    public void saveEpicTask(EpicTask epicTask) {
        epicTaskStorage.put(idGeneration(epicTask), epicTask);
    }

    @Override
    public void saveSubTask(EpicTask.SubTask subTask) {
        subTaskStorage.put(idGeneration(subTask), subTask);
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
        taskStorage.clear();
    }

    @Override
    public void deleteAllEpicTasks() {
        for (int key : epicTaskStorage.keySet()) {
            EpicTask epicTask = epicTaskStorage.get(key);
            if (!epicTask.equals(null)) {
                epicTask.getListOfSubTaskId().clear();
            }
        }
        epicTaskStorage.clear();
        subTaskStorage.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTaskStorage.clear();
    }

    /**
     * 2.3 Получение задачи по идентификатору
     */
    @Override
    public Task getTaskById(int id) {
        Task task = taskStorage.get(id);
        if (!task.equals(null)) {
            inMemoryHistoryManager.add(task);
        }
        return task;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask epicTask = epicTaskStorage.get(id);
        if (!epicTask.equals(null)) {
            inMemoryHistoryManager.add(epicTask);
        }
        return epicTask;
    }

    @Override
    public EpicTask.SubTask getSubTaskById(int id) {
        EpicTask.SubTask subTask = subTaskStorage.get(id);
        if (!subTask.equals(null)) {
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
        Task newTask = new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
        taskStorage.put(task.getId(), newTask);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        Task.Status epicTaskStatus = getEpicTaskStatus(epicTask.getListOfSubTaskId());
        EpicTask newEpicTask = new EpicTask(epicTask.getId(), epicTask.getName(), epicTask.getDescription()
                , epicTask.getListOfSubTaskId(), epicTaskStatus);
        epicTask.setStatus(epicTaskStatus);
        epicTaskStorage.put(epicTask.getId(), newEpicTask);
    }

    @Override
    public void updateSubTask(EpicTask.SubTask subTask) {
        EpicTask.SubTask newSubTask = new EpicTask.SubTask(subTask.getId(), subTask.getEpicTaskId(), subTask.getName()
                , subTask.getDescription(), subTask.getStatus());
        subTaskStorage.put(subTask.getId(), newSubTask);

        int epicTaskId = subTask.getEpicTaskId();
        EpicTask epicTask = epicTaskStorage.get(epicTaskId);
        if (!epicTask.equals(null)) {
            Task.Status newStatus = getEpicTaskStatus(epicTask.getListOfSubTaskId());
            epicTask.setStatus(newStatus);
        }
    }

    /**
     * 2.6 Удаление задачи
     */
    @Override
    public void removeTaskById(int id) {
        taskStorage.remove(id);
    }

    @Override
    public void removeEpicTaskById(int id) {
        EpicTask epicTask = epicTaskStorage.get(id);
        if (!epicTask.equals(null)) {
            for (int subTaskId : epicTask.getListOfSubTaskId()) {
                subTaskStorage.remove(subTaskId);
            }
            epicTaskStorage.remove(id);
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        EpicTask.SubTask subTask = subTaskStorage.get(id);
        if (!subTask.equals(null)) {
            int epicTaskId = subTask.getEpicTaskId();
            EpicTask epicTask = epicTaskStorage.get(epicTaskId);
            if (!epicTask.equals(null)) {
                epicTask.getListOfSubTaskId().remove((Integer) id);
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
        for (int subTaskId : subTaskStorage.keySet()) {
            EpicTask.SubTask subTask = subTaskStorage.get(subTaskId);
            if (!subTask.equals(null) && id == subTask.getEpicTaskId()) {
                listOfSubTaskByEpicTaskId.add(subTask);
            }
        }
        return listOfSubTaskByEpicTaskId;
    }
}