package manager;

import exception.FileException;
import task.Task;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
