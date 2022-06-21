package manager;

import task.EpicTask;
import task.Task;

import java.util.List;

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
    Task createCopyOfTask(Task task);

    EpicTask createCopyOfTask(EpicTask epicTask);

    EpicTask.SubTask createCopyOfTask(EpicTask.SubTask subTask);

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