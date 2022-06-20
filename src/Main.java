import exception.FileException;
import manager.*;
import task.EpicTask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static task.Task.Status.DONE;
import static task.Task.Status.NEW;

/**
 * «Трекер задач»
 */
public class Main {

    public static void main(String[] args) throws IOException, FileException {

        /**
         * Тестирование
         */
        //TaskManager manager = Managers.getDefault();

        /**
         * Создали 2е Task задачи
         */
        /*Task firstTask = new Task("Поесть", "Принять пищу", NEW);
        manager.saveTask(firstTask);

        Task secondTask = new Task("Поспать", "Хорошенько выспаться", DONE);
        manager.saveTask(secondTask);*/

        /**
         * Создали 1у EpicTask задачу с 3мя SubTask подзадачами
         */
        /*ArrayList<Integer> listOfSubtaskIdOfTheFirstEpicTask = new ArrayList<>();
        Task.Status statusOfTheFirstEpicTask = manager.getterEpicTaskStatus(listOfSubtaskIdOfTheFirstEpicTask);
        EpicTask firstEpicTask = new EpicTask("Закончить учебу",
                "Получить сертификат обучения", listOfSubtaskIdOfTheFirstEpicTask
                , statusOfTheFirstEpicTask);
        manager.saveEpicTask(firstEpicTask);

        EpicTask.SubTask firstSubtaskOfTheFirstEpicTask = new EpicTask.SubTask(firstEpicTask.getId()
                , "Сдать все спринты"
                , "Вовремя выполнить ТЗ", NEW);
        manager.saveSubTask(firstSubtaskOfTheFirstEpicTask);

        EpicTask.SubTask secondSubtaskOfTheFirstEpicTask = new EpicTask.SubTask(firstEpicTask.getId()
                , "Сдать дипломный проект"
                , "Сделать дипломный проект", DONE);
        manager.saveSubTask(secondSubtaskOfTheFirstEpicTask);

        EpicTask.SubTask thirdSubtaskOfTheFirstEpicTask = new EpicTask.SubTask(firstEpicTask.getId()
                , "Сдать 5й спринт"
                , "Сделать ТЗ", NEW);
        manager.saveSubTask(thirdSubtaskOfTheFirstEpicTask);

        manager.addSubtaskToEpicTask(firstSubtaskOfTheFirstEpicTask, firstEpicTask);
        manager.addSubtaskToEpicTask(secondSubtaskOfTheFirstEpicTask, firstEpicTask);
        manager.addSubtaskToEpicTask(thirdSubtaskOfTheFirstEpicTask, firstEpicTask);
        manager.updateEpicTask(firstEpicTask);*/

        /**
         * Создали 2ю EpicTask задачу без SubTask подзадач
         */
        /*ArrayList<Integer> listOfSubtaskIdOfTheSecondEpicTask = new ArrayList<>();
        Task.Status statusOfTheSecondEpicTask = manager.getterEpicTaskStatus(listOfSubtaskIdOfTheSecondEpicTask);
        EpicTask secondEpicTask = new EpicTask("Сменить работу"
                , "Начать работать Java разработчиком", listOfSubtaskIdOfTheSecondEpicTask
                , statusOfTheSecondEpicTask);
        manager.saveEpicTask(secondEpicTask);*/

        /**
         * Возможность хранить задачи всех типов
         * Получение списка всех задач
         */
        /*System.out.println("\n    Возможность хранить задачи всех типов:");
        System.out.println("    Получение списка всех задач:");
        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpicTasks());
        System.out.println(manager.getListOfSubTasks());*/

        /**
         * Получение по идентификатору
         * История просмотров задач
         * Удаление задачи, которая есть в истории
         * Удаление эпика с 3мя подзадачамии
         */
        /*System.out.println("\n    -Получение по идентификатору:");
        System.out.println(manager.getTaskById(1));
        System.out.println("    -История просмотров задач:");
        System.out.println(manager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(manager.getTaskById(2));
        System.out.println("    -История просмотров задач:");
        System.out.println(manager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(manager.getEpicTaskById(3));
        System.out.println("    -История просмотров задач:");
        System.out.println(manager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(manager.getSubTaskById(4));
        System.out.println("    -История просмотров задач:");
        System.out.println(manager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(manager.getSubTaskById(5));
        System.out.println("    -История просмотров задач:");
        System.out.println(manager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(manager.getSubTaskById(6));
        System.out.println("    -История просмотров задач:");
        System.out.println(manager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(manager.getEpicTaskById(7));
        System.out.println("    -История просмотров задач:");
        System.out.println(manager.getHistory());*/

        /**
         * Удаление задачи, которая есть в истории;
         */
        /*System.out.println("    -Удаление задачи, которая есть в истории:");
        manager.remove(4);
        System.out.println("    -История просмотров задач:");
        System.out.println(manager.getHistory());*/

        /**
         * Удаление эпика с 3мя подзадачамии;
         */
        /*System.out.println("    -Удаление эпика с 3мя подзадачамии:");
        manager.removeEpicTaskById(3);
        System.out.println("    -История просмотров задач:");
        System.out.println(manager.getHistory());*/

        /**
         * Удаление всех задач;
         */
        /*System.out.println("\n    Удаление всех задач:");
        manager.deleteAllTasks();
        System.out.println(manager.getHistory());
        System.out.println(manager.getListOfTasks());
        manager.deleteAllEpicTasks();
        System.out.println(manager.getHistory());
        System.out.println(manager.getListOfEpicTasks());
        manager.deleteAllSubTasks();
        System.out.println(manager.getHistory());
        System.out.println(manager.getListOfSubTasks());*/

        /**
         * Создание. Сам объект должен передаваться в качестве параметра;
         */
        /*System.out.println("\n    Создание. Сам объект должен передаваться в качестве параметра:");
        Task newFirstTask = manager.createCopyOfTask(firstTask);
        manager.saveTask(newFirstTask);
        Task newSecondTask = manager.createCopyOfTask(secondTask);
        manager.saveTask(newSecondTask);

        System.out.println(newFirstTask);
        System.out.println(newSecondTask);

        EpicTask newFirstEpicTask = manager.createCopyOfTask(firstEpicTask);
        manager.saveEpicTask(newFirstEpicTask);

        EpicTask.SubTask newFirstSubtaskOfTheFirstEpicTask =
                manager.createCopyOfTask(firstSubtaskOfTheFirstEpicTask);
        manager.saveSubTask(newFirstSubtaskOfTheFirstEpicTask);
        manager.addSubtaskToEpicTask(newFirstSubtaskOfTheFirstEpicTask, newFirstEpicTask);

        EpicTask.SubTask newSecondSubtaskOfTheFirstEpicTask =
                manager.createCopyOfTask(secondSubtaskOfTheFirstEpicTask);
        manager.saveSubTask(newSecondSubtaskOfTheFirstEpicTask);
        manager.addSubtaskToEpicTask(newSecondSubtaskOfTheFirstEpicTask, newFirstEpicTask);
        manager.updateEpicTask(newFirstEpicTask);

        System.out.println(newFirstEpicTask);
        System.out.println(newFirstSubtaskOfTheFirstEpicTask);
        System.out.println(newSecondSubtaskOfTheFirstEpicTask);

        EpicTask newSecondEpicTask = manager.createCopyOfTask(secondEpicTask);
        manager.saveEpicTask(newSecondEpicTask);

        EpicTask.SubTask newFirstSubtaskOfTheSecondEpicTask =
                manager.createCopyOfTask(firstSubtaskOfTheFirstEpicTask);
        manager.saveSubTask(newFirstSubtaskOfTheSecondEpicTask);
        manager.addSubtaskToEpicTask(newFirstSubtaskOfTheSecondEpicTask, newSecondEpicTask);
        manager.updateEpicTask(newSecondEpicTask);

        System.out.println(newSecondEpicTask);
        System.out.println(newFirstSubtaskOfTheSecondEpicTask);*/

        /**
         * Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра;
         */
        /*newFirstTask.setStatus(DONE);
        manager.updateTask(newFirstTask);

        newFirstSubtaskOfTheFirstEpicTask.setStatus(DONE);
        manager.updateSubTask(newFirstSubtaskOfTheFirstEpicTask);
        manager.updateEpicTask(newFirstEpicTask);

        System.out.println("\n    Обновление. Новая версия объекта с верным идентификатором передаются в виде"
                + " параметра:");
        System.out.println(manager.getListOfTasks());
        System.out.println(manager.getListOfEpicTasks());
        System.out.println(manager.getListOfSubTasks());*/

        /**
         * Получение списка всех подзадач определённого эпика.
         */
        /*System.out.println("\n    Получение списка всех подзадач определённого эпика:");
        System.out.println(manager.getListOfSubTaskByEpicTaskId(10));
        System.out.println(manager.getListOfSubTaskByEpicTaskId(13));*/

        /**
         * Удаление по идентификатору.
         */
        /*System.out.println("\n    Удаление по идентификатору:");
        manager.removeTaskById(8);
        System.out.println(manager.getHistory());
        System.out.println(manager.getListOfTasks());
        manager.removeTaskById(9);
        System.out.println(manager.getHistory());
        System.out.println(manager.getListOfTasks());

        manager.removeEpicTaskById(10);
        System.out.println(manager.getHistory());
        System.out.println(manager.getListOfEpicTasks());

        manager.removeSubTaskById(14);
        System.out.println(manager.getHistory());
        System.out.println(manager.getListOfSubTasks());
        manager.removeEpicTaskById(13);
        System.out.println(manager.getHistory());
        System.out.println(manager.getListOfSubTasks());*/
    }
}