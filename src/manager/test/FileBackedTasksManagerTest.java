package manager.test;

import manager.FileBackedTasksManager;
import manager.TaskManager;

import java.io.File;
import java.io.IOException;

class FileBackedTasksManagerTest extends TaskManagerTest {
    TaskManager manager = new FileBackedTasksManager(new File("Autosave.csv"));

    protected FileBackedTasksManagerTest() throws IOException {
    }

    @Override
    TaskManager createManager() {
        return manager;
    }

}
