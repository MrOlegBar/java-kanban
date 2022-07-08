package manager.test;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest {
    TaskManager manager = Managers.getDefault();

    @Override
    TaskManager createManager() {
        return manager;
    }

    @Test
    void addSubtaskToEpicTask() {
        super.addSubtaskToEpicTask();
    }

    @Test
    void getListOfSubTaskByEpicTaskId() {
        super.getListOfSubTaskByEpicTaskId();
    }

    @Test
    void getterEpicTaskStatus() {
        super.getterEpicTaskStatus();
    }
}
