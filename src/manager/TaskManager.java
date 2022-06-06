package manager;

import task.EpicTask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Интерфейс для объекта-менеджера
 */
public interface TaskManager {

    /**
     * Метод для добавления подзадач в список
     */
    void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask);

    /**
     * 4. Метод для управления статусом для EpicTask задач
     */
    Task.Status getterEpicTaskStatus(ArrayList<Integer> listOfSubtaskIdOfTheFirstEpicTask);

    /**
     * 1. Метод для сохранения задач
     */
    void saveTask(Task task);

    void saveEpicTask(EpicTask epicTask);

    void saveSubTask(EpicTask.SubTask subTask);

    /**
     * 2.1 Получение списка всех задач
     */
    List<Task> getListOfTasks();

    List<EpicTask> getListOfEpicTasks();

    List<EpicTask.SubTask> getListOfSubTasks();

    /**
     * 2.2 Удаление всех задач
     */
    void deleteAllTasks();

    void deleteAllEpicTasks();

    void deleteAllSubTasks();

    /**
     * 2.3 Получение задачи по идентификатору
     */
    Task getTaskById(int id);

    EpicTask getEpicTaskById(int id);

    EpicTask.SubTask getSubTaskById(int id);

    /**
     * 2.4 Создание задачи
     */
    Task createCopyOfTask(Task task);

    EpicTask createCopyOfTask(EpicTask epicTask);

    EpicTask.SubTask createCopyOfTask(EpicTask.SubTask subTask);

    /**
     * 2.5 Обновление задачи
     */
    void updateTask(Task task);

    void updateEpicTask(EpicTask epicTask);

    void updateSubTask(EpicTask.SubTask subTask);

    /**
     * 2.6 Удаление задачи
     */
    void removeTaskById(int id);

    void removeEpicTaskById(int id);

    void removeSubTaskById(int id);

    /**
     * 3.1 Получение списка всех подзадач определённого эпика
     */
    List<EpicTask.SubTask> getListOfSubTaskByEpicTaskId(int i);

    /**
     * История просмотров задач
     */
    List<Task> getHistory();

    /**
     * Удаляет задачи из просмотра
     */
    void remove(int id);
}