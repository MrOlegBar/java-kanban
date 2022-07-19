package manager;

import exception.ManagerCreateException;
import task.EpicTask;
import task.Task;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс для объекта-менеджера
 */
public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> taskStorage = new TreeMap<>();
    private final Map<Integer, EpicTask> epicTaskStorage = new TreeMap<>();
    private final Map<Integer, EpicTask.SubTask> subTaskStorage = new TreeMap<>();
    private int id = 0;
    private final Set<Task> listOfPrioritizedTasks = new TreeSet<>((task1, task2) -> {
        if ((task1.getStartTime() != null) && (task2.getStartTime() != null)) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        } else if (task1.getStartTime() == null) {
            return 1;
        } else if (null == task2.getStartTime()) {
            return -1;
        }else {
            return 0;
        }
    });

    protected HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Добавляет id подзадачи в список id подзадач Epic задачи, а id Epic задачи в подзадачу
     */
    @Override
    public void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask) {
        List<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
        int subTaskId = subTask.getId();
        int epicTaskId = epicTask.getId();

        if (listOfSubTaskId.contains(subTaskId)) {
            listOfSubTaskId.remove(subTaskId);
        }
        listOfSubTaskId.add(subTaskId);
        subTask.setEpicTaskId(epicTaskId);
    }

    /**
     * Возвращает список всех подзадач Epic задачи по id
     */
    public List<EpicTask.SubTask> getListOfSubTaskByEpicTaskId(int id) {
        List<EpicTask.SubTask> listOfSubTaskByEpicTaskId = new ArrayList<>();
        for (var subTaskId : subTaskStorage.keySet()) {
            EpicTask.SubTask subTask = subTaskStorage.get(subTaskId);
            if (subTask != null && id == subTask.getEpicTaskId()) {
                listOfSubTaskByEpicTaskId.add(subTask);
            }
        }
        return listOfSubTaskByEpicTaskId;
    }

    /**
     * Геттер для метода, который возвращает статус Epic задачи, рассчитанный на основе статусов подзадач
     */
    @Override
    public Task.Status getterEpicTaskStatus(List<Integer> listOfSubTaskId) {
        return getEpicTaskStatus(listOfSubTaskId);
    }

    /**
     * Геттер для метода, который возвращает дату и время начала самой ранней подзадачи Epic задачи
     */
    @Override
    public LocalDateTime getterEpicTaskStartTime(List<Integer> listOfSubTaskId) {
        return getEpicTaskStartTime(listOfSubTaskId);
    }

    /**
     * Геттер для метода, который возвращает сумму продолжительностей всех подзадач Epic задач
     */
    @Override
    public long getterEpicTaskDuration(List<Integer> listOfSubTaskId) {
        return getEpicTaskDuration(listOfSubTaskId);
    }

    /**
     * Возвращает дату и время конца самой поздней подзадачи Epic задачи
     */
    @Override
    public LocalDateTime getterEpicTaskEndTime(List<Integer> listOfSubTaskId) {
        return getEpicTaskEndTime(listOfSubTaskId);
    }

    /**
     * Проверяет задачу на пересечение по времени выполнения с уже созданными задачами
     */
    public void checkIntersectionByTaskTime(Task task) {
        LocalDateTime startTimeTask = task.getStartTime();
        LocalDateTime endTimeTask = task.getEndTime();
        Set<Task> listOfSortedTasks = getPrioritizedTasks();

        if (startTimeTask != null) {
            for (var taskFromTheList : listOfSortedTasks) {
                if (taskFromTheList.getStartTime() != null) {
                    LocalDateTime startTimeTaskFromList = taskFromTheList.getStartTime();
                    LocalDateTime endTimeTaskFromList = taskFromTheList.getEndTime();
                    if ((startTimeTask.isAfter(startTimeTaskFromList)
                            && startTimeTask.isBefore(endTimeTaskFromList))
                            || (endTimeTask.isAfter(startTimeTaskFromList)
                            && endTimeTask.isBefore(endTimeTaskFromList))
                            || (startTimeTaskFromList.isAfter(startTimeTask)
                            && endTimeTaskFromList.isBefore(endTimeTask))
                            || (startTimeTask.isAfter(startTimeTaskFromList)
                            && endTimeTask.isBefore(endTimeTaskFromList))
                            || (startTimeTask.equals(startTimeTaskFromList))
                            || (endTimeTask.equals(endTimeTaskFromList))) {
                        throw new ManagerCreateException("Задачи и подзадачи пересекаются по времени выполнения");
                    }
                }
            }
        }
    }

    /**
     * Возвращает задачу Task после проверки на пересечение по времени выполнения
     */
    @Override
    public Task createTask(Task task) {
        checkIntersectionByTaskTime(task);
        return new Task(task.getName(), task.getDescription(), task.getStatus(), task.getStartTime(), task.getDuration());
    }

    /**
     * Возвращает задачу EpicTask
     */
    @Override
    public EpicTask createTask(EpicTask epicTask) {
        return new EpicTask(epicTask.getName(), epicTask.getDescription(), epicTask.getListOfSubTaskId()
                , epicTask.getStatus(), epicTask.getStartTime(), epicTask.getDuration());
    }

    /**
     * Возвращает задачу SubTask после проверки на пересечение по времени выполнения
     */
    @Override
    public EpicTask.SubTask createTask(EpicTask.SubTask subTask) {
        checkIntersectionByTaskTime(subTask);
        return new EpicTask.SubTask(subTask.getEpicTaskId(), subTask.getName(), subTask.getDescription()
                , subTask.getStatus(), subTask.getStartTime(), subTask.getDuration());
    }

    /**
     * Сохраняет задачу в коллекцию
     */
    @Override
    public void saveTask(Task task) {
        int taskId = idGeneration(task);
        taskStorage.put(taskId, task);
        listOfPrioritizedTasks.add(task);
    }

    /**
     * Сохраняет Epic задачу в коллекцию
     */
    @Override
    public void saveEpicTask(EpicTask epicTask) {
        int epicTaskId = idGeneration(epicTask);
        epicTaskStorage.put(epicTaskId, epicTask);
    }

    /**
     * Сохраняет подзадачу в коллекцию
     */
    @Override
    public void saveSubTask(EpicTask.SubTask subTask) {
        int subTaskId = idGeneration(subTask);
        subTaskStorage.put(subTaskId, subTask);
        listOfPrioritizedTasks.add(subTask);
    }

    /**
     * Возвращает задачу по id и добавляет ее в историю задач
     */
    @Override
    public Task getTaskById(int id) {
        Task task = taskStorage.get(id);
        if (task != null) {
            inMemoryHistoryManager.addTaskToTaskHistory(task);
        }
        return task;
    }

    /**
     * Возвращает Epic задачу по id и добавляет ее в историю задач
     */
    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask epicTask = epicTaskStorage.get(id);
        if (epicTask != null) {
            inMemoryHistoryManager.addTaskToTaskHistory(epicTask);
        }
        return epicTask;
    }

    /**
     * Возвращает подзадачу по id и добавляет ее в историю задач
     */
    @Override
    public EpicTask.SubTask getSubTaskById(int id) {
        EpicTask.SubTask subTask = subTaskStorage.get(id);
        if (subTask != null) {
            inMemoryHistoryManager.addTaskToTaskHistory(subTask);
        }
        return subTask;
    }

    /**
     * Обновляет задачу после проверки на пересечение по времени выполнения
     */
    @Override
    public void updateTask(Task task) {
        checkIntersectionByTaskTime(task);
        int taskId = task.getId();
        task.setId(taskId);
        String taskName = task.getName();
        task.setName(taskName);
        String taskDescription = task.getDescription();
        task.setDescription(taskDescription);
        Task.Status taskStatus = task.getStatus();
        task.setStatus(taskStatus);
        LocalDateTime startTime = task.getStartTime();
        task.setStartTime(startTime);
        long duration = task.getDuration();
        task.setDuration(duration);
        task.setEndTime(task.getEndTime());
        Task newTask = new Task(taskId, taskName, taskDescription, taskStatus, startTime, duration);
        taskStorage.put(taskId, newTask);
        listOfPrioritizedTasks.add(task);
    }

    /**
     * Обновляет Epic задачу после проверки на пересечение по времени выполнения
     */
    @Override
    public void updateEpicTask(EpicTask epicTask) {
        int epicTaskId = epicTask.getId();
        String epicTaskName = epicTask.getName();
        String epicTaskDescription = epicTask.getDescription();
        List<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
        epicTask.setListOfSubTaskId(listOfSubTaskId);
        Task.Status epicTaskStatus = getEpicTaskStatus(listOfSubTaskId);
        epicTask.setStatus(epicTaskStatus);
        LocalDateTime epicTaskStartTime = getEpicTaskStartTime(listOfSubTaskId);
        epicTask.setStartTime(epicTaskStartTime);
        long epicTaskDuration = getEpicTaskDuration(listOfSubTaskId);
        epicTask.setDuration(epicTaskDuration);
        epicTask.setEndTime(epicTask.getEndTime());
        EpicTask newEpicTask = new EpicTask(epicTaskId, epicTaskName, epicTaskDescription
                , listOfSubTaskId, epicTaskStatus, epicTaskStartTime, epicTaskDuration);
        epicTaskStorage.put(epicTaskId, newEpicTask);
        listOfPrioritizedTasks.add(epicTask);
    }

    /**
     * Обновляет подзадачу после проверки на пересечение по времени выполнения
     */
    @Override
    public void updateSubTask(EpicTask.SubTask subTask) {
        checkIntersectionByTaskTime(subTask);
        int subTaskId = subTask.getId();
        subTask.setId(subTaskId);
        int epicTaskId = subTask.getEpicTaskId();
        subTask.setEpicTaskId(epicTaskId);
        String subTaskName = subTask.getName();
        subTask.setName(subTaskName);
        String subTaskDescription = subTask.getDescription();
        subTask.setDescription(subTaskDescription);
        Task.Status subTaskStatus = subTask.getStatus();
        subTask.setStatus(subTaskStatus);
        LocalDateTime startTime = subTask.getStartTime();
        subTask.setStartTime(startTime);
        long duration = subTask.getDuration();
        subTask.setDuration(duration);
        subTask.setEndTime(subTask.getEndTime());
        EpicTask.SubTask newSubTask = new EpicTask.SubTask(subTaskId, epicTaskId, subTaskName, subTaskDescription
                , subTaskStatus, startTime, duration);
        subTaskStorage.put(subTaskId, newSubTask);
        listOfPrioritizedTasks.add(subTask);

        EpicTask epicTask = epicTaskStorage.get(epicTaskId);
        if (epicTask != null) {
            List<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
            Task.Status newStatus = getEpicTaskStatus(listOfSubTaskId);
            epicTask.setStatus(newStatus);
        }
    }

    /**
     * Возвращает из коллекции список всех задач
     */
    @Override
    public List<Task> getListOfTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    /**
     * Возвращает из коллекции список всех Epic задач
     */
    @Override
    public List<EpicTask> getListOfEpicTasks() {
        return new ArrayList<>(epicTaskStorage.values());
    }

    /**
     * Возвращает из коллекции список всех подзадач
     */
    @Override
    public List<EpicTask.SubTask> getListOfSubTasks() {
        return new ArrayList<>(subTaskStorage.values());
    }

    /**
     * Геттер для метода, который возвращает список всех задач
     * , отсортированный по дате и времени начала самой ранней задачи
     */
    public Set<Task> getterPrioritizedTasks() {
        return getPrioritizedTasks();
    }

    /**
     * Удаляет по id задачу из коллекции и истории задач
     */
    @Override
    public void removeTaskById(int id) {
        inMemoryHistoryManager.removeTaskFromTaskHistory(id);
        Task task = taskStorage.get(id);
        if (task != null) {
            listOfPrioritizedTasks.remove(task);
        }
        taskStorage.remove(id);

    }

    /**
     * Удаляет по id  Epic задачу из коллекции и истории задач
     */
    @Override
    public void removeEpicTaskById(int id) {
        EpicTask epicTask = epicTaskStorage.get(id);
        if (epicTask != null) {
            for (int subTaskId : epicTask.getListOfSubTaskId()) {
                inMemoryHistoryManager.removeTaskFromTaskHistory(subTaskId);
                EpicTask.SubTask subTask = subTaskStorage.get(id);
                if (subTask != null) {
                    listOfPrioritizedTasks.remove(subTask);
                }
                subTaskStorage.remove(subTaskId);
            }
            inMemoryHistoryManager.removeTaskFromTaskHistory(id);
            epicTaskStorage.remove(id);
        }
    }

    /**
     * Удаляет по id подзадачу из коллекции и истории задач
     */
    @Override
    public void removeSubTaskById(int id) {
        EpicTask.SubTask subTask = subTaskStorage.get(id);
        if (subTask != null) {
            int epicTaskId = subTask.getEpicTaskId();
            EpicTask epicTask = epicTaskStorage.get(epicTaskId);
            if (epicTask != null) {
                epicTask.getListOfSubTaskId().remove((Integer) id);
                inMemoryHistoryManager.removeTaskFromTaskHistory(id);
                listOfPrioritizedTasks.remove(subTask);
                subTaskStorage.remove(id);
                updateEpicTask(epicTask);
            }
        }
    }

    /**
     * Удаляет все задачи из коллекции и истории задач
     */
    @Override
    public void deleteAllTasks() {
        for (var key : taskStorage.keySet()) {
            inMemoryHistoryManager.removeTaskFromTaskHistory(key);
        }
        taskStorage.clear();
    }

    /**
     * Удаляет все Epic задачи и связанные с ними подзадачи из коллекции и истории задач
     */
    @Override
    public void deleteAllEpicTasks() {
        for (var key : epicTaskStorage.keySet()) {
            inMemoryHistoryManager.removeTaskFromTaskHistory(key);
            EpicTask epicTask = epicTaskStorage.get(key);
            if (epicTask != null) {
                List<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
                listOfSubTaskId.clear();
            }
        }
        epicTaskStorage.clear();

        for (var key : subTaskStorage.keySet()) {
            inMemoryHistoryManager.removeTaskFromTaskHistory(key);
        }
        subTaskStorage.clear();
    }

    /**
     * Удаляет все подзадачи из коллекции и истории задач
     */
    @Override
    public void deleteAllSubTasks() {
        for (var key : subTaskStorage.keySet()) {
            inMemoryHistoryManager.removeTaskFromTaskHistory(key);
            EpicTask.SubTask subTask = subTaskStorage.get(key);
            if (subTask != null) {
                EpicTask epicTask = epicTaskStorage.get(subTask.getEpicTaskId());
                if (epicTask != null) {
                    List<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
                    listOfSubTaskId.clear();
                    updateEpicTask(epicTask);
                }
            }
        }
        subTaskStorage.clear();
    }

    /**
     * Возвращает список истории задач
     */
    @Override
    public List<Task> getListOfTaskHistory() {
        return inMemoryHistoryManager.getTaskHistory();
    }

    /**
     * Удаляет по id задачу из просмотра
     */
    @Override
    public void removeTaskFromTaskHistory(int id) {
        inMemoryHistoryManager.removeTaskFromTaskHistory(id);
    }

    /**
     * Возвращает id для новой задачи
     */
    private int idGeneration(Task task) {
        task.setId(++id);
        return task.getId();
    }

    /**
     * Возвращает статус Epic задачиб, рассчитанный на основе статусов подзадач
     */
    private Task.Status getEpicTaskStatus(List<Integer> listOfSubTaskId) {
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
     * Возвращает дату и время начала самой ранней подзадачи
     */
    private LocalDateTime getEpicTaskStartTime(List<Integer> listOfSubTaskId) {
        try {
            EpicTask.SubTask subTaskForStartTimeMin;

            LocalDateTime startTimeMin = null;
            if (listOfSubTaskId.size() != 0) {
                subTaskForStartTimeMin = subTaskStorage.get(listOfSubTaskId.get(0));
                startTimeMin = subTaskForStartTimeMin.getStartTime();
            }
            for (int i = 1; i < listOfSubTaskId.size(); i++) {
                EpicTask.SubTask subTask = subTaskStorage.get(listOfSubTaskId.get(i));
                if (subTask.getStartTime().isBefore(startTimeMin)) {
                    startTimeMin = subTask.getStartTime();
                }
            }
            return startTimeMin;
        } catch (ClassCastException | NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Возвращает сумму продолжительностей всех подзадач Epic задач
     */
    private long getEpicTaskDuration(List<Integer> listOfSubTaskId) {
        long durationEpicTask = 0;
        for (var id : listOfSubTaskId) {
            EpicTask.SubTask subTask = subTaskStorage.get(id);
            if (subTask != null) {
                durationEpicTask += subTask.getDuration();
            }
        }
        return durationEpicTask;
    }

    /**
     * Возвращает дату и время конца самой поздней подзадачи Epic задачи
     */
    private LocalDateTime getEpicTaskEndTime(List<Integer> listOfSubTaskId) {
        try {
            EpicTask.SubTask subTaskForStartTimeMax;

            LocalDateTime startTimeMax = null;
            if (listOfSubTaskId.size() != 0) {
                subTaskForStartTimeMax = subTaskStorage.get(listOfSubTaskId.get(0));
                startTimeMax = subTaskForStartTimeMax.getEndTime();
            }
            for (int i = 1; i < listOfSubTaskId.size(); i++) {
                EpicTask.SubTask subTask = subTaskStorage.get(listOfSubTaskId.get(i));
                if (subTask.getEndTime().isAfter(startTimeMax)) {
                    startTimeMax = subTask.getEndTime();
                }
            }
            return startTimeMax;
        } catch (ClassCastException | NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Возвращает список задач, отсортированный по дате и времени начала самой ранней задачи
     */
    private Set<Task> getPrioritizedTasks() {
        return listOfPrioritizedTasks;
    }
}