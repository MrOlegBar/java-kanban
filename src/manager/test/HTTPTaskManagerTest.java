package manager.test;

import manager.HTTPTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static manager.Managers.getDefaultManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HTTPTaskManagerTest extends TaskManagerTest {
    String key = "key1";
    TaskManager manager = new HTTPTaskManager(URI.create("http://localhost:8081/"));

    HTTPTaskManagerTest() throws IOException, InterruptedException {
    }

    @Override
    TaskManager createManager() {
        return manager;
    }

    @Test
    void toJson() throws IOException, InterruptedException {
        //a. Проверка работы с пустым списком задач
        listOfTasks = manager.getListOfTasks();
        assertNotNull(listOfTasks, "Список задач пуст");
        listOfEpicTasks = manager.getListOfEpicTasks();
        assertNotNull(listOfEpicTasks, "Список Epic задач пуст");
        listOfSubTasks = manager.getListOfSubTasks();
        assertNotNull(listOfSubTasks, "Список подзадач пуст");
        listOfTaskHistory = manager.getListOfTaskHistory();
        assertNotNull(listOfTaskHistory, "Список истории задач пуст");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getTaskById(task1.getId()), "ID задачи 1 не существует");
        assertNotNull(manager.getTaskById(task2.getId()), "ID задачи 2 не существует");
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "ID Epic задачи 1 не существует");
        assertNotNull(manager.getEpicTaskById(epicTask2.getId()), "ID Epic задачи 2 не существует");
        assertNotNull(manager.getSubTaskById(subTask1.getId()), "ID подзадачи 1 не существует");
        assertNotNull(manager.getSubTaskById(subTask2.getId()), "ID подзадачи 2 не существует");
        assertNotNull(manager.getSubTaskById(subTask3.getId()), "ID подзадачи 3 не существует");

        //Проверка работы со стандартым поведением
        statusCode = manager.toJson(key);
        assertEquals(200, statusCode, "Значение для ключа " + key + " не обновлено");

    }

    @Test
    void fromJson() throws IOException, InterruptedException {
        statusCode = manager.toJson(key);

        TaskManager managerActual = getDefaultManager(key);

        //a. Проверка работы с пустым списком задач
        List<Task> listOfTasksActual = managerActual.getListOfTasks();
        assertNotNull(listOfTasksActual, "Список задач не восстановился");
        List<EpicTask> listOfEpicTasksActual = managerActual.getListOfEpicTasks();
        assertNotNull(listOfEpicTasksActual, "Список Epic задач не восстановился");
        List<EpicTask.SubTask> listOfSubTasksActual = managerActual.getListOfSubTasks();
        assertNotNull(listOfSubTasksActual, "Список подзадач не восстановился");
        List<Task> listOfTaskHistoryActual = managerActual.getListOfTaskHistory();
        assertNotNull(listOfTaskHistoryActual, "Список истории задач пуст");


        //Проверка работы со стандартым поведением и с несуществующем идентификатором
        assertEquals(manager.getTaskById(task1.getId()), managerActual.getTaskById(task1.getId()), "Задача " + task1 + " восстановилась не правильно");
        assertEquals(manager.getTaskById(task2.getId()), managerActual.getTaskById(task2.getId()), "Задача " + task2 + " восстановилась не правильно");
        assertEquals(manager.getEpicTaskById(epicTask1.getId()), managerActual.getEpicTaskById(epicTask1.getId()), "Задача " + epicTask1 + " восстановилась не правильно");
        assertEquals(manager.getEpicTaskById(epicTask2.getId()), managerActual.getEpicTaskById(epicTask2.getId()), "Задача " + epicTask2 + " восстановилась не правильно");
        assertEquals(manager.getSubTaskById(subTask1.getId()), managerActual.getSubTaskById(subTask1.getId()), "Задача " + subTask1 + " восстановилась не правильно");
        assertEquals(manager.getSubTaskById(subTask2.getId()), managerActual.getSubTaskById(subTask2.getId()), "Задача " + subTask2 + " восстановилась не правильно");
        assertEquals(manager.getSubTaskById(subTask3.getId()), managerActual.getSubTaskById(subTask3.getId()), "Задача " + subTask3 + " восстановилась не правильно");
    }
}