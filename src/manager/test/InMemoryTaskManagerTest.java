package manager.test;

import manager.Managers;
import manager.TaskManager;

import java.io.IOException;

class InMemoryTaskManagerTest extends TaskManagerTest {
    TaskManager manager = Managers.getDefaultInMemoryTaskManager();

    protected InMemoryTaskManagerTest() throws IOException {
    }

    @Override
    TaskManager createManager() {
        return manager;
    }
}
