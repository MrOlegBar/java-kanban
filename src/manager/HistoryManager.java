package manager;

import task.EpicTask;
import task.Task;

import java.util.List;

public interface HistoryManager {

    /**
     * Помечает задачи как просмотренные
     */
    void add(Task task);

    void add(EpicTask.SubTask subTask);

    void add(EpicTask epicTask);

    /**
     * Проверка списка десяти последних задач
     */
    public void checkListOfTenRecentTasks();

    /**
     * История просмотров задач
     */
    List<Task> getHistory();


}
