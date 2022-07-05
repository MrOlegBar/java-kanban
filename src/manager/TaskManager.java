package manager;

import task.EpicTask;
import task.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Интерфейс для объекта-менеджера
 */
public interface TaskManager {

    /**
     * Метод для добавления подзадач в список
     */
    void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask);

    /**
     * Метод для управления статусом для EpicTask задач
     */
    Task.Status getterEpicTaskStatus(List<Integer> listOfSubtaskIdOfTheFirstEpicTask);

    /**
     * Метод для управления датой, когда предполагается закончить выполнение задачи
     */
    LocalDateTime getterEpicTaskStartTime(List<Integer> listOfSubtaskIdOfTheFirstEpicTask);

    /**
     * Метод для управления продолжительностью задачи, оценка того, сколько времени она займёт в минутах (число)
     */
    long getterEpicTaskDuration(List<Integer> listOfSubtaskIdOfTheFirstEpicTask);

    /**
     * Метод для возвращения списка задач и подзадач в заданном порядке
     */
    Set<Task> getterPrioritizedTasks();

    /**
     * Метод для сохранения задач
     */
    void saveTask(Task task);

    void saveEpicTask(EpicTask epicTask);

    void saveSubTask(EpicTask.SubTask subTask);

    /**
     * Получение списка всех задач
     */
    List<Task> getListOfTasks();

    List<EpicTask> getListOfEpicTasks();

    List<EpicTask.SubTask> getListOfSubTasks();

    /**
     * Удаление всех задач
     */
    void deleteAllTasks();

    void deleteAllEpicTasks();

    void deleteAllSubTasks();

    /**
     * Получение задачи по идентификатору
     */
    Task getTaskById(int id);

    EpicTask getEpicTaskById(int id);

    EpicTask.SubTask getSubTaskById(int id);

    /**
     * Создание задачи
     */
    Task createTask(Task task);

    EpicTask createTask(EpicTask epicTask);

    EpicTask.SubTask createTask(EpicTask.SubTask subTask);

    /**
     * Обновление задачи
     */
    void updateTask(Task task);

    void updateEpicTask(EpicTask epicTask);

    void updateSubTask(EpicTask.SubTask subTask);

    /**
     * Удаление задачи
     */
    void removeTaskById(int id);

    void removeEpicTaskById(int id);

    void removeSubTaskById(int id);

    /**
     * Получение списка всех подзадач определённого эпика
     */
    List<EpicTask.SubTask> getListOfSubTaskByEpicTaskId(int i);

    /**
     * История просмотров задач
     */
    List<Task> getHistory();

    /**
     * Удаляет задачи из просмотра
     */
    void remove(int id);
}