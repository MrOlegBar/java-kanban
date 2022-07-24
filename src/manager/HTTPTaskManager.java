package manager;

import exception.ManagerSaveException;
import server.KVTaskClient;
import task.EpicTask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class HTTPTaskManager extends FileBackedTasksManager {
    URI url;

    HTTPTaskManager(URI url) throws IOException, InterruptedException {
        this.url = url;
        KVTaskClient KVTaskClient = new KVTaskClient(url);
    }

    public HTTPTaskManager(File file) throws ManagerSaveException {
        super(file);
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
    }

    @Override
    public void saveEpicTask(EpicTask epicTask) {
        super.saveEpicTask(epicTask);
    }

    @Override
    public void saveSubTask(EpicTask.SubTask subTask) throws ManagerSaveException {
        super.saveSubTask(subTask);
    }
}
