import epictask.EpicTask;
import manager.Manager;
import task.Task;

import java.util.ArrayList;
import java.util.Arrays;

import static manager.Manager.Status.DONE;
import static manager.Manager.Status.NEW;

/**
 * «Трекер задач»
 */
public class Main {
    public static void main(String[] args) {
    /**
     * Тестирование
     */
        Manager manager = new Manager();
        /**
         * Создали 2е Task.Task задачи
         */
        Task taskFirst = new Task("Поесть","Принять пищу", NEW);
        Task taskSecond = new Task("Поспать","Хорошенько выспаться", DONE);
        /**
         * Создали 1у epicTask.EpicTask задачу с 2мя SubTask подзадачами
         */
        ArrayList<EpicTask.SubTask> subTasksEpicTaskFirst = new ArrayList<>();
        EpicTask.SubTask subtaskFirstEpicTaskFirst = new EpicTask.SubTask("Закончить учебу",
                "Сдать все спринты", "Вовремя выполнить ТЗ", NEW);
        EpicTask.SubTask subtaskSecondEpicTaskFirst = new EpicTask.SubTask("Закончить учебу",
                "Сдать дипломный проект", "Сделать дипломный проект", DONE);




        manager.addSubtaskToList(subtaskFirstEpicTaskFirst, subTasksEpicTaskFirst);
        manager.addSubtaskToList(subtaskSecondEpicTaskFirst, subTasksEpicTaskFirst);
        EpicTask epicTaskFirst = new EpicTask("Закончить учебу",
                "Получить сертификат обучения", subTasksEpicTaskFirst);
        /**
         * Создали 2ю epicTask.EpicTask задачу с 1й SubTask подзадачей
         */
        EpicTask.SubTask subtaskFirstEpicTaskSecond = new EpicTask.SubTask("Сменить работу",
                "Закончить курс по Java","Научиться программировать на языке Java",
                NEW);
        ArrayList<EpicTask.SubTask> subTasksEpicTaskSecond = new ArrayList<>();

        manager.addSubtaskToList(subtaskFirstEpicTaskSecond, subTasksEpicTaskSecond);

        EpicTask epicTaskSecond = new EpicTask("Сменить работу"
                ,"Начать работать Java разработчиком", subTasksEpicTaskSecond);
        /**
         * 1. Возможность хранить задачи всех типов
         */
        manager.saveToTaskStorage(taskFirst);
        manager.saveToTaskStorage(taskSecond);
        manager.saveToSubTaskStorage(subtaskFirstEpicTaskFirst);
        manager.saveToSubTaskStorage(subtaskSecondEpicTaskFirst);
        manager.saveToEpicTaskStorage(epicTaskFirst);
        manager.saveToSubTaskStorage(subtaskFirstEpicTaskSecond);
        manager.saveToEpicTaskStorage(epicTaskSecond);

        System.out.println("\n    1. Возможность хранить задачи всех типов:");
        System.out.println(manager.getTaskStorage());
        System.out.println(manager.getSubTaskStorage());
        System.out.println(manager.getEpicTaskStorage());
        /**
         * 0. Статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач
         */
        subtaskFirstEpicTaskFirst.setStatus(DONE);
        System.out.println("\n    0. Статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам "
                + "подзадач:");
        System.out.println(manager.getListOfSubTasks());
        System.out.println(manager.getListOfEpicTasks());
        /**
         * 2. Методы для каждого из типа задач(Task.Task/epicTask.EpicTask/SubTask):
         *  2.1 Получение списка всех задач;
         */
        System.out.println("\n    2.1 Получение списка всех задач:");
        System.out.println(Arrays.toString(manager.getListOfTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfEpicTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfSubTasks().toArray()));
        /**
         *  2.2 Удаление всех задач;
         */
        manager.deleteAllEpicTasks();

        System.out.println("\n    2.2 Удаление всех задач:");
        System.out.println(Arrays.toString(manager.getListOfTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfEpicTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfSubTasks().toArray()));
        /**
         *  2.3 Получение по идентификатору;
         */
        System.out.println("\n    2.3 Получение по идентификатору:");
        System.out.println(manager.getTaskById(0));
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getSubTaskById(2));
        System.out.println(manager.getSubTaskById(3));
        System.out.println(manager.getEpicTaskById(4));
        System.out.println(manager.getEpicTaskById(5));
        /**
         *  2.4 Создание. Сам объект должен передаваться в качестве параметра;
         */
        System.out.println("\n    2.4 Создание. Сам объект должен передаваться в качестве параметра:");
        System.out.println(manager.createCopyOfTask(taskFirst));
        System.out.println(manager.createCopyOfEpicTask(epicTaskFirst));
        System.out.println(manager.createCopyOfSubTask(subtaskFirstEpicTaskFirst));
        /**
         *  2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра;
         */
        manager.updateEpicTask(5, epicTaskFirst);
        manager.updateEpicTask(7, epicTaskSecond);

        System.out.println("\n    2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде"
                + " параметра:");
        System.out.println(Arrays.toString(manager.getListOfEpicTasks().toArray()));
        /**
         *  2.6 Удаление по идентификатору.
         */
        manager.removeTaskById(1);
        manager.removeTaskById(2);

        System.out.println("\n    2.6 Удаление по идентификатору:");
        System.out.println(Arrays.toString(manager.getListOfTasks().toArray()));
        /**
         * 3. Дополнительные методы:
         *  3.1 Получение списка всех подзадач определённого эпика.
         */
        System.out.println("\n    3.1 Получение списка всех подзадач определённого эпика:");
        System.out.println(manager.getListOfSubTaskByEpicTask(1));
        System.out.println(manager.getListOfSubTaskByEpicTask(2));
    }
}