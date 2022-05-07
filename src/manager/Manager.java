package manager;

import task.EpicTask;
import task.Task;
import java.util.ArrayList;
import java.util.TreeMap;
/**
 * Класс для объекта-менеджера
 */
public class Manager {
    private Integer id = 0;
    private ArrayList<Integer> listOfSubTaskId;
    private final TreeMap<Integer, Task> taskStorage = new TreeMap<>();
    private final TreeMap<Integer, EpicTask> epicTaskStorage = new TreeMap<>();
    private final TreeMap<Integer, EpicTask.SubTask> subTaskStorage = new TreeMap<>();

    /**
     * Метод для добавления подзадач в список
     */
    public void addSubtaskToList(EpicTask.SubTask subTask, ArrayList<Integer> listOfSubTaskId){
        if (!listOfSubTaskId.contains(subTask.getId())) {
            listOfSubTaskId.add(subTask.getId());
        } else {
            listOfSubTaskId.remove(subTask.getId());
            listOfSubTaskId.add(subTask.getId());
        }
    }
    /**
     * 1. Метод для сохранения задач
     */
    public void saveToTaskStorage(Task task) {
        id += 1;
        task.setId(id);
        taskStorage.put(id , task);
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
     * 2. Методы для каждого из типа задач
     *  2.1 Получение списка всех задач
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
     *  2.2 Удаление всех задач
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
     *  2.3 Получение задачи по идентификатору
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
     *  2.4 Создание задачи
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
     *  2.5 Обновление задачи
     */
    public void taskUpdate(Task task) {
        Task newTask = new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
        taskStorage.put(task.getId(), newTask);
    }

    public void epicTaskUpdate(EpicTask epicTask) {
        Task.Status epicTaskStatus = getEpicTaskStatus(epicTask.getListOfSubTaskId());
        EpicTask newEpicTask = new EpicTask(epicTask.getId(), epicTask.getName(), epicTask.getDescription()
                , epicTask.getListOfSubTaskId(), epicTaskStatus);
        epicTaskStorage.put(epicTask.getId(), newEpicTask);
    }

    public void subTaskUpdate(EpicTask.SubTask subTask) {
        EpicTask.SubTask newSubTask = new EpicTask.SubTask(subTask.getId(), subTask.getEpicTaskId(), subTask.getName()
                ,subTask.getDescription(), subTask.getStatus());
        subTaskStorage.put(subTask.getId(), newSubTask);
    }
    /**
     *  2.6 Удаление задачи
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
     * 3. Дополнительные методы
     *  3.1 Получение списка всех подзадач определённого эпика
     */
    /*public ArrayList<EpicTask.SubTask> getListOfSubTaskByEpicTask(int id) {
        ArrayList<Integer> listOfSubtasksId = null;
        ArrayList<Integer> returnedListOfSubtasksId = null;
        for (Integer idEpicTask : epicTaskStorage.keySet()) {
            if (id == idEpicTask) {
                listOfSubtasksId = epicTaskStorage.get(id).getSubTasksId();
                for (Integer idSubtask : listOfSubtasksId) {
                    returnedListOfSubtasksId.add(idSubtask);
                }
            }
        }
        return returnedListOfSubtasksId;
    }*/
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
}