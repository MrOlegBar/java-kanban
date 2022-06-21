package manager;

import task.Task;

import java.util.List;

public interface HistoryManager<T extends Task> {

    /**
     * Помечает задачи как просмотренные
     */
    void add(T e);

    /**
     * История просмотров задач
     */
    List<T> getHistory();

    /**
     * Удаляет задачи из просмотра
     */
    void remove(int id);
}