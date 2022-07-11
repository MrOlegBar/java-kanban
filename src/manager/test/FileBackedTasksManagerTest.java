package manager.test;

import manager.FileBackedTasksManager;
import manager.TaskManager;

import java.io.File;

class FileBackedTasksManagerTest extends TaskManagerTest {
    TaskManager manager = new FileBackedTasksManager(new File("Autosave.csv"));

    @Override
    TaskManager createManager() {
        return manager;
    }

}
