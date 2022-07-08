package manager.test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.TaskManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task.Task.Status.DONE;
import static task.Task.Status.NEW;

abstract class HistoryManagerTest {
    HistoryManager historyManager;
    TaskManager manager;
    Task task1;
    Task task2;
    List<Task> history;
    List<Integer> listOfSubtaskIdOfEpicTask1;
    Task.Status statusOfEpicTask1;
    LocalDateTime startTimeOFEpicTask1;
    long durationOfEpicTask1;
    EpicTask epicTask1;
    EpicTask.SubTask subtask1;

    @BeforeEach
    private void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        manager = new InMemoryTaskManager();

        task1 = new Task(
                "Поесть"
                , "Принять пищу"
                , NEW
                , LocalDateTime.now().minusMinutes(30L)
                , 30L
        );
        manager.saveTask(task1);

        task2 = new Task(
                "Поспать"
                , "Хорошенько выспаться"
                , DONE
                , LocalDateTime.now().plusMinutes(30L)
                , 600L
        );
        manager.saveTask(task2);

        listOfSubtaskIdOfEpicTask1 = new ArrayList<>();
        statusOfEpicTask1 = manager.getterEpicTaskStatus(listOfSubtaskIdOfEpicTask1);
        startTimeOFEpicTask1 = manager.getterEpicTaskStartTime(listOfSubtaskIdOfEpicTask1);
        durationOfEpicTask1 = manager.getterEpicTaskDuration(listOfSubtaskIdOfEpicTask1);

        epicTask1 = manager.createTask(new EpicTask(
                "Закончить учебу"
                , "Получить сертификат обучения"
                , listOfSubtaskIdOfEpicTask1
                , statusOfEpicTask1
                , startTimeOFEpicTask1
                , durationOfEpicTask1
        ));
        manager.saveEpicTask(epicTask1);

        subtask1 = manager.createTask(new EpicTask.SubTask(
                epicTask1.getId()
                , "Сдать все спринты"
                , "Вовремя выполнить ТЗ"
                , NEW
                , LocalDateTime.now().plusMinutes(630L)
                , 150_000L
        ));
        manager.saveSubTask(subtask1);

        manager.addSubtaskToEpicTask(subtask1, epicTask1);
        manager.updateEpicTask(epicTask1);

        historyManager.addTaskToTaskHistory(task1);
        historyManager.addTaskToTaskHistory(task1);
        historyManager.addTaskToTaskHistory(epicTask1);
        historyManager.addTaskToTaskHistory(subtask1);

        history = historyManager.getTaskHistory();
    }
    /**
     * Граничные условия:
     * a. Пустая история задач.
     * b. Дублирование.
     * с. Удаление из истории: начало, середина, конец.
     */
    @Test
    void addTaskToTaskHistory() {

        //a. Пустая история задач
        assertNotNull(history, "Пустая история задач");

        //b. Дублирование
        int expected = 3;
        int actual = history.size();

        assertEquals(expected, actual, "История задач дублируется");
    }

    @Test
    void getTaskHistory() {

        //a. Пустая история задач
        assertNotNull(history, "Пустая история задач");

        //b. Дублирование
        int expected = 3;
        int actual = history.size();

        assertEquals(expected, actual, "История задач дублируется.");

        //с. Удаление из истории: начало, середина, конец
        historyManager.removeTaskFromTaskHistory(task1.getId());
        historyManager.removeTaskFromTaskHistory(epicTask1.getId());
        historyManager.removeTaskFromTaskHistory(subtask1.getId());

        history = historyManager.getTaskHistory();

        assertTrue(history.isEmpty(), "Задачи не удаляются из истории задач");
    }

    @Test
    void removeTaskFromTaskHistory() {

        //a. Пустая история задач
        assertNotNull(history, "Пустая история задач");

        //b. Дублирование
        int expected = 3;
        int actual = history.size();

        assertEquals(expected, actual, "История задач дублируется.");

        //с. Удаление из истории: начало, середина, конец
        historyManager.removeTaskFromTaskHistory(task1.getId());
        historyManager.removeTaskFromTaskHistory(epicTask1.getId());
        historyManager.removeTaskFromTaskHistory(subtask1.getId());

        history = historyManager.getTaskHistory();

        assertTrue(history.isEmpty(), "Задачи не удаляются из истории задач");
    }
}
