package manager;

import task.EpicTask;
import task.Task;
import exception.FileException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    File autosaveFile;

    public FileBackedTasksManager(File file) throws FileException {
        if (isFileExists(file)) {
            this.autosaveFile = file;
        }
    }

    /**
     * Метод для проверки файла для автосохранения
     */
    private boolean isFileExists(File file) throws FileException {
        if(file.exists()) {
            System.out.println("Файл " + file.getName() + " создан");
            if(file.canRead()) {
                System.out.println("Доступен для чтения");
            } else {
                throw new FileException("Недоступен для чтения");
            }

            if(file.canWrite()) {
                System.out.println("Доступен для записи");
            } else {
                throw new FileException("Недоступен для записи");
            }
            return true;
        }
        else {
            throw new FileException("Файл " + file.getName() + " не создан");
        }
    }

    /**
     * Метод сохранения задачи в строку
     */
    public String taskToString(Task task) {
        return task.getId() + "," + task.getClass() + "," + task.getName() + "," + task.getStatus()
                + "," + task.getDescription();
    }

    public String epicTaskToString(EpicTask epicTask) {
        StringBuilder stringBuilderOfSubTaskId = new StringBuilder("[");
        for (Integer id : epicTask.getListOfSubTaskId()) {
            stringBuilderOfSubTaskId.append(id);
            stringBuilderOfSubTaskId.append(" ");
        }
        stringBuilderOfSubTaskId.append("]");
        String stringOfSubTaskId = stringBuilderOfSubTaskId.toString();
        stringOfSubTaskId = stringOfSubTaskId.replaceFirst(" ]", "]");

        return epicTask.getId() + "," + epicTask.getClass() + "," + epicTask.getName() + "," + epicTask.getStatus()
                + "," + epicTask.getDescription() + "," + stringOfSubTaskId;
    }

    public String subTaskToString(EpicTask.SubTask subTask) {
        return subTask.getId() + "," + subTask.getClass() + "," + subTask.getName() + "," + subTask.getStatus()
                + "," + subTask.getDescription() + "," + subTask.getEpicTaskId();
    }

    /**
     * Метод создания задачи из строки
     */
    public Task taskFromString(String value) {
        String[] arrayThisTask = value.split(",");
        int taskId = Integer.parseInt(arrayThisTask[0]);
        String taskName = arrayThisTask[1];
        String taskDescription = arrayThisTask[2];
        Task.Status taskStatus = Task.Status.valueOf(arrayThisTask[3]);
        Task task = new Task(taskId, taskName, taskDescription, taskStatus);
        return task;
    }

    public EpicTask epicTaskFromString(String value) {
        String[] arrayThisEpicTask = value.split(",");
        int epicTaskId = Integer.parseInt(arrayThisEpicTask[0]);
        String epicTaskName = arrayThisEpicTask[1];
        String epicTaskDescription = arrayThisEpicTask[2];

        ArrayList<Integer> listOfSubTaskIdOfTheEpicTask = null;
        String[] arrayOfSubTaskIdOfTheEpicTask = arrayThisEpicTask[4].split(" ");
        for (String id : arrayOfSubTaskIdOfTheEpicTask) {
            listOfSubTaskIdOfTheEpicTask.add(Integer.parseInt(id));
        }

        Task.Status epicTaskStatus = Task.Status.valueOf(arrayThisEpicTask[3]);
        EpicTask epicTask = new EpicTask(epicTaskId, epicTaskName, epicTaskDescription, listOfSubTaskIdOfTheEpicTask
                , epicTaskStatus);
        return epicTask;
    }

    public EpicTask.SubTask subTaskFromString(String value) {
        String[] arrayThisSubTask = value.split(",");
        int subTaskId = Integer.parseInt(arrayThisSubTask[0]);
        int epicTaskId = Integer.parseInt(arrayThisSubTask[4]);
        String subTaskName = arrayThisSubTask[1];
        String subTaskDescription = arrayThisSubTask[2];
        Task.Status subTaskStatus = Task.Status.valueOf(arrayThisSubTask[3]);
        EpicTask.SubTask subTask = new EpicTask.SubTask(subTaskId, epicTaskId, subTaskName, subTaskDescription, subTaskStatus);
        return subTask;
    }

    /**
     * Метод автосохранения менеджера задач
     */
    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(autosaveFile, StandardCharsets.UTF_8
                , false))) {
            if(autosaveFile.delete()){
                autosaveFile.createNewFile();
            }

            bw.write("| id | / { historyTasks },| type |,| name |,| status |,| description |" +
                    ",| epic | / [ subTasks ]\n"); //Шапка таблицы

            for (Task task : getListOfTasks()) {
                bw.write(taskToString(task) + "\n");
            }

            for (EpicTask epicTask : getListOfEpicTasks()) {
                bw.write(epicTaskToString(epicTask) + "\n");
            }

            for (EpicTask.SubTask subTask : getListOfSubTasks()) {
                bw.write(subTaskToString(subTask) + "\n");
            }

            bw.write("\n");

           List<Integer> listOfIdsFromHistory = new ArrayList<>();
            for (Task task : getHistory()) {
                listOfIdsFromHistory.add(task.getId());
            }

            bw.write(super.getStringOfIdsFromTaskHistory(listOfIdsFromHistory));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Метод для добавления подзадач в список
     */
    @Override
    public void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask) {
        super.addSubtaskToEpicTask(subTask, epicTask);
    }

    /**
     * 1. Метод для сохранения задач
     */
    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public void saveEpicTask(EpicTask epicTask) {
        super.saveEpicTask(epicTask);
        save();
    }

    @Override
    public void saveSubTask(EpicTask.SubTask subTask) {
        super.saveSubTask(subTask);
        save();
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
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    /**
     * 2.3 Получение задачи по идентификатору
     */
    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask epicTask = super.getEpicTaskById(id);
        save();
        return epicTask;
    }

    @Override
    public EpicTask.SubTask getSubTaskById(int id) {
        EpicTask.SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    /**
     * 2.4 Создание задачи
     */
    @Override
    public Task createCopyOfTask(Task task) {
        Task newTask = super.createCopyOfTask(task);
        save();
        return newTask;
    }

    @Override
    public EpicTask createCopyOfTask(EpicTask epicTask) {
        EpicTask newEpicTask = super.createCopyOfTask(epicTask);
        save();
        return newEpicTask;
    }

    @Override
    public EpicTask.SubTask createCopyOfTask(EpicTask.SubTask subTask) {
        EpicTask.SubTask newSubTask = super.createCopyOfTask(subTask);
        save();
        return newSubTask;
    }

    /**
     * 2.5 Обновление задачи
     */
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void updateSubTask(EpicTask.SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    /**
     * 2.6 Удаление задачи
     */
    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicTaskById(int id) {
        super.removeEpicTaskById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    /**
     * 3.1 Получение списка всех подзадач определённого эпика
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
        save();
    }

    /**
     * 4. Метод для управления статусом для EpicTask задач
     */
    @Override
    public Task.Status getterEpicTaskStatus(ArrayList<Integer> listOfSubTaskId) {
        return super.getterEpicTaskStatus(listOfSubTaskId);
    }
}