package manager.test;

import manager.TaskManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static task.Task.Status.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T manager;
    abstract T createManager();
    Task firstTask;
    Task secondTask;
    List<Integer> listOfSubtaskIdOfTheFirstEpicTask;
    Task.Status statusOfTheFirstEpicTask;
    LocalDateTime startTimeOfTheFirstEpicTask;
    long durationOfTheFirstEpicTask;
    EpicTask epicTask1;
    EpicTask.SubTask subtask1;
    EpicTask.SubTask subtask2;
    EpicTask.SubTask subtask3;
    List<EpicTask.SubTask> listOfSubTaskByEpicTaskId;

    @BeforeEach
    private void BeforeEach() {

        manager = createManager();

        firstTask = manager.createTask(new Task(
                "Поесть"
                , "Принять пищу"
                , NEW
                , LocalDateTime.now().minusMinutes(30L)
                , 30L
        ));
        manager.saveTask(firstTask);

        secondTask = manager.createTask(new Task(
                "Поспать"
                , "Хорошенько выспаться"
                , DONE
                , LocalDateTime.now().plusMinutes(30L)
                , 600L
        ));
        manager.saveTask(secondTask);

        listOfSubtaskIdOfTheFirstEpicTask = new ArrayList<>();
        statusOfTheFirstEpicTask = manager.getterEpicTaskStatus(listOfSubtaskIdOfTheFirstEpicTask);
        startTimeOfTheFirstEpicTask = manager.getterEpicTaskStartTime(listOfSubtaskIdOfTheFirstEpicTask);
        durationOfTheFirstEpicTask = manager.getterEpicTaskDuration(listOfSubtaskIdOfTheFirstEpicTask);

        epicTask1 = manager.createTask(new EpicTask(
                "Закончить учебу"
                , "Получить сертификат обучения"
                , listOfSubtaskIdOfTheFirstEpicTask
                , statusOfTheFirstEpicTask
                , startTimeOfTheFirstEpicTask
                , durationOfTheFirstEpicTask
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

        subtask2 = manager.createTask(new EpicTask.SubTask(
                epicTask1.getId()
                , "Сдать дипломный проект"
                , "Сделать дипломный проект"
                , DONE
                , LocalDateTime.now().plusMinutes(150_630L)
                , 250_000L
        ));
        manager.saveSubTask(subtask2);

        subtask3 = manager.createTask(new EpicTask.SubTask(
                epicTask1.getId()
                , "Сдать 5й спринт"
                , "Сделать ТЗ"
                , NEW
                , LocalDateTime.now().plusMinutes(400_630L)
                , 4_320L
        ));
        manager.saveSubTask(subtask3);

        manager.addSubtaskToEpicTask(subtask1, epicTask1);
        manager.addSubtaskToEpicTask(subtask2, epicTask1);
        manager.addSubtaskToEpicTask(subtask3, epicTask1);

        manager.updateEpicTask(epicTask1);
    }

    @Test
    void addSubtaskToEpicTask() {

        //Проверка работы со стандартым поведением
        int expected = epicTask1.getId();
        int actual = subtask1.getEpicTaskId();

        assertEquals(expected, actual,"id Epic задачи не добавился в подзадачу");
        assertNotNull(epicTask1.getListOfSubTaskId(), "id подзадачи не добавился в список id подзадач Epic задачи");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "id Epic задачи не существует");
        assertNotNull(manager.getSubTaskById(subtask1.getId()), "id подзадачи не существует");
    }

    @Test
    void getListOfSubTaskByEpicTaskId() {

        //Проверка работы с пустым списком задач
        listOfSubTaskByEpicTaskId = manager.getListOfSubTaskByEpicTaskId(epicTask1.getId());

        assertNotNull(listOfSubTaskByEpicTaskId, "Список подзадач пуст");

        //Проверка работы со стандартым поведением
        int expected = 3;
        int actual = listOfSubTaskByEpicTaskId.size();

        assertEquals(expected, actual, "Подзадача не добавилась в список");


        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "id Epic задачи не существует");
    }

    /**
     * Для расчёта статуса Epic. Граничные условия:
     * a. Пустой список подзадач.
     * b. Все подзадачи со статусом NEW.
     * c. Все подзадачи со статусом DONE.
     * d. Подзадачи со статусами NEW и DONE.
     * e. Подзадачи со статусом IN_PROGRESS.
     */
    @Test
    void getterEpicTaskStatus() {

        //a. Проверка работы с пустым списком задач
        listOfSubTaskByEpicTaskId = manager.getListOfSubTaskByEpicTaskId(epicTask1.getId());

        assertNotNull(listOfSubTaskByEpicTaskId, "Список подзадач пуст");
        
        //d. Подзадачи со статусами NEW и DONE
        Task.Status actual1 = manager.getterEpicTaskStatus(epicTask1.getListOfSubTaskId());

        assertEquals(IN_PROGRESS, actual1, "Статус Epic задачи IN_PROGRESS рассчитан не правильно");

        //b. Все подзадачи со статусом NEW.
        subtask2.setStatus(NEW);

        Task.Status actual2 = manager.getterEpicTaskStatus(epicTask1.getListOfSubTaskId());

        assertEquals(NEW, actual2, "Статус Epic задачи NEW рассчитан не правильно");
        
        //c. Все подзадачи со статусом DONE.
        subtask1.setStatus(DONE);
        subtask2.setStatus(DONE);
        subtask3.setStatus(DONE);
        Task.Status actual3 = manager.getterEpicTaskStatus(epicTask1.getListOfSubTaskId());

        assertEquals(DONE, actual3, "Статус Epic задачи DONE рассчитан не правильно");

        //e. Подзадачи со статусом IN_PROGRESS
        subtask1.setStatus(IN_PROGRESS);
        subtask2.setStatus(IN_PROGRESS);
        subtask3.setStatus(IN_PROGRESS);
        Task.Status actual4 = manager.getterEpicTaskStatus(epicTask1.getListOfSubTaskId());

        assertEquals(IN_PROGRESS, actual4, "Статус Epic задачи IN_PROGRESS рассчитан не правильно");
    }
}