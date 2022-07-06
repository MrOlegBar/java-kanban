package manager.test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task.Task.Status.DONE;
import static task.Task.Status.NEW;

class InMemoryHistoryManagerTest extends HistoryManagerTest {
    HistoryManager historyManager = new InMemoryHistoryManager();

    /**
     * Тесты для всех методов интерфейса
     * Граничные условия:
     * Пустая история задач.
     */
    @Test
    public void add() {
        Task task = new Task("Поесть", "Принять пищу", NEW, LocalDateTime.now().minusMinutes(30L)
                , 30L);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Пустая история задач");
        assertEquals(1, history.size(), "Пустая история задач");
    }

    /**
     * Тесты для всех методов интерфейса
     * Граничные условия:
     * Дублирование.
     */
    @Test
    void getHistory() {
        Task task = new Task("Поесть", "Принять пищу", NEW, LocalDateTime.now().minusMinutes(30L)
                , 30L);
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История задач дублируется.");
    }

    /**
     * Тесты для всех методов интерфейса
     * Граничные условия:
     * Удаление из истории: начало, середина, конец.
     */
    @Test
    void remove() {
        TaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Поесть", "Принять пищу", NEW, LocalDateTime.now().minusMinutes(30L)
                , 30L);
        Task task2 = new Task("Поспать", "Хорошенько выспаться", DONE
                , LocalDateTime.now().plusMinutes(30L), 600L);
        Task task3 = new Task("Сдать все спринты", "Вовремя выполнить ТЗ", NEW
                , LocalDateTime.now().plusMinutes(630L), 150_000L);

        manager.saveTask(task1);
        manager.saveTask(task2);
        manager.saveTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());
        historyManager.remove(task2.getId());
        historyManager.remove(task3.getId());

        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "Задачи не удаляются из истории задач");
    }
}