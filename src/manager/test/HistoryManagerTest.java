package manager.test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static task.Task.Status.DONE;
import static task.Task.Status.NEW;

public abstract class HistoryManagerTest {
    HistoryManager historyManager;
    TaskManager manager;
    Task task1;
    Task task2;
    Task task3;
    List<Task> history;

    @BeforeEach
    private void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        manager = new InMemoryTaskManager();

        task1 = new Task("Поесть", "Принять пищу", NEW, LocalDateTime.now().minusMinutes(30L)
                , 30L);
        task2 = new Task("Поспать", "Хорошенько выспаться", DONE
                , LocalDateTime.now().plusMinutes(30L), 600L);
        task3 = new Task("Сдать все спринты", "Вовремя выполнить ТЗ", NEW
                , LocalDateTime.now().plusMinutes(630L), 150_000L);

        manager.saveTask(task1);
        manager.saveTask(task2);
        manager.saveTask(task3);

        historyManager.addTaskToTaskHistory(task1);
        historyManager.addTaskToTaskHistory(task1);
        historyManager.addTaskToTaskHistory(task2);
        historyManager.addTaskToTaskHistory(task3);

        history = historyManager.getTaskHistory();
    }

    @Test
    public void add() {

        //Проверка работы с пустой историей задач
        assertNotNull(history, "Пустая история задач");

        //Проверка работы с дублированием задач в истории задач
        assertEquals(3, history.size(), "История задач дублируется");
    }

    @Test
    public void getTaskHistory() {

        //Проверка работы с пустой историей задач
        assertNotNull(history, "Пустая история задач");

        //Проверка работы с дублированием задач в истории задач
        assertEquals(3, history.size(), "История задач дублируется.");

        historyManager.removeTaskFromTaskHistory(task1.getId());
        historyManager.removeTaskFromTaskHistory(task2.getId());
        historyManager.removeTaskFromTaskHistory(task3.getId());

        history = historyManager.getTaskHistory();

        //Проверка работы с дублированием задач в истории задач
        assertEquals(0, history.size(), "Задачи не удаляются из истории задач");
    }

    @Test
    public void removeTaskFromTaskHistory() {

        //Проверка работы с пустой историей задач
        assertNotNull(history, "Пустая история задач");

        //Проверка работы с дублированием задач в истории задач
        assertEquals(3, history.size(), "История задач дублируется.");

        historyManager.removeTaskFromTaskHistory(task1.getId());
        historyManager.removeTaskFromTaskHistory(task2.getId());
        historyManager.removeTaskFromTaskHistory(task3.getId());

        history = historyManager.getTaskHistory();

        //Проверка работы с удалением из истории: начало, середина, конец.
        assertEquals(0, history.size(), "Задачи не удаляются из истории задач");
    }
}
