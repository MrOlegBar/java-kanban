package managers.tests;

import managers.FileBackedTasksManager;
import managers.TaskManager;

import java.io.File;

class FileBackedTasksManagerTest extends TaskManagerTest {
    TaskManager manager = new FileBackedTasksManager(new File("Autosave.csv"));

    protected FileBackedTasksManagerTest() {
    }

    @Override
    TaskManager createManager() {
        return manager;
    }

}
