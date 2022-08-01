package manager;

import server.KVTaskClient;
import task.EpicTask;
import task.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Интерфейс для объекта-менеджера
 */
public interface TaskManager {

    /**
     * Добавляет id подзадачи в список id подзадач Epic задачи, а id Epic задачи в подзадачу
     */
    void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask);

    /**
     * Возвращает список всех подзадач Epic задачи по id
     */
    List<EpicTask.SubTask> getListOfSubTaskByEpicTaskId(int i);

    /**
     * Возвращает статус Epic задачи, рассчитанный на основе статусов подзадач
     */
    Task.Status getterEpicTaskStatus(List<Integer> listOfSubtaskIdOfTheFirstEpicTask);

    /**
     * Возвращает дату и время начала самой ранней подзадачи Epic задачи
     */
    LocalDateTime getterEpicTaskStartTime(List<Integer> listOfSubtaskIdOfTheFirstEpicTask);

    /**
     * Возвращает сумму продолжительностей всех подзадач Epic задач
     */
    long getterEpicTaskDuration(List<Integer> listOfSubtaskIdOfTheFirstEpicTask);

    /**
     * Возвращает дату и время конца самой поздней подзадачи Epic задачи
     */
    LocalDateTime getterEpicTaskEndTime(List<Integer> listOfSubTaskId);

    /**
     * Возвращает задачу Task после проверки на пересечение по времени выполнения с уже созданными задачами
     */
    Task createTask(Task task) throws IOException, InterruptedException;

    /**
     * Возвращает задачу EpicTask после проверки на пересечение по времени выполнения
     */
    EpicTask createTask(EpicTask epicTask);

    /**
     * Возвращает задачу SubTask после проверки на пересечение по времени выполнения
     */
    EpicTask.SubTask createTask(EpicTask.SubTask subTask);

    /**
     * Сохраняет задачу в коллекцию
     */
    void saveTask(Task task) throws IOException, InterruptedException;

    /**
     * Сохраняет Epic задачу в коллекцию
     */
    void saveEpicTask(EpicTask epicTask);

    /**
     * Сохраняет подзадачу в коллекцию
     */
    void saveSubTask(EpicTask.SubTask subTask);

    /**
     * Возвращает задачу по id и добавляет ее в историю задач
     */
    Task getTaskById(int id);

    /**
     * Возвращает Epic задачу по id и добавляет ее в историю задач
     */
    EpicTask getEpicTaskById(int id);

    /**
     * Возвращает подзадачу по id и добавляет ее в историю задач
     */
    EpicTask.SubTask getSubTaskById(int id);

    /**
     * Обновляет задачу после проверки на пересечение по времени выполнения
     */
    void updateTask(Task task);

    /**
     * Обновляет Epic задачу после проверки на пересечение по времени выполнения
     */
    void updateEpicTask(EpicTask epicTask);

    /**
     * Обновляет подзадачу после проверки на пересечение по времени выполнения
     */
    void updateSubTask(EpicTask.SubTask subTask);

    /**
     * Возвращает из коллекции список всех задач
     */
    List<Task> getListOfTasks();

    /**
     * Возвращает из коллекции список всех Epic задач
     */
    List<EpicTask> getListOfEpicTasks();

    /**
     * Возвращает из коллекции список всех подзадач
     */
    List<EpicTask.SubTask> getListOfSubTasks();

    /**
     * Геттер для метода, который возвращает список всех задач
     * , отсортированный по дате и времени начала самой ранней задачи
     */
    Set<Task> getterPrioritizedTasks();

    /**
     * Удаляет по id задачу из коллекции и истории задач
     */
    void removeTaskById(int id);

    /**
     * Удаляет по id  Epic задачу из коллекции и истории задач
     */
    void removeEpicTaskById(int id);

    /**
     * Удаляет по id подзадачу из коллекции и истории задач
     */
    void removeSubTaskById(int id);

    /**
     * Удаляет все задачи из коллекции и истории задач
     */
    void deleteAllTasks();

    /**
     * Удаляет все Epic задачи и связанные с ними подзадачи из коллекции и истории задач
     */
    void deleteAllEpicTasks();

    /**
     * Удаляет все подзадачи из коллекции и истории задач
     */
    void deleteAllSubTasks();

    /**
     * Возвращает список истории задач
     */
    List<Task> getListOfTaskHistory();

    /**
     * Удаляет по id задачу из просмотра
     */
    void removeTaskFromTaskHistory(int id);

    KVTaskClient getKVTaskClient();

    Integer toJson(String key) throws IOException, InterruptedException;
}
