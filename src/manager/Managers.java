package manager;

import exception.FileException;
import task.Task;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() throws FileException {
        return new FileBackedTasksManager(new File("Autosave.csv"));
    }

    /*public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }*/

    public static HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
