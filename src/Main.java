import java.util.ArrayList;
import java.util.Arrays;
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
        Task taskFirst = new Task("Поесть","Принять пищу", "NEW");
        Task taskSecond = new Task("Поспать","Хорошенько выспаться", "DONE");
/**
 * Создали 1у EpicTask задачу с 2мя SubTask подзадачами
 */
        ArrayList<EpicTask.SubTask> subTasksEpicTaskFirst = new ArrayList<>();
        EpicTask.SubTask subtaskFirstEpicTaskFirst = new EpicTask.SubTask("Закончить учебу",
                "Сдать все спринты", "Вовремя выполнить ТЗ", "NEW");
        EpicTask.SubTask subtaskSecondEpicTaskFirst = new EpicTask.SubTask("Закончить учебу",
                "Сдать дипломный проект", "Сделать дипломный проект", "DONE");

        subTasksEpicTaskFirst.add(subtaskFirstEpicTaskFirst);
        subTasksEpicTaskFirst.add(subtaskSecondEpicTaskFirst);

        EpicTask epicTaskFirst = new EpicTask("Закончить учебу",
                "Получить сертификат обучения", subTasksEpicTaskFirst);
/**
 * Создали 2ю EpicTask задачу с 1й SubTask подзадачей
 */
        EpicTask.SubTask subtaskFirstEpicTaskSecond = new EpicTask.SubTask("Сменить работу",
                "Закончить курс по Java","Научиться программировать на языке Java",
                "NEW");
        ArrayList<EpicTask.SubTask> subTasksEpicTaskSecond = new ArrayList<>();

        subTasksEpicTaskSecond.add(subtaskFirstEpicTaskSecond);

        EpicTask epicTaskSecond = new EpicTask("Сменить работу"
                ,"Начать работать Java разработчиком", subTasksEpicTaskSecond);
/**
 * 1. Возможность хранить задачи всех типов
 */
        manager.saveToStorage(taskFirst);
        manager.saveToStorage(taskSecond);
        manager.saveToStorage(subtaskFirstEpicTaskFirst);
        manager.saveToStorage(subtaskSecondEpicTaskFirst);
        manager.saveToStorage(epicTaskFirst);
        manager.saveToStorage(subtaskFirstEpicTaskSecond);
        manager.saveToStorage(epicTaskSecond);
/**
 * 2. Методы для каждого из типа задач(Task/EpicTask/SubTask):
 *  2.1 Получение списка всех задач;
 */
        System.out.println("\n    2.1 Получение списка всех задач:");
        System.out.println(Arrays.toString(manager.getCompleteListOfAnyTasks(manager.getTaskStorage()).toArray()));
        System.out.println(Arrays.toString(manager.getCompleteListOfAnyTasks(manager.getEpicTaskStorage()).toArray()));
        System.out.println(Arrays.toString(manager.getCompleteListOfAnyTasks(manager.getSubTaskStorage()).toArray()));
/**
 *  2.2 Удаление всех задач;
 */
        manager.deleteAllTasksOfAnyType(manager.getEpicTaskStorage());

        System.out.println("\n    2.2 Удаление всех задач:");
        System.out.println(Arrays.toString(manager.getCompleteListOfAnyTasks(manager.getTaskStorage()).toArray()));
        System.out.println(Arrays.toString(manager.getCompleteListOfAnyTasks(manager.getEpicTaskStorage()).toArray()));
        System.out.println(Arrays.toString(manager.getCompleteListOfAnyTasks(manager.getSubTaskStorage()).toArray()));
/**
 *  2.3 Получение по идентификатору;
 */
        System.out.println("\n    2.3 Получение по идентификатору:");
        System.out.println(manager.getTaskOfAnyTypeById(0));
        System.out.println(manager.getTaskOfAnyTypeById(1));
        System.out.println(manager.getTaskOfAnyTypeById(2));
        System.out.println(manager.getTaskOfAnyTypeById(3));
        System.out.println(manager.getTaskOfAnyTypeById(4));
        System.out.println(manager.getTaskOfAnyTypeById(5));
        System.out.println(manager.getTaskOfAnyTypeById(6));
        System.out.println(manager.getTaskOfAnyTypeById(7));
        System.out.println(manager.getTaskOfAnyTypeById(8));
/**
 *  2.4 Создание. Сам объект должен передаваться в качестве параметра;
 */
        System.out.println("\n    2.4 Создание. Сам объект должен передаваться в качестве параметра:");
        System.out.println(manager.createCopyOfTaskOfAnyType(taskFirst));
        System.out.println(manager.createCopyOfTaskOfAnyType(epicTaskFirst));
        System.out.println(manager.createCopyOfTaskOfAnyType(subtaskFirstEpicTaskFirst));
/**
 *  2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра;
 */
        manager.updateTaskOfAnyType(5, epicTaskFirst);
        manager.updateTaskOfAnyType(7, epicTaskSecond);

        System.out.println("\n    2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде"
                + " параметра:");
        System.out.println(Arrays.toString(manager.getCompleteListOfAnyTasks(manager.getEpicTaskStorage()).toArray()));
/**
 *  2.6 Удаление по идентификатору.
 */
        manager.removeTaskOfAnyTypeById(1);
        manager.removeTaskOfAnyTypeById(2);

        System.out.println("\n    2.6 Удаление по идентификатору:");
        System.out.println(Arrays.toString(manager.getCompleteListOfAnyTasks(manager.getTaskStorage()).toArray()));
/**
 * 3. Дополнительные методы:
 *  3.1 Получение списка всех подзадач определённого эпика.
 */
        System.out.println("\n    3.1 Получение списка всех подзадач определённого эпика:");
        System.out.println(manager.getCompleteListOfSubTaskByEpicTask(epicTaskFirst));
        System.out.println(manager.getCompleteListOfSubTaskByEpicTask(epicTaskSecond));
    }
}