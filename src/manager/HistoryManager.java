package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getListOfTenRecentTasks();

    /**
     * Помечает задачи как просмотренные
     */
    void add(Task task);

    /**
     * История просмотров задач
     */
    List<Task> getHistory();
}
