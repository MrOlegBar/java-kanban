package manager;

import exception.ManagerSaveException;

import java.io.File;
import java.net.URI;

public class HTTPTaskManager extends FileBackedTasksManager {
    HTTPTaskManager(URI url) {

    }

    public HTTPTaskManager(File file) throws ManagerSaveException {
        super(file);
    }
}
