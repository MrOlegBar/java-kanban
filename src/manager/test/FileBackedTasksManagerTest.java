package manager.test;

import manager.FileBackedTasksManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;

class FileBackedTasksManagerTest extends TaskManagerTest {
    TaskManager manager = new FileBackedTasksManager(new File("Autosave.csv"));

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

    @Test
    void getterEpicTaskStartTime() {
        super.getterEpicTaskStartTime();
    }

    @Test
    void getterEpicTaskDuration() {
        super.getterEpicTaskDuration();
    }

    @Test
    void getterEpicTaskEndTime() {
        super.getterEpicTaskEndTime();
    }

    @Test
    void createTask() {
        super.createTask();
    }

    @Test
    void saveTask() {
        super.saveTask();
    }

    @Test
    void saveEpicTask() {
        super.saveEpicTask();
    }

    @Test
    void saveSubTask() {
        super.saveSubTask();
    }
}
