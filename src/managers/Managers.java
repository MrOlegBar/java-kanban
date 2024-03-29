package managers;

import java.io.IOException;

public class Managers {

    public static TaskManager getDefaultInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultManager(String key) throws IOException {
        return HTTPTaskManager.fromJson(key);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
