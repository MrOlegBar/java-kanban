package manager.test;

import manager.FileBackedTasksManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static task.Task.Status.NEW;

public abstract class TaskManagerTest {
    TaskManager manager;

    @BeforeEach
    private void BeforeEach() {
        manager = new FileBackedTasksManager(new File("Autosave.csv"));
    }

    @Test
    void addSubtaskToEpicTask() {
        List<Integer> listOfSubtaskIdOfTheFirstEpicTask = new ArrayList<>();
        Task.Status statusOfTheFirstEpicTask = manager.getterEpicTaskStatus(listOfSubtaskIdOfTheFirstEpicTask);
        LocalDateTime startTimeOfTheFirstEpicTask = manager.getterEpicTaskStartTime(listOfSubtaskIdOfTheFirstEpicTask);
        long durationOfTheFirstEpicTask = manager.getterEpicTaskDuration(listOfSubtaskIdOfTheFirstEpicTask);

        EpicTask epicTask = manager.createTask(new EpicTask(
                "Закончить учебу"
                , "Получить сертификат обучения"
                , listOfSubtaskIdOfTheFirstEpicTask
                , statusOfTheFirstEpicTask
                , startTimeOfTheFirstEpicTask
                , durationOfTheFirstEpicTask
        ));

        EpicTask.SubTask subTask = manager.createTask(new EpicTask.SubTask(
                epicTask.getId()
                , "Сдать все спринты"
                , "Вовремя выполнить ТЗ"
                , NEW
                , LocalDateTime.now().plusMinutes(630L)
                , 150_000L
        ));

        manager.saveEpicTask(epicTask);
        manager.saveSubTask(subTask);
        manager.addSubtaskToEpicTask(subTask, epicTask);
        manager.updateEpicTask(epicTask);

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask.getId()), "id Epic задачи не существует");
        assertNotNull(manager.getSubTaskById(subTask.getId()), "id подзадачи не существует");
        //Проверка работы со стандартым поведением
        assertNotNull(epicTask.getListOfSubTaskId(), "id подзадачи не добавился в список id подзадач Epic задачи");
        assertEquals(epicTask.getId(), subTask.getEpicTaskId(), "id Epic задачи не добавился в подзадачу");
    }

    /*@Test
    void getListOfSubTaskByEpicTaskId() {
    }

    @Test
    void getterEpicTaskStatus() {
    }

    @Test
    void getterEpicTaskStartTime() {
    }

    @Test
    void getterEpicTaskDuration() {
    }

    @Test
    void getterEpicTaskEndTime() {
    }

    @Test
    void createTask() {
    }

    @Test
    void testCreateTask() {
    }

    @Test
    void testCreateTask1() {
    }

    @Test
    void saveTask() {
    }

    @Test
    void saveEpicTask() {
    }

    @Test
    void saveSubTask() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void getEpicTaskById() {
    }

    @Test
    void getSubTaskById() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateEpicTask() {
    }

    @Test
    void updateSubTask() {
    }

    @Test
    void getListOfTasks() {
    }

    @Test
    void getListOfEpicTasks() {
    }

    @Test
    void getListOfSubTasks() {
    }

    @Test
    void removeTaskById() {
    }

    @Test
    void removeEpicTaskById() {
    }

    @Test
    void removeSubTaskById() {
    }

    @Test
    void deleteAllTasks() {
    }

    @Test
    void deleteAllEpicTasks() {
    }

    @Test
    void deleteAllSubTasks() {
    }

    @Test
    void getTaskHistory() {
    }

    @Test
    void removeTaskFromTaskHistory() {
    }

    @Test
    void getterPrioritizedTasks() {
    }*/
}