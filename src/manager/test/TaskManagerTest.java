package manager.test;

import manager.TaskManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.EpicTask;
import task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static task.Task.Status.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T manager;
    abstract T createManager();
    Task task1;
    Task task2;
    List<Integer> listOfSubtaskIdOfEpicTask1;
    Task.Status statusOfEpicTask1;
    LocalDateTime startTimeOfEpicTask1;
    long durationOfEpicTask1;
    EpicTask epicTask1;
    EpicTask.SubTask subTask1;
    EpicTask.SubTask subTask2;
    EpicTask.SubTask subTask3;
    List<Integer> listOfSubtaskIdOfEpicTask2;
    Task.Status statusOfEpicTask2;
    LocalDateTime startTimeOfEpicTask2;
    long durationOfEpicTask2;
    EpicTask epicTask2;
    List<EpicTask.SubTask> listOfSubTaskByEpicTask;
    List<Task> listOfTasks;
    List<EpicTask> listOfEpicTasks;
    List<EpicTask.SubTask> listOfSubTasks;

    @BeforeEach
    private void BeforeEach() {

        manager = createManager();

        task1 = manager.createTask(new Task(
                "Поесть"
                , "Принять пищу"
                , NEW
                , LocalDateTime.of(2022, 2, 26, 22, 22)
                , 30L
        ));
        manager.saveTask(task1);

        task2 = manager.createTask(new Task(
                "Поспать"
                , "Хорошенько выспаться"
                , DONE
                , LocalDateTime.now().plusMinutes(30L)
                , 600L
        ));
        manager.saveTask(task2);

        listOfSubtaskIdOfEpicTask1 = new ArrayList<>();
        statusOfEpicTask1 = manager.getterEpicTaskStatus(listOfSubtaskIdOfEpicTask1);
        startTimeOfEpicTask1 = manager.getterEpicTaskStartTime(listOfSubtaskIdOfEpicTask1);
        durationOfEpicTask1 = manager.getterEpicTaskDuration(listOfSubtaskIdOfEpicTask1);

        epicTask1 = manager.createTask(new EpicTask(
                "Закончить учебу"
                , "Получить сертификат обучения"
                , listOfSubtaskIdOfEpicTask1
                , statusOfEpicTask1
                , startTimeOfEpicTask1
                , durationOfEpicTask1
        ));
        manager.saveEpicTask(epicTask1);

        subTask1 = manager.createTask(new EpicTask.SubTask(
                epicTask1.getId()
                , "Сдать все спринты"
                , "Вовремя выполнить ТЗ"
                , NEW
                , LocalDateTime.of(2022, 2, 27, 22, 22)
                , 150_000L
        ));
        manager.saveSubTask(subTask1);

        subTask2 = manager.createTask(new EpicTask.SubTask(
                epicTask1.getId()
                , "Сдать дипломный проект"
                , "Сделать дипломный проект"
                , DONE
                , LocalDateTime.now().plusMinutes(150_630L)
                , 250_000L
        ));
        manager.saveSubTask(subTask2);

        subTask3 = manager.createTask(new EpicTask.SubTask(
                epicTask1.getId()
                , "Сдать 5й спринт"
                , "Сделать ТЗ"
                , NEW
                , LocalDateTime.of(2222, 2, 2, 22, 22)
                , 4_320L
        ));
        manager.saveSubTask(subTask3);

        manager.addSubtaskToEpicTask(subTask1, epicTask1);
        manager.addSubtaskToEpicTask(subTask2, epicTask1);
        manager.addSubtaskToEpicTask(subTask3, epicTask1);

        manager.updateEpicTask(epicTask1);

        listOfSubtaskIdOfEpicTask2 = new ArrayList<>();
        statusOfEpicTask2 = manager.getterEpicTaskStatus(listOfSubtaskIdOfEpicTask2);
        startTimeOfEpicTask2 = manager.getterEpicTaskStartTime(listOfSubtaskIdOfEpicTask2);
        durationOfEpicTask2 = manager.getterEpicTaskDuration(listOfSubtaskIdOfEpicTask2);

        epicTask2 = manager.createTask(new EpicTask(
                "Сменить работу"
                , "Начать работать Java разработчиком"
                , listOfSubtaskIdOfEpicTask2
                , statusOfEpicTask2
                , startTimeOfEpicTask2
                , durationOfEpicTask2));

        manager.saveEpicTask(epicTask2);
    }

    @Test
    void addSubtaskToEpicTask() {

        //Проверка работы со стандартым поведением
        int expected = epicTask1.getId();
        int actual = subTask1.getEpicTaskId();

        assertEquals(expected, actual,"id Epic задачи не добавился в подзадачу");
        assertNotNull(epicTask1.getListOfSubTaskId(), "id подзадачи не добавился в список id подзадач Epic задачи");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "id Epic задачи не существует");
        assertNotNull(manager.getSubTaskById(subTask1.getId()), "id подзадачи не существует");
    }

    @Test
    void getListOfSubTaskByEpicTaskId() {

        //Проверка работы с пустым списком задач
        listOfSubTaskByEpicTask = manager.getListOfSubTaskByEpicTaskId(epicTask1.getId());

        assertNotNull(listOfSubTaskByEpicTask, "Список подзадач пуст");

        //Проверка работы со стандартым поведением
        int expected = 3;
        int actual = listOfSubTaskByEpicTask.size();

        assertEquals(expected, actual, "Подзадача не добавилась в список");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "id Epic задачи не существует");
    }

    @Test
    void getterEpicTaskStatus() {

        //a. Проверка работы с пустым списком задач
        listOfSubTaskByEpicTask = manager.getListOfSubTaskByEpicTaskId(epicTask1.getId());

        assertNotNull(listOfSubTaskByEpicTask, "Список подзадач пуст");
        
        //d. Подзадачи со статусами NEW и DONE
        Task.Status actual1 = manager.getterEpicTaskStatus(epicTask1.getListOfSubTaskId());

        assertEquals(IN_PROGRESS, actual1, "Статус Epic задачи IN_PROGRESS рассчитан не правильно");

        //b. Все подзадачи со статусом NEW.
        subTask2.setStatus(NEW);

        Task.Status actual2 = manager.getterEpicTaskStatus(epicTask1.getListOfSubTaskId());

        assertEquals(NEW, actual2, "Статус Epic задачи NEW рассчитан не правильно");
        
        //c. Все подзадачи со статусом DONE.
        subTask1.setStatus(DONE);
        subTask2.setStatus(DONE);
        subTask3.setStatus(DONE);
        Task.Status actual3 = manager.getterEpicTaskStatus(epicTask1.getListOfSubTaskId());

        assertEquals(DONE, actual3, "Статус Epic задачи DONE рассчитан не правильно");

        //e. Подзадачи со статусом IN_PROGRESS
        subTask1.setStatus(IN_PROGRESS);
        subTask2.setStatus(IN_PROGRESS);
        subTask3.setStatus(IN_PROGRESS);
        Task.Status actual4 = manager.getterEpicTaskStatus(epicTask1.getListOfSubTaskId());

        assertEquals(IN_PROGRESS, actual4, "Статус Epic задачи IN_PROGRESS рассчитан не правильно");
    }

    @Test
    void getterEpicTaskStartTime() {

        //a. Проверка работы с пустым списком задач
        listOfSubTaskByEpicTask = manager.getListOfSubTaskByEpicTaskId(epicTask1.getId());

        assertNotNull(listOfSubTaskByEpicTask, "Список подзадач пуст");

        //Проверка работы со стандартым поведением
        LocalDateTime expected = LocalDateTime.of(2022, 2, 2, 22, 22);
        LocalDateTime actual = manager.getterEpicTaskStartTime(epicTask1.getListOfSubTaskId());

        assertEquals(expected, actual, "Дату и время начала самой ранней подзадачи рассчитаны не правильно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "ID Epic задачи не существует");
    }

    @Test
    void getterEpicTaskDuration() {

        //a. Проверка работы с пустым списком задач
        listOfSubTaskByEpicTask = manager.getListOfSubTaskByEpicTaskId(epicTask1.getId());

        assertNotNull(listOfSubTaskByEpicTask, "Список подзадач пуст");

        //Проверка работы со стандартым поведением
        long expected = 404_320L;
        long actual = manager.getterEpicTaskDuration(epicTask1.getListOfSubTaskId());

        assertEquals(expected, actual, "Сумма продолжительностей всех подзадач рассчитана не правильно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "ID Epic задачи не существует");
    }

    @Test
    void getterEpicTaskEndTime() {

        //a. Проверка работы с пустым списком задач
        listOfSubTaskByEpicTask = manager.getListOfSubTaskByEpicTaskId(epicTask1.getId());

        assertNotNull(listOfSubTaskByEpicTask, "Список подзадач пуст");

        //Проверка работы со стандартым поведением
        LocalDateTime expected = LocalDateTime.of(2222, 2, 5, 22, 22);
        LocalDateTime actual = manager.getterEpicTaskEndTime(epicTask1.getListOfSubTaskId());

        assertEquals(expected, actual, "Дату и время начала самой поздней подзадачи рассчитаны не правильно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "ID Epic задачи не существует");
    }

    @Test
    void createTask() {

        //a. Проверка работы с пустым списком задач
        listOfTasks = manager.getListOfTasks();
        listOfEpicTasks = manager.getListOfEpicTasks();
        listOfSubTasks = manager.getListOfSubTasks();

        assertNotNull(listOfTasks, "Список задач пуст");
        assertNotNull(listOfEpicTasks, "Список Epic задач пуст");
        assertNotNull(listOfSubTasks, "Список подзадач пуст");

        //Проверка работы со стандартым поведением
        Task expected1 = new Task(
                1,
                "Поесть"
                , "Принять пищу"
                , NEW
                , LocalDateTime.of(2022, 2, 26, 22, 22)
                , 30L
        );
        Task actual1 = task1;

        assertEquals(expected1, actual1,"Задача создана не верно");

        EpicTask expected2 = new EpicTask(
                3,
                "Закончить учебу"
                , "Получить сертификат обучения"
                , new ArrayList<>(List.of(4, 5, 6))
                , IN_PROGRESS
                , LocalDateTime.of(2022, 2, 2, 22, 22)
                , 404_320L
        );
        EpicTask actual2 = epicTask1;

        assertEquals(expected2, actual2,"Epic Задача создана не верно");

        EpicTask.SubTask expected3 = new EpicTask.SubTask(
                4,
                3
                , "Сдать все спринты"
                , "Вовремя выполнить ТЗ"
                , NEW
                , LocalDateTime.of(2022, 2, 2, 22, 22)
                , 150_000L
        );
        EpicTask.SubTask actual3 = subTask1;

        assertEquals(expected3, actual3,"Подадача создана не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getTaskById(task1.getId()), "id задачи не существует");
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "id Epic задачи не существует");
        assertNotNull(manager.getSubTaskById(subTask1.getId()), "id подзадачи не существует");
    }

    @Test
    void saveTask() {

        //a. Проверка работы с пустым списком задач
        listOfTasks = manager.getListOfTasks();

        assertNotNull(listOfTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        Task expected = new Task(
                1,
                "Поесть"
                , "Принять пищу"
                , NEW
                , LocalDateTime.of(2022, 2, 26, 22, 22)
                , 30L
        );
        Task actual = manager.getTaskById(1);

        assertEquals(expected, actual,"Задача сохранена не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getTaskById(task1.getId()), "id задачи не существует");
    }

    @Test
    void saveEpicTask() {

        //a. Проверка работы с пустым списком задач
        listOfEpicTasks = manager.getListOfEpicTasks();

        assertNotNull(listOfEpicTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        EpicTask expected = new EpicTask(
                3,
                "Закончить учебу"
                , "Получить сертификат обучения"
                , new ArrayList<>(List.of(4, 5, 6))
                , IN_PROGRESS
                , LocalDateTime.of(2022, 2, 2, 22, 22)
                , 404_320L
        );
        EpicTask actual = manager.getEpicTaskById(epicTask1.getId());

        assertEquals(expected, actual,"Epic Задача сохранена не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "id  Epic задачи не существует");
    }

    @Test
    void saveSubTask() {

        //a. Проверка работы с пустым списком задач
        listOfSubTasks = manager.getListOfSubTasks();

        assertNotNull(listOfSubTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        EpicTask.SubTask expected = new EpicTask.SubTask(
                4,
                3
                , "Сдать все спринты"
                , "Вовремя выполнить ТЗ"
                , NEW
                , LocalDateTime.of(2022, 2, 2, 22, 22)
                , 150_000L
        );
        EpicTask.SubTask actual = manager.getSubTaskById(subTask1.getId());

        assertEquals(expected, actual,"Подзадача сохранена не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getSubTaskById(subTask1.getId()), "id  подзадачи не существует");
    }

    @Test
    void getTaskById() {

        //a. Проверка работы с пустым списком задач
        listOfTasks = manager.getListOfTasks();

        assertNotNull(listOfTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        Task expected = new Task(
                1,
                "Поесть"
                , "Принять пищу"
                , NEW
                , LocalDateTime.of(2022, 2, 26, 22, 22)
                , 30L
        );
        Task actual = manager.getTaskById(1);

        assertEquals(expected, actual,"Задача получена не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getTaskById(task1.getId()), "id задачи не существует");
    }

    @Test
    void getEpicTaskById() {

        //a. Проверка работы с пустым списком задач
        listOfEpicTasks = manager.getListOfEpicTasks();

        assertNotNull(listOfEpicTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        EpicTask expected = new EpicTask(
                3,
                "Закончить учебу"
                , "Получить сертификат обучения"
                , new ArrayList<>(List.of(4, 5, 6))
                , IN_PROGRESS
                , LocalDateTime.of(2022, 2, 2, 22, 22)
                , 404_320L
        );
        EpicTask actual = manager.getEpicTaskById(epicTask1.getId());

        assertEquals(expected, actual,"Epic Задача получена не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "id  Epic задачи не существует");
    }

    @Test
    void getSubTaskById() {

        //a. Проверка работы с пустым списком задач
        listOfSubTasks = manager.getListOfSubTasks();

        assertNotNull(listOfSubTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        EpicTask.SubTask expected = new EpicTask.SubTask(
                4,
                3
                , "Сдать все спринты"
                , "Вовремя выполнить ТЗ"
                , NEW
                , LocalDateTime.of(2022, 2, 2, 22, 22)
                , 150_000L
        );
        EpicTask.SubTask actual = manager.getSubTaskById(subTask1.getId());

        assertEquals(expected, actual,"Подзадача получена не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getSubTaskById(subTask1.getId()), "id  подзадачи не существует");
    }

    @Test
    void updateTask() {

        //a. Проверка работы с пустым списком задач
        listOfTasks = manager.getListOfTasks();

        assertNotNull(listOfTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        Task expected = new Task(
                1,
                "Перестать есть"
                , "Не принимать пищу"
                , DONE
                , LocalDateTime.of(3033, 3, 30, 13, 33)
                , 33L
        );

        manager.updateTask(expected);
        Task actual = manager.getTaskById(1);

        assertEquals(expected, actual,"Задача обновлена не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getTaskById(task1.getId()), "id задачи не существует");
    }

    @Test
    void updateEpicTask() {

        //a. Проверка работы с пустым списком задач
        listOfEpicTasks = manager.getListOfEpicTasks();

        assertNotNull(listOfEpicTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        EpicTask expected = new EpicTask(
                3,
                "Начать учебу"
                , "Научиться программированию"
                , new ArrayList<>(List.of(4, 5, 6))
                , NEW
                , LocalDateTime.of(3033, 3, 30, 13, 33)
                , 33L
        );

        manager.updateEpicTask(expected);
        EpicTask actual = manager.getEpicTaskById(epicTask1.getId());

        assertEquals(expected, actual,"Epic Задача обновлена не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "id  Epic задачи не существует");
    }

    @Test
    void updateSubTask() {

        //a. Проверка работы с пустым списком задач
        listOfSubTasks = manager.getListOfSubTasks();

        assertNotNull(listOfSubTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        EpicTask.SubTask expected = new EpicTask.SubTask(
                4,
                33
                , "Получить оффер"
                , "Поменять работу"
                , DONE
                , LocalDateTime.of(3033, 3, 30, 13, 33)
                , 33L
        );

        manager.updateSubTask(expected);
        EpicTask.SubTask actual = manager.getSubTaskById(subTask1.getId());

        assertEquals(expected, actual,"Подзадача обновлена не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getSubTaskById(subTask1.getId()), "id  подзадачи не существует");
    }

    @Test
    void getListOfTasks() {

        //a. Проверка работы с пустым списком задач
        listOfTasks = manager.getListOfTasks();

        assertNotNull(listOfTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        List<Task> expected = new ArrayList<>(List.of(task1, task2));
        List<Task> actual = manager.getListOfTasks();

        assertEquals(expected, actual,"Список задач получен не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getTaskById(task1.getId()), "id задачи 1 не существует");
        assertNotNull(manager.getTaskById(task2.getId()), "id задачи 2 не существует");
    }

    @Test
    void getListOfEpicTasks() {

        //a. Проверка работы с пустым списком задач
        listOfEpicTasks = manager.getListOfEpicTasks();

        assertNotNull(listOfEpicTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        List<EpicTask> expected = new ArrayList<>(List.of(epicTask1, epicTask2));
        List<EpicTask> actual = manager.getListOfEpicTasks();

        assertEquals(expected, actual,"Список Epic задач получен не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "id  Epic 1 задачи не существует");
        assertNotNull(manager.getEpicTaskById(epicTask2.getId()), "id  Epic 2 задачи не существует");
    }

    @Test
    void getListOfSubTasks() {

        //a. Проверка работы с пустым списком задач
        listOfSubTasks = manager.getListOfSubTasks();

        assertNotNull(listOfSubTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        List<EpicTask.SubTask> expected = new ArrayList<>(List.of(subTask1, subTask2, subTask3));
        List<EpicTask.SubTask> actual = manager.getListOfSubTasks();

        assertEquals(expected, actual,"Список подзадач получен не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getSubTaskById(subTask1.getId()), "id  подзадачи 1 не существует");
        assertNotNull(manager.getSubTaskById(subTask2.getId()), "id  подзадачи 2 не существует");
        assertNotNull(manager.getSubTaskById(subTask3.getId()), "id  подзадачи 3 не существует");
    }

    @Test
    void getPrioritizedTasks() {

        //a. Проверка работы с пустым списком задач
        listOfTasks = manager.getListOfTasks();
        listOfEpicTasks = manager.getListOfEpicTasks();
        listOfSubTasks = manager.getListOfSubTasks();

        assertNotNull(listOfTasks, "Список задач пуст");
        assertNotNull(listOfEpicTasks, "Список задач пуст");
        assertNotNull(listOfSubTasks, "Список задач пуст");

        //Проверка работы со стандартым поведением
        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpicTasks());
        System.out.println(manager.getListOfSubTasks());
        System.out.println(manager.getterPrioritizedTasks());
        Set<Task> expected = new TreeSet<>(Set.of(epicTask1, task1, task2, subTask2, subTask3, epicTask2));
        Set<Task> actual = manager.getterPrioritizedTasks();

        assertEquals(expected, actual,"Список задач получен не верно");

        //Проверка работы с несуществующем идентификатором
        assertNotNull(manager.getTaskById(task1.getId()), "id задачи 1 не существует");
        assertNotNull(manager.getTaskById(task2.getId()), "id задачи 2 не существует");
        assertNotNull(manager.getEpicTaskById(epicTask1.getId()), "id  Epic 1 задачи не существует");
        assertNotNull(manager.getEpicTaskById(epicTask2.getId()), "id  Epic 2 задачи не существует");
        assertNotNull(manager.getSubTaskById(subTask1.getId()), "id  подзадачи 1 не существует");
        assertNotNull(manager.getSubTaskById(subTask2.getId()), "id  подзадачи 2 не существует");
        assertNotNull(manager.getSubTaskById(subTask3.getId()), "id  подзадачи 3 не существует");
    }
}