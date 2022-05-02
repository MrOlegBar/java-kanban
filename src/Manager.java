import java.util.ArrayList;
import java.util.TreeMap;
/**
 * Класс для объекта-менеджера
 */
public class Manager {
    private static int Id = 0;

    private final TreeMap<Integer, Task> taskStorage = new TreeMap<>();

    private final TreeMap<Integer, EpicTask> epicTaskStorage = new TreeMap<>();

    private final TreeMap<Integer, EpicTask.SubTask> subTaskStorage = new TreeMap<>();
    /**
     * get и set методы
     */
    static int getId() {
        return Id;
    }

    static void setId(int id) {
        Id = id;
    }

    TreeMap<Integer, Task> getTaskStorage() {
        return taskStorage;
    }

    TreeMap<Integer, EpicTask> getEpicTaskStorage() {
        return epicTaskStorage;
    }

    TreeMap<Integer, EpicTask.SubTask> getSubTaskStorage() {
        return subTaskStorage;
    }
    /**
     * 1. Метод для сохранения задач всех типов.
     */
    void saveToStorage(Object object) {
        switch (object.getClass().toString()) {
            case "class Task": {
                taskStorage.put(((Task) object).getId(), (Task) object);
                break;
            }
            case "class EpicTask": {
                epicTaskStorage.put(((EpicTask) object).getId(), (EpicTask) object);
                break;
            }
            case "class EpicTask$SubTask": {
                subTaskStorage.put(((EpicTask.SubTask) object).getId(), (EpicTask.SubTask) object);
                break;
            }
        }
    }
    /**
     * 2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):
     *  2.1 Получение списка всех задач;
     */
    ArrayList<Object> getCompleteListOfAnyTasks(TreeMap<Integer, ? extends Task> treeMap) {
        ArrayList<Object> completeListOfAnyTasks = new ArrayList<>();

        for (Integer key : treeMap.keySet()) {
            completeListOfAnyTasks.add(treeMap.get(key));
        }
        return completeListOfAnyTasks;
    }
    /**
     *  2.2 Удаление всех задач;
     */
    void deleteAllTasksOfAnyType(TreeMap<Integer, ? extends Task> treeMap) {
        treeMap.clear();
    }
    /**
     *  2.3 Получение по идентификатору;
     */
    Object getTaskOfAnyTypeById(int id) {
        Object taskOfAnyKind = null;

        if (taskStorage.get(id) != null) {
            taskOfAnyKind = taskStorage.get(id);
        } else if (epicTaskStorage.get(id) != null) {
            taskOfAnyKind = epicTaskStorage.get(id);
        } else if (subTaskStorage.get(id) != null) {
            taskOfAnyKind = subTaskStorage.get(id);
        }
        return taskOfAnyKind;
    }
    /**
     *  2.4 Создание. Сам объект должен передаваться в качестве параметра;
     */
    Object createCopyOfTaskOfAnyType(Object object) {
        switch (object.getClass().toString()) {
            case "class Task": {
                return new Task((Task) object);
            }
            case "class EpicTask$SubTask": {
                return new EpicTask.SubTask((EpicTask.SubTask) object);
            }
            case "class EpicTask": {
                return new EpicTask((EpicTask) object);
            }
            default:
                return null;
        }
    }
    /**
     *  2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра;
     */
    void updateTaskOfAnyType(int id, Object object) {
        switch (object.getClass().toString()) {
            case "class Task": {
                taskStorage.put(id, (Task) object);
                break;
            }
            case "class EpicTask": {
                epicTaskStorage.put(id, (EpicTask) object);
                break;
            }
            case "class EpicTask$SubTask": {
                subTaskStorage.put(id, (EpicTask.SubTask) object);
                break;
            }
        }
    }
    /**
     *  2.6 Удаление по идентификатору.
     */
    void removeTaskOfAnyTypeById(int id) {
        for (Integer task : taskStorage.keySet()) {
            if (id == task) {
                taskStorage.remove(id);
                break;
            }
        }
        for (Integer epicTask : epicTaskStorage.keySet()) {
            if (id == epicTask) {
                epicTaskStorage.remove(id);
                break;
            }
        }
        for (Integer subTask : subTaskStorage.keySet()) {
            if (id == subTask) {
                subTaskStorage.remove(id);
                break;
            }
        }
    }
    /**
     * 3. Дополнительные методы:
     *  3.1 Получение списка всех подзадач определённого эпика.
     */
    ArrayList<EpicTask.SubTask> getCompleteListOfSubTaskByEpicTask(EpicTask epicTask) {
        return epicTask.getSubTasks();
    }
    /**
     * 4. Метод для управления статусом для эпик задач.
     */
    static String getEpicTaskStatus(ArrayList<EpicTask.SubTask> subTasks) {
        String statusEpicTask;
        int countNew = 0;
        int countDone = 0;

        for (EpicTask.SubTask subTask : subTasks) {
            if (subTask.getStatus().equalsIgnoreCase("NEW")) {
                countNew++;
            }
            if (!subTask.getStatus().equalsIgnoreCase("DONE")) {
                countDone++;
            }
        }
/**
 * Если у эпика нет подзадач или все они имеют статус NEW | DONE, то статус должен быть NEW | DONE.
 * Во всех остальных случаях статус должен быть IN_PROGRESS.
 */
        if ((subTasks.isEmpty()) || (countNew == subTasks.size())) {
            statusEpicTask = "NEW";
        } else if (countDone == subTasks.size()) {
            statusEpicTask = "DONE";
        } else {
            statusEpicTask = "IN_PROGRESS";
        }
        return statusEpicTask;
    }
}