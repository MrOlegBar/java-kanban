package manager;

import task.EpicTask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public static final List<Task> listOfTenRecentTasks = new ArrayList<>(10);

    /**
     * Проверка списка десяти последних задач
     */
    public void checkListOfTenRecentTasks() {
        int realArrayLength = listOfTenRecentTasks.toArray().length;
        if (realArrayLength == 10) {
            listOfTenRecentTasks.remove(0);
            Task[] temporaryListOfRecentTasks = listOfTenRecentTasks.toArray(new Task[realArrayLength - 1]);
            listOfTenRecentTasks.clear();
            listOfTenRecentTasks.addAll(0, List.of(temporaryListOfRecentTasks));
        }
    }

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
        task.setStatus(Task.Status.DONE);
    }

    public void add(EpicTask.SubTask subTask) {
        subTask.setStatus(Task.Status.DONE);
    }

    public void add(EpicTask epicTask) {
        epicTask.setStatus(Task.Status.DONE);
    }
}
