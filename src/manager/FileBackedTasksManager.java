package manager;

import task.EpicTask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    /**
     * Метод для добавления подзадач в список
     *
     * @param subTask
     * @param epicTask
     */
    @Override
    public void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask) {
        super.addSubtaskToEpicTask(subTask, epicTask);
    }

    /**
     * 1. Метод для сохранения задач
     *
     * @param task
     */
    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
    }

    @Override
    public void saveEpicTask(EpicTask epicTask) {
        super.saveEpicTask(epicTask);
    }

    @Override
    public void saveSubTask(EpicTask.SubTask subTask) {
        super.saveSubTask(subTask);
    }

    /**
     * 2.1 Получение списка всех задач
     */
    @Override
    public ArrayList<Task> getListOfTasks() {
        return super.getListOfTasks();
    }

    @Override
    public ArrayList<EpicTask> getListOfEpicTasks() {
        return super.getListOfEpicTasks();
    }

    @Override
    public ArrayList<EpicTask.SubTask> getListOfSubTasks() {
        return super.getListOfSubTasks();
    }

    /**
     * 2.2 Удаление всех задач
     */
    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
    }

    /**
     * 2.3 Получение задачи по идентификатору
     *
     * @param id
     */
    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        return super.getEpicTaskById(id);
    }

    @Override
    public EpicTask.SubTask getSubTaskById(int id) {
        return super.getSubTaskById(id);
    }

    /**
     * 2.4 Создание задачи
     *
     * @param task
     */
    @Override
    public Task createCopyOfTask(Task task) {
        return super.createCopyOfTask(task);
    }

    @Override
    public EpicTask createCopyOfTask(EpicTask epicTask) {
        return super.createCopyOfTask(epicTask);
    }

    @Override
    public EpicTask.SubTask createCopyOfTask(EpicTask.SubTask subTask) {
        return super.createCopyOfTask(subTask);
    }

    /**
     * 2.5 Обновление задачи
     *
     * @param task
     */
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
    }

    @Override
    public void updateSubTask(EpicTask.SubTask subTask) {
        super.updateSubTask(subTask);
    }

    /**
     * 2.6 Удаление задачи
     *
     * @param id
     */
    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
    }

    @Override
    public void removeEpicTaskById(int id) {
        super.removeEpicTaskById(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
    }

    /**
     * 3.1 Получение списка всех подзадач определённого эпика
     *
     * @param id
     */
    @Override
    public ArrayList<EpicTask.SubTask> getListOfSubTaskByEpicTaskId(int id) {
        return super.getListOfSubTaskByEpicTaskId(id);
    }

    /**
     * История просмотров задач
     */
    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void remove(int id) {
        super.remove(id);
    }

    /**
     * 4. Метод для управления статусом для EpicTask задач
     *
     * @param listOfSubTaskId
     */
    @Override
    public Task.Status getterEpicTaskStatus(ArrayList<Integer> listOfSubTaskId) {
        return super.getterEpicTaskStatus(listOfSubTaskId);
    }
}
