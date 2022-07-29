package manager.test;

import manager.Managers;
import manager.TaskManager;
import java.io.IOException;

class HTTPTaskManagerTest extends TaskManagerTest {
    String key = "key1";
    TaskManager manager = Managers.getDefaultManager(key);

    HTTPTaskManagerTest() throws IOException {
    }

    @Override
    TaskManager createManager() {
        return manager;
    }
}