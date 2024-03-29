package managers;

import exceptions.ManagerCreateException;
import exceptions.ManagerDeleteException;
import exceptions.ManagerGetException;
import exceptions.ManagerSaveException;
import servers.KVTaskClient;
import tasks.EpicTask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс для объекта-менеджера
 */
public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> taskStorage = new TreeMap<>();
    private final Map<Integer, EpicTask> epicTaskStorage = new TreeMap<>();
    private final Map<Integer, EpicTask.SubTask> subTaskStorage = new TreeMap<>();

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

    /**
     * Добавляет id подзадачи в список id подзадач Epic задачи, а id Epic задачи в подзадачу
     */
    @Override
    public void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask) {
        List<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
        int subTaskId = subTask.getId();
        int epicTaskId = epicTask.getId();

        if (listOfSubTaskId.contains(subTaskId)) {
            listOfSubTaskId.remove((Integer) subTaskId);
        }

        listOfSubTaskId.add(subTaskId);
        Task.Status status = getEpicTaskStatus(listOfSubTaskId);
        epicTask.setStatus(status);
        LocalDateTime startTime = getEpicTaskStartTime(listOfSubTaskId);
        epicTask.setStartTime(startTime);
        long duration = getEpicTaskDuration(listOfSubTaskId);
        epicTask.setDuration(duration);
        LocalDateTime endTime = getEpicTaskEndTime(listOfSubTaskId);
        epicTask.setEndTime(endTime);

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
    public void checkIntersectionByTaskTime(Task task) throws ManagerCreateException {
        LocalDateTime startTimeTask = task.getStartTime();
        LocalDateTime endTimeTask = task.getEndTime();
        Set<Task> listOfSortedTasks;
        try {
            listOfSortedTasks = getPrioritizedTasks();
        } catch (ManagerGetException e) {
            return;
        }

        if (startTimeTask != null) {
            for (var sortedTask : listOfSortedTasks) {
                if (task.getId() == sortedTask.getId()) {
                    continue;
                }

                if (sortedTask.getStartTime() != null) {
                    LocalDateTime startTimeTaskFromList = sortedTask.getStartTime();
                    LocalDateTime endTimeTaskFromList = sortedTask.getEndTime();
                    if ((startTimeTask.isAfter(startTimeTaskFromList) && startTimeTask.isBefore(endTimeTaskFromList))
                            || (endTimeTask.isAfter(startTimeTaskFromList)
                            && endTimeTask.isBefore(endTimeTaskFromList))
                            || (startTimeTaskFromList.isAfter(startTimeTask)
                            && endTimeTaskFromList.isBefore(endTimeTask))
                            || (startTimeTask.isAfter(startTimeTaskFromList)
                            && endTimeTask.isBefore(endTimeTaskFromList))
                            || (startTimeTask.equals(startTimeTaskFromList))
                            || (endTimeTask.equals(endTimeTaskFromList))) {
                        throw new ManagerCreateException("Задачи пересекаются по времени выполнения");
                    }
                }
            }
        }
    }

    /**
     * Возвращает задачу Task после проверки на пересечение по времени выполнения
     */
    @Override
    public Task createTask(Task task) throws ManagerCreateException, IOException {
        checkIntersectionByTaskTime(task);
        if (task.getId() != 0) {
            Task newTask = new Task(task.getId()
                    , task.getName()
                    , task.getDescription()
                    , task.getStatus()
                    , task.getStartTime()
                    , task.getDuration());

            saveTask(newTask);
            return newTask;
        }
        Task newTask = new Task(task.getName()
                , task.getDescription()
                , task.getStatus()
                , task.getStartTime()
                , task.getDuration());

        saveTask(newTask);
        return newTask;
    }

    /**
     * Возвращает задачу EpicTask
     */
    @Override
    public EpicTask createTask(EpicTask epicTask) {
        if (epicTask.getId() != 0) {
            EpicTask newEpicTask = new EpicTask(epicTask.getId()
                    , epicTask.getName()
                    , epicTask.getDescription()
                    , epicTask.getListOfSubTaskId()
                    , epicTask.getStatus()
                    , epicTask.getStartTime()
                    , epicTask.getDuration());

            saveEpicTask(newEpicTask);
            return newEpicTask;
        }
        Task.Status status = getEpicTaskStatus(epicTask.getListOfSubTaskId());
        LocalDateTime startTime = getEpicTaskStartTime(epicTask.getListOfSubTaskId());
        long duration = getEpicTaskDuration(epicTask.getListOfSubTaskId());
        EpicTask newEpicTask = new EpicTask(epicTask.getName()
                , epicTask.getDescription()
                , epicTask.getListOfSubTaskId()
                , status, startTime, duration);

        saveEpicTask(newEpicTask);
        return newEpicTask;
    }

    /**
     * Возвращает задачу SubTask после проверки на пересечение по времени выполнения
     */
    @Override
    public EpicTask.SubTask createTask(EpicTask.SubTask subTask) throws ManagerCreateException {
        if (subTask.getId() != 0) {
            EpicTask.SubTask newSubTask = new EpicTask.SubTask(subTask.getId()
                    , subTask.getEpicTaskId()
                    , subTask.getName()
                    , subTask.getDescription()
                    , subTask.getStatus()
                    , subTask.getStartTime()
                    , subTask.getDuration());

            saveSubTask(newSubTask);
            return newSubTask;
        }
        if (epicTaskStorage.get(subTask.getEpicTaskId()) != null) {
            checkIntersectionByTaskTime(subTask);
            EpicTask.SubTask newSubTask = new EpicTask.SubTask(subTask.getEpicTaskId()
                    , subTask.getName()
                    , subTask.getDescription()
                    , subTask.getStatus()
                    , subTask.getStartTime()
                    , subTask.getDuration());

            saveSubTask(newSubTask);
            return newSubTask;
        } else {
            throw new ManagerCreateException("Не существует Epic задачи для данной подзадачи");
        }
    }

    /**
     * Сохраняет задачу в коллекцию
     */
    @Override
    public void saveTask(Task task) throws IOException {
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
    public void saveSubTask(EpicTask.SubTask subTask) throws ManagerSaveException {
        int subTaskId = idGeneration(subTask);
        subTaskStorage.put(subTaskId, subTask);
        addSubtaskToEpicTask(subTask, epicTaskStorage.get(subTask.getEpicTaskId()));
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
    public void updateTask(Task task) throws ManagerCreateException {
        if (taskStorage.containsValue(task) || taskStorage.isEmpty()) {
            throw new ManagerCreateException("Задача с таким ID не может быть обновлена");
        }
        checkIntersectionByTaskTime(task);
        int taskId = task.getId();
        String taskName = task.getName();
        String taskDescription = task.getDescription();
        Task.Status taskStatus = task.getStatus();
        LocalDateTime startTime = task.getStartTime();
        long duration = task.getDuration();
        Task newTask = new Task(taskId, taskName, taskDescription, taskStatus, startTime, duration);
        taskStorage.put(taskId, newTask);
        listOfPrioritizedTasks.add(task);
    }

    /**
     * Обновляет Epic задачу после проверки на пересечение по времени выполнения
     */
    @Override
    public void updateEpicTask(EpicTask epicTask) throws ManagerCreateException {
        if (epicTaskStorage.isEmpty()) {
            throw new ManagerCreateException("Задача с таким ID не может быть обновлена");
        }
        int epicTaskId = epicTask.getId();
        String epicTaskName = epicTask.getName();
        String epicTaskDescription = epicTask.getDescription();
        List<Integer> listOfSubTaskId = epicTask.getListOfSubTaskId();
        Task.Status epicTaskStatus = getEpicTaskStatus(listOfSubTaskId);
        LocalDateTime epicTaskStartTime;
        if (epicTask.getStartTime() == null) {
            epicTaskStartTime = getEpicTaskStartTime(listOfSubTaskId);
        } else {
            epicTaskStartTime = epicTask.getStartTime();
        }
        if (epicTask.getEndTime() == null) {
            epicTask.setEndTime(getEpicTaskEndTime(listOfSubTaskId));
        } else {
            epicTask.setEndTime(epicTask.getEndTime());
        }
        long epicTaskDuration;
        if (epicTask.getDuration() == 0) {
            epicTaskDuration = getEpicTaskDuration(listOfSubTaskId);
        } else {
            epicTaskDuration = epicTask.getDuration();
        }
        EpicTask newEpicTask = new EpicTask(epicTaskId, epicTaskName, epicTaskDescription
                , listOfSubTaskId, epicTaskStatus, epicTaskStartTime, epicTaskDuration);
        epicTaskStorage.put(epicTaskId, newEpicTask);
    }

    /**
     * Обновляет подзадачу после проверки на пересечение по времени выполнения
     */
    @Override
    public void updateSubTask(EpicTask.SubTask subTask) throws ManagerCreateException {
        if (subTaskStorage.containsValue(subTask) || subTaskStorage.isEmpty()) {
            throw new ManagerCreateException("Задача с таким ID не может быть обновлена");
        }
        checkIntersectionByTaskTime(subTask);
        int subTaskId = subTask.getId();
        int epicTaskId = subTask.getEpicTaskId();
        String subTaskName = subTask.getName();
        String subTaskDescription = subTask.getDescription();
        Task.Status subTaskStatus = subTask.getStatus();
        LocalDateTime startTime = subTask.getStartTime();
        long duration = subTask.getDuration();
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
        List<Task> listOfTasks = new ArrayList<>();
        if (!taskStorage.isEmpty()) {
            listOfTasks = new ArrayList<>(taskStorage.values());
        }
        return listOfTasks;
    }

    /**
     * Возвращает из коллекции список всех Epic задач
     */
    @Override
    public List<EpicTask> getListOfEpicTasks() {
        List<EpicTask> listOfEpicTasks = new ArrayList<>();
        if (!epicTaskStorage.isEmpty()) {
            listOfEpicTasks = new ArrayList<>(epicTaskStorage.values());
        }
        return listOfEpicTasks;
    }

    /**
     * Возвращает из коллекции список всех подзадач
     */
    @Override
    public List<EpicTask.SubTask> getListOfSubTasks() {
        List<EpicTask.SubTask> listOfSubTasks = new ArrayList<>();
        if (!subTaskStorage.isEmpty()) {
            listOfSubTasks = new ArrayList<>(subTaskStorage.values());
        }
        return listOfSubTasks;
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
    public void removeTaskById(int id) throws ManagerDeleteException {
        Task task = taskStorage.get(id);
        if (task != null) {
            listOfPrioritizedTasks.remove(task);
        }

        inMemoryHistoryManager.removeTaskFromTaskHistory(id);
        taskStorage.remove(id);

    }

    /**
     * Удаляет по id  Epic задачу из коллекции и истории задач
     */
    @Override
    public void removeEpicTaskById(int id) throws ManagerDeleteException {
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
        } else {
            throw new ManagerDeleteException("Задача с таким ID отсутвует");
        }
    }

    /**
     * Удаляет по id подзадачу из коллекции и истории задач
     */
    @Override
    public void removeSubTaskById(int id) throws ManagerDeleteException {
        EpicTask.SubTask subTask = subTaskStorage.get(id);
        if (subTask != null) {
            int epicTaskId = subTask.getEpicTaskId();
            EpicTask epicTask = epicTaskStorage.get(epicTaskId);
            List<Integer> listOfSubTaskId;
            if (epicTask != null) {
                listOfSubTaskId = epicTask.getListOfSubTaskId();
                listOfSubTaskId.remove((Integer) id);
                inMemoryHistoryManager.removeTaskFromTaskHistory(id);
                listOfPrioritizedTasks.remove(subTask);
                subTaskStorage.remove(id);
                updateEpicTask(epicTask);
            }
        } else {
            throw new ManagerDeleteException("Задача с таким ID отсутвует");
        }
    }

    /**
     * Удаляет все задачи из коллекции и истории задач
     */
    @Override
    public void deleteAllTasks() throws ManagerDeleteException {
        if (taskStorage.isEmpty()) {
            throw new ManagerDeleteException("Нет задач для удаления");
        }
        for (Integer id : taskStorage.keySet()) {
            inMemoryHistoryManager.removeTaskFromTaskHistory(id);
            listOfPrioritizedTasks.remove(taskStorage.get(id));
        }
        taskStorage.clear();
    }

    /**
     * Удаляет все Epic задачи и связанные с ними подзадачи из коллекции и истории задач
     */
    @Override
    public void deleteAllEpicTasks() throws ManagerDeleteException {
        if (epicTaskStorage.isEmpty()) {
            throw new ManagerDeleteException("Нет задач для удаления");
        }

        for (Integer id : epicTaskStorage.keySet()) {
            inMemoryHistoryManager.removeTaskFromTaskHistory(id);
        }
        epicTaskStorage.clear();

        for (Integer id : subTaskStorage.keySet()) {
            inMemoryHistoryManager.removeTaskFromTaskHistory(id);
        }
        subTaskStorage.clear();
    }

    /**
     * Удаляет все подзадачи из коллекции и истории задач
     */
    @Override
    public void deleteAllSubTasks() throws ManagerDeleteException {
        if (subTaskStorage.isEmpty()) {
            throw new ManagerDeleteException("Нет задач для удаления");
        }

        for (Integer id : subTaskStorage.keySet()) {
            inMemoryHistoryManager.removeTaskFromTaskHistory(id);
            listOfPrioritizedTasks.remove(subTaskStorage.get(id));

            EpicTask.SubTask subTask = subTaskStorage.get(id);
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

    @Override
    public KVTaskClient getKVTaskClient() {
        return null;
    }

    @Override
    public Integer toJson(String key) throws IOException, InterruptedException {
        return null;
    }

    /**
     * Возвращает id для новой задачи
     */
    private int idGeneration(Task task) {
        int id = task.getId();
        if (id == 0) {
            id += 1;

            while (taskStorage.containsKey(id)) {
                id++;
            }
            while (epicTaskStorage.containsKey(id)) {
                id++;
            }
            while (subTaskStorage.containsKey(id)) {
                id++;
            }
            task.setId(id);
        }
        return id;
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
                EpicTask.SubTask subTask = subTaskStorage.get(listOfSubTaskId.get(0));
                if (subTask != null) {
                    subTaskForStartTimeMin = subTaskStorage.get(listOfSubTaskId.get(0));
                    startTimeMin = subTaskForStartTimeMin.getStartTime();
                }
            }
            for (int i = 1; i < listOfSubTaskId.size(); i++) {
                EpicTask.SubTask subTask = subTaskStorage.get(listOfSubTaskId.get(i));
                if (subTask != null && startTimeMin != null) {
                    if (subTask.getStartTime().isBefore(startTimeMin)) {
                        startTimeMin = subTask.getStartTime();
                    }
                }
            }
            return startTimeMin;
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
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
                EpicTask.SubTask subTask = subTaskStorage.get(listOfSubTaskId.get(0));
                if (subTask != null) {
                    subTaskForStartTimeMax = subTaskStorage.get(listOfSubTaskId.get(0));
                    startTimeMax = subTaskForStartTimeMax.getEndTime();
                }
            }
            for (int i = 1; i < listOfSubTaskId.size(); i++) {
                EpicTask.SubTask subTask = subTaskStorage.get(listOfSubTaskId.get(i));
                if (subTask != null && startTimeMax != null) {
                    if (subTask.getEndTime().isAfter(startTimeMax)) {
                        startTimeMax = subTask.getEndTime();
                    }
                }
            }
            return startTimeMax;
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
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