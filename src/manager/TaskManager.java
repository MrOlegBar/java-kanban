package manager;

import task.EpicTask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Интерфейс для объекта-менеджера
 */
public interface TaskManager {
    /**
     * 1. Метод для сохранения задач
     */
    void saveToTaskStorage(Task task);

    void saveToTaskStorage(EpicTask epicTask);

    void saveToTaskStorage(EpicTask.SubTask subTask);

    /**
     * 2.1 Получение списка всех задач
     */
    ArrayList<Task> getListOfTasks();

    ArrayList<EpicTask> getListOfEpicTasks();

    ArrayList<EpicTask.SubTask> getListOfSubTasks();

    /**
     * 2.2 Удаление всех задач
     */
    void deleteAllTasks();

    void deleteAllEpicTasks();

    /**
     * 2.3 Получение задачи по идентификатору
     */
    Task getTaskById(int id);

    EpicTask getEpicTaskById(int id);

    EpicTask.SubTask getSubTaskById(int id);

    /**
     * 2.4 Создание задачи
     */
    Task createCopyOfTask(Task task);

    EpicTask createCopyOfTask(EpicTask epicTask);

    EpicTask.SubTask createCopyOfTask(EpicTask.SubTask subTask);

    /**
     * 2.5 Обновление задачи
     */
    void updateTask(Task task);

    void updateTask(EpicTask epicTask);

    void updateTask(EpicTask.SubTask subTask);

    /**
     * 2.6 Удаление задачи
     */
    void removeTaskById(int id);

    void removeEpicTaskById(int id);

    void removeSubTaskById(int id);
    /**
     * История просмотров задач
     */
    List<Task> getHistory();
}
