package manager;

import task.Task;

import java.util.List;

public interface HistoryManager<E extends Task> {

    /**
     * Помечает задачи как просмотренные
     */
    void add(E e);

    /**
     * История просмотров задач
     */
    List<E> getHistory();

    void remove(int id);
}