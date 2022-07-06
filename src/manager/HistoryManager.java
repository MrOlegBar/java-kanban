package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {

    /**
     * Добавляет задачи в коллекцию истории задач
     */
    void addTaskToTaskHistory(Task task);

    /**
     * Возвращает список истории задач
     */
    List<Task> getTaskHistory();

    /**
     * Удаляет задачу по id из истории задач
     */
    void removeTaskFromTaskHistory(int id);
}