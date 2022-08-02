package manager.test;

import manager.Managers;
import manager.TaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest {
    TaskManager manager = Managers.getDefaultInMemoryTaskManager();

    protected InMemoryTaskManagerTest() {
    }

    @Override
    TaskManager createManager() {
        return manager;
    }
}
