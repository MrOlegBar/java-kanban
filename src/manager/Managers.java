package manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefaultInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager(new File("Autosave.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
