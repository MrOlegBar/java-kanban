package managers.tests;

import managers.Managers;
import managers.TaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest {
    TaskManager manager = Managers.getDefaultInMemoryTaskManager();

    protected InMemoryTaskManagerTest() {
    }

    @Override
    TaskManager createManager() {
        return manager;
    }
}
