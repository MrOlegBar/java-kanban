import manager.Manager;
import task.EpicTask;
import task.Task;

import java.util.ArrayList;

import static task.Task.Status.DONE;
import static task.Task.Status.NEW;

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
        Task firstTask = new Task("Поесть", "Принять пищу", NEW);
        manager.saveToTaskStorage(firstTask);

        Task secondTask = new Task("Поспать", "Хорошенько выспаться", DONE);
        manager.saveToTaskStorage(secondTask);
        /**
         * Создали 1у EpicTask задачу с 2мя SubTask подзадачами
         */
        ArrayList<Integer> listOfSubtaskIdOfTheFirstEpicTask = new ArrayList<>();
        Task.Status statusOfTheFirstEpicTask = manager.getEpicTaskStatus(listOfSubtaskIdOfTheFirstEpicTask);
        EpicTask firstEpicTask = new EpicTask("Закончить учебу",
                "Получить сертификат обучения", listOfSubtaskIdOfTheFirstEpicTask
                , statusOfTheFirstEpicTask);
        manager.saveToEpicTaskStorage(firstEpicTask);

        EpicTask.SubTask firstSubtaskOfTheFirstEpicTask = new EpicTask.SubTask(firstEpicTask.getId()
                , "Сдать все спринты"
                , "Вовремя выполнить ТЗ", NEW);
        manager.saveToSubTaskStorage(firstSubtaskOfTheFirstEpicTask);

        EpicTask.SubTask secondSubtaskOfTheFirstEpicTask = new EpicTask.SubTask(firstEpicTask.getId()
                , "Сдать дипломный проект"
                , "Сделать дипломный проект", DONE);
        manager.saveToSubTaskStorage(secondSubtaskOfTheFirstEpicTask);

        manager.addSubtaskToEpicTask(firstSubtaskOfTheFirstEpicTask, firstEpicTask);
        manager.addSubtaskToEpicTask(secondSubtaskOfTheFirstEpicTask, firstEpicTask);
        manager.updateEpicTask(firstEpicTask);
        /**
         * Создали 2ю EpicTask задачу с 1й SubTask подзадачей
         */
        ArrayList<Integer> listOfSubtaskIdOfTheSecondEpicTask = new ArrayList<>();
        Task.Status statusOfTheSecondEpicTask = manager.getEpicTaskStatus(listOfSubtaskIdOfTheSecondEpicTask);
        EpicTask secondEpicTask = new EpicTask("Сменить работу"
                , "Начать работать Java разработчиком", listOfSubtaskIdOfTheSecondEpicTask
                , statusOfTheSecondEpicTask);
        manager.saveToEpicTaskStorage(secondEpicTask);

        EpicTask.SubTask firstSubtaskOfTheSecondEpicTask = new EpicTask.SubTask(secondEpicTask.getId()
                , "Закончить курс по Java"
                , "Научиться программировать на языке Java", NEW);
        manager.saveToSubTaskStorage(firstSubtaskOfTheSecondEpicTask);

        manager.addSubtaskToEpicTask(firstSubtaskOfTheSecondEpicTask, secondEpicTask);
        manager.updateEpicTask(secondEpicTask);
        /**
         * 1. Возможность хранить задачи всех типов
         * 2.1 Получение списка всех задач
         */
        System.out.println("\n    1. Возможность хранить задачи всех типов:");
        System.out.println("    2.1 Получение списка всех задач:");
        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpicTasks());
        System.out.println(manager.getListOfSubTasks());
        /**
         *  2.3 Получение по идентификатору;
         */
        System.out.println("\n    2.3 Получение по идентификатору:");
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getEpicTaskById(3));
        System.out.println(manager.getSubTaskById(4));
        System.out.println(manager.getSubTaskById(5));
        System.out.println(manager.getEpicTaskById(6));
        System.out.println(manager.getSubTaskById(7));
        /**
         *  2.2 Удаление всех задач;
         */
        manager.deleteAllTasks();
        manager.deleteAllEpicTasks();

        System.out.println("\n    2.2 Удаление всех задач:");
        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpicTasks());
        System.out.println(manager.getListOfSubTasks());
        /**
         *  2.4 Создание. Сам объект должен передаваться в качестве параметра;
         */
        System.out.println("\n    2.4 Создание. Сам объект должен передаваться в качестве параметра:");
        Task newFirstTask = manager.createCopyOfTask(firstTask);
        manager.saveToTaskStorage(newFirstTask);
        Task newSecondTask = manager.createCopyOfTask(secondTask);
        manager.saveToTaskStorage(newSecondTask);

        System.out.println(newFirstTask);
        System.out.println(newSecondTask);

        EpicTask newFirstEpicTask = manager.createCopyOfEpicTask(firstEpicTask);
        manager.saveToEpicTaskStorage(newFirstEpicTask);

        EpicTask.SubTask newFirstSubtaskOfTheFirstEpicTask =
                manager.createCopyOfSubTask(firstSubtaskOfTheFirstEpicTask);
        manager.saveToSubTaskStorage(newFirstSubtaskOfTheFirstEpicTask);
        manager.addSubtaskToEpicTask(newFirstSubtaskOfTheFirstEpicTask, newFirstEpicTask);

        EpicTask.SubTask newSecondSubtaskOfTheFirstEpicTask =
                manager.createCopyOfSubTask(secondSubtaskOfTheFirstEpicTask);
        manager.saveToSubTaskStorage(newSecondSubtaskOfTheFirstEpicTask);
        manager.addSubtaskToEpicTask(newSecondSubtaskOfTheFirstEpicTask, newFirstEpicTask);
        manager.updateEpicTask(newFirstEpicTask);

        System.out.println(newFirstEpicTask);
        System.out.println(newFirstSubtaskOfTheFirstEpicTask);
        System.out.println(newSecondSubtaskOfTheFirstEpicTask);

        EpicTask newSecondEpicTask = manager.createCopyOfEpicTask(secondEpicTask);
        manager.saveToEpicTaskStorage(newSecondEpicTask);

        EpicTask.SubTask newFirstSubtaskOfTheSecondEpicTask =
                manager.createCopyOfSubTask(firstSubtaskOfTheSecondEpicTask);
        manager.saveToSubTaskStorage(newFirstSubtaskOfTheSecondEpicTask);
        manager.addSubtaskToEpicTask(newFirstSubtaskOfTheSecondEpicTask, newSecondEpicTask);
        manager.updateEpicTask(newSecondEpicTask);

        System.out.println(newSecondEpicTask);
        System.out.println(newFirstSubtaskOfTheSecondEpicTask);
        /**
         *  2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра;
         */
        newFirstTask.setStatus(DONE);
        manager.updateTask(newFirstTask);

        newFirstSubtaskOfTheFirstEpicTask.setStatus(DONE);
        manager.updateSubTask(newFirstSubtaskOfTheFirstEpicTask);
        manager.updateEpicTask(newFirstEpicTask);

        System.out.println("\n    2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде"
                + " параметра:");
        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpicTasks());
        System.out.println(manager.getListOfSubTasks());
        /**
         *  3.1 Получение списка всех подзадач определённого эпика.
         */
        System.out.println("\n    3.1 Получение списка всех подзадач определённого эпика:");
        System.out.println(manager.getListOfSubTaskByEpicTaskId(10));
        System.out.println(manager.getListOfSubTaskByEpicTaskId(13));
        /**
         *  2.6 Удаление по идентификатору.
         */
        manager.removeTaskById(8);
        manager.removeTaskById(9);
        manager.removeEpicTaskById(10);
        manager.removeSubTaskById(11);
        manager.removeSubTaskById(12);
        manager.removeEpicTaskById(13);
        manager.removeSubTaskById(14);

        System.out.println("\n    2.6 Удаление по идентификатору:");
        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpicTasks());
        System.out.println(manager.getListOfSubTasks());
    }
}