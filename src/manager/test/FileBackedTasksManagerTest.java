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

    @Test
    void getTaskById() {
        super.getTaskById();
    }

    @Test
    void getEpicTaskById() {
        super.getEpicTaskById();
    }

    @Test
    void getSubTaskById() {
        super.getSubTaskById();
    }

    @Test
    void updateTask() {
        super.updateTask();
    }

    @Test
    void updateEpicTask() {
        super.updateEpicTask();
    }

    @Test
    void updateSubTask() {
        super.updateSubTask();
    }

    @Test
    void getListOfTasks() {
        super.getListOfTasks();
    }

    @Test
    void getListOfEpicTasks() {
        super.getListOfEpicTasks();
    }

    @Test
    void getListOfSubTasks() {
        super.getListOfSubTasks();
    }
}
