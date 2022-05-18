package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> listOfTenRecentTasks = new ArrayList<>(10);

    /**
     * История просмотров задач
     */
    public List<Task> getHistory() {
        return listOfTenRecentTasks;
    }

    /**
     * Помечает задачи как просмотренные
     */
    public void add(Task task) {
        if (listOfTenRecentTasks.size() == 10) {
            listOfTenRecentTasks.remove(0);
        }
        listOfTenRecentTasks.add(task);
    }
}
