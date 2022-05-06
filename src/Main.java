import epictask.EpicTask;
import manager.Manager;
import task.Task;
import java.util.ArrayList;
import java.util.Arrays;
import static manager.Manager.Status.*;
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
         * Создали 2е Task задачи
         */
        Task taskFirst = new Task("Поесть","Принять пищу", NEW);
        Task taskSecond = new Task("Поспать","Хорошенько выспаться", DONE);
        /**
         * Создали 1у EpicTask задачу с 2мя SubTask подзадачами
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
         * Создали 2ю EpicTask задачу с 1й SubTask подзадачей
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
         * 2. Методы для каждого из типа задач
         *  2.1 Получение списка всех задач
         */
        System.out.println("\n    2.1 Получение списка всех задач:");
        System.out.println(Arrays.toString(manager.getListOfTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfSubTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfEpicTasks().toArray()));
        /**
         *  2.3 Получение по идентификатору;
         */
        System.out.println("\n    2.3 Получение по идентификатору:");
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getSubTaskById(3));
        System.out.println(manager.getSubTaskById(4));
        System.out.println(manager.getEpicTaskById(5));
        System.out.println(manager.getSubTaskById(6));
        System.out.println(manager.getEpicTaskById(7));
        /**
         *  2.2 Удаление всех задач;
         */
        manager.deleteAllTasks();
        manager.deleteAllEpicTasks();
        manager.deleteAllSubTasks();

        System.out.println("\n    2.2 Удаление всех задач:");
        System.out.println(Arrays.toString(manager.getListOfTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfEpicTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfSubTasks().toArray()));
        /**
         *  2.4 Создание. Сам объект должен передаваться в качестве параметра;
         */
        System.out.println("\n    2.4 Создание. Сам объект должен передаваться в качестве параметра:");
        System.out.println(manager.createCopyOfTask(taskFirst));
        System.out.println(manager.createCopyOfTask(taskSecond));
        System.out.println(manager.createCopyOfSubTask(subtaskFirstEpicTaskFirst));
        System.out.println(manager.createCopyOfSubTask(subtaskSecondEpicTaskFirst));
        System.out.println(manager.createCopyOfEpicTask(epicTaskFirst));
        System.out.println(manager.createCopyOfSubTask(subtaskFirstEpicTaskSecond));
        System.out.println(manager.createCopyOfEpicTask(epicTaskSecond));
        /**
         *  2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра;
         *  Статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач
         */
        taskFirst.setStatus(DONE);
        manager.updateTask(1, taskFirst);

        subtaskFirstEpicTaskFirst.setStatus(DONE);
        manager.updateSubTask(3, subtaskFirstEpicTaskFirst);

        manager.addSubtaskToList(subtaskFirstEpicTaskFirst, subTasksEpicTaskFirst);
        epicTaskFirst.setSubTasks(subTasksEpicTaskFirst);
        manager.updateEpicTask(5, epicTaskFirst);

        System.out.println("\n    Статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам"
                + " подзадач");
        System.out.println("    2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде"
                + " параметра:");
        System.out.println(Arrays.toString(manager.getListOfTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfSubTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfEpicTasks().toArray()));
        /**
         *  2.6 Удаление по идентификатору.
         */
        manager.removeTaskById(8);
        manager.removeTaskById(9);
        manager.removeSubTaskById(10);
        manager.removeSubTaskById(11);
        manager.removeEpicTaskById(12);
        manager.removeSubTaskById(13);
        manager.removeEpicTaskById(14);

        System.out.println("\n    2.6 Удаление по идентификатору:");
        System.out.println(Arrays.toString(manager.getListOfTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfSubTasks().toArray()));
        System.out.println(Arrays.toString(manager.getListOfEpicTasks().toArray()));
        /**
         * 3. Дополнительные методы:
         *  3.1 Получение списка всех подзадач определённого эпика.
         */
        System.out.println("\n    3.1 Получение списка всех подзадач определённого эпика:");
        System.out.println(manager.getListOfSubTaskByEpicTask(5));
        System.out.println(manager.getListOfSubTaskByEpicTask(7));
    }
}