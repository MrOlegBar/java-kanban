package manager;

import task.EpicTask;
import task.Task;
import exception.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static task.Task.Status.DONE;
import static task.Task.Status.NEW;
import static task.Task.Type.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File autosaveFile;

    public FileBackedTasksManager(File file) throws ManagerSaveException {
        this.autosaveFile = fileExists(file);
    }

    public static void main(String[] args) throws ManagerSaveException {

        /**
         * Тестирование
         */
        FileBackedTasksManager manager = new FileBackedTasksManager(new File("Autosave.csv"));

        /**
         * Создали 2е Task задачи
         */
        Task firstTask = new Task("Поесть", "Принять пищу", NEW);
        manager.saveTask(firstTask);

        Task secondTask = new Task("Поспать", "Хорошенько выспаться", DONE);
        manager.saveTask(secondTask);
        System.out.println("    Создали 2е Task задачи:");
        System.out.println(manager.getListOfTasks());

        /**
         * Создали 1у EpicTask задачу с 3мя SubTask подзадачами
         */
        List<Integer> listOfSubtaskIdOfTheFirstEpicTask = new ArrayList<>();
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
        manager.updateEpicTask(firstEpicTask);

        System.out.println("    Создали 1у EpicTask задачу с 3мя SubTask подзадачами:");
        System.out.println(manager.getListOfEpicTasks());
        /**
         * Создали 2ю EpicTask задачу без SubTask подзадач
         */
        List<Integer> listOfSubtaskIdOfTheSecondEpicTask = new ArrayList<>();
        Task.Status statusOfTheSecondEpicTask = manager.getterEpicTaskStatus(listOfSubtaskIdOfTheSecondEpicTask);
        EpicTask secondEpicTask = new EpicTask("Сменить работу"
                , "Начать работать Java разработчиком", listOfSubtaskIdOfTheSecondEpicTask
                , statusOfTheSecondEpicTask);
        manager.saveEpicTask(secondEpicTask);

        System.out.println("    Создали 2ю EpicTask задачу без SubTask подзадач:");
        System.out.println(manager.getListOfEpicTasks());

        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getEpicTaskById(3));
        System.out.println(manager.getSubTaskById(4));
        System.out.println(manager.getSubTaskById(5));
        System.out.println(manager.getSubTaskById(6));
        System.out.println(manager.getEpicTaskById(7));
        System.out.println("    -История просмотров задач:");
        System.out.println(manager.getHistory());

        FileBackedTasksManager recoveryManager = manager.loadFromFile(new File("Autosave.csv"));
        System.out.println("    Восстановленный список всех задач:");
        System.out.println(recoveryManager.getListOfTasks());
        System.out.println(recoveryManager.getListOfEpicTasks());
        System.out.println(recoveryManager.getListOfSubTasks());
        System.out.println("    Восстановленная История просмотров задач:");
        System.out.println(recoveryManager.getHistory());
    }

    /**
     * Метод для восстановления данных менеджера из файла
     */
    public FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        List<Integer> taskHistoryIds = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String autosaveFileLine;
            while ((autosaveFileLine = bufferedReader.readLine()) != null) {

                if ((autosaveFileLine.matches(".*, Task .*"))) {
                    Task restoredTask = fromString(autosaveFileLine);
                    manager.updateTask(restoredTask);
                    manager.setId(restoredTask.getId() + 1);
                }

                if ((autosaveFileLine.matches(".*, EpicTask .*"))) {
                    EpicTask restoredEpicTask = (EpicTask) fromString(autosaveFileLine);
                    manager.updateEpicTask(restoredEpicTask);
                    manager.setId(restoredEpicTask.getId() + 1);
                }

                if ((autosaveFileLine.matches(".*, SubTask .*"))) {
                    EpicTask.SubTask restoredSubTask = (EpicTask.SubTask) fromString(autosaveFileLine);
                    EpicTask epicTaskForSubTask = manager.getEpicTaskById(restoredSubTask.getEpicTaskId());
                    List<Integer> listOfSubTaskId = epicTaskForSubTask.getListOfSubTaskId();
                    listOfSubTaskId.add(restoredSubTask.getId());
                    manager.updateEpicTask(epicTaskForSubTask);
                    manager.updateSubTask(restoredSubTask);
                    manager.setId(restoredSubTask.getId() + 1);

                }

                if (autosaveFileLine.matches("^\\d, \\d.*")) {
                    taskHistoryIds.addAll(fromStringToHistoryManager(autosaveFileLine));
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        for (Integer id : taskHistoryIds) {
            Task task = manager.getTaskById(id);
            EpicTask epicTask = manager.getEpicTaskById(id);
            EpicTask.SubTask subTask = manager.getSubTaskById(id);

            if (task != null) {
                inMemoryHistoryManager.add(task);
            }

            if (epicTask != null) {
                inMemoryHistoryManager.add(epicTask);
            }

            if (subTask != null) {
                inMemoryHistoryManager.add(subTask);
            }
        }
        return manager;
    }

    /**
     * Метод для проверки файла для автосохранения
     */
    private File fileExists(File file) throws ManagerSaveException {
        if (file.exists()) {
            System.out.println("Файл " + file.getName() + " создан");
            if(file.canRead()) {
                System.out.println("Доступен для чтения");
            } else {
                throw new ManagerSaveException("Недоступен для чтения");
            }

            if(file.canWrite()) {
                System.out.println("Доступен для записи");
            } else {
                throw new ManagerSaveException("Недоступен для записи");
            }
        }
        else {
            file = new File(file.getPath());
        }
        return file;
    }

    /**
     * Метод сохранения задачи в строку
     */
    private String taskToString(Task task) {
        String type = String.valueOf(task.getClass()).replace("class task.", "");
        return String.format("%s, %s, %s, %s , %s,", task.getId(), type, task.getName(), task.getStatus()
                , task.getDescription());
    }

    private String epicTaskToString(EpicTask epicTask) {
        String type = String.valueOf(epicTask.getClass()).replace("class task.", "");
        return String.format("%s, %s, %s, %s , %s", epicTask.getId(), type, epicTask.getName()
                , epicTask.getStatus(), epicTask.getDescription());
    }

    private String subTaskToString(EpicTask.SubTask subTask) {
        String type = String.valueOf(subTask.getClass()).replace("class task.EpicTask$", "");
        return String.format("%s, %s, %s, %s , %s, %s", subTask.getId(), type, subTask.getName()
                , subTask.getStatus(), subTask.getDescription(), subTask.getEpicTaskId());
    }

    /**
     * Метод создания задачи из строки
     */
    private static Task fromString(String value) {
        Task result = null;
        String[] arrayThisTask = value.split(",");
        String type = arrayThisTask[1].trim();
        String task = TASK.toString();
        String epictask = EPICTASK.toString();
        String subtask = SUBTASK.toString();

        if (type.equalsIgnoreCase(task)) {

            int taskId = Integer.parseInt(arrayThisTask[0].trim());
            String taskName = arrayThisTask[2].trim();
            String taskDescription = arrayThisTask[4].trim();
            Task.Status taskStatus = Task.Status.valueOf(arrayThisTask[3].trim());
            result = new Task(taskId, taskName, taskDescription, taskStatus);
        }

        if (type.equalsIgnoreCase(epictask)) {

            int epicTaskId = Integer.parseInt(arrayThisTask[0].trim());
            String epicTaskName = arrayThisTask[2].trim();
            String epicTaskDescription = arrayThisTask[4].trim();

            List<Integer> listOfSubTaskIdOfTheEpicTask = new ArrayList<>();

            Task.Status epicTaskStatus = Task.Status.valueOf(arrayThisTask[3].trim());
            result = new EpicTask(epicTaskId, epicTaskName, epicTaskDescription, listOfSubTaskIdOfTheEpicTask
                    , epicTaskStatus);
        }

        if (type.equalsIgnoreCase(subtask)) {

            int subTaskId = Integer.parseInt(arrayThisTask[0].trim());
            int epicTaskId = Integer.parseInt(arrayThisTask[5].trim());
            String subTaskName = arrayThisTask[2].trim();
            String subTaskDescription = arrayThisTask[4].trim();
            Task.Status subTaskStatus = Task.Status.valueOf(arrayThisTask[3].trim());
            result = new EpicTask.SubTask(subTaskId, epicTaskId, subTaskName, subTaskDescription, subTaskStatus);
        }
        return result;
    }

    /**
     * Метод для сохранения менеджера истории в CSV
     */
    private static String historyManagerToString(HistoryManager<Task> manager) {
        List<Integer> listOfIdsFromHistory = new ArrayList<>();
        List<Task> list = manager.getHistory();
        for (Task task : list) {
            listOfIdsFromHistory.add(task.getId());
        }

        return listOfIdsFromHistory.toString().replaceFirst("\\[", "").replaceFirst("]"
                , "");
    }

    /**
     * Метод для восстановления истории задач из строки CSV
     */
    private static List<Integer> fromStringToHistoryManager(String value) {
        List<Integer> listOfIdsFromHistory = new ArrayList<>();
        if (value.matches("^\\d, \\d.*")) {
            String[] taskHistoryArray = value.split(", ");
            for (String elem : taskHistoryArray) {
                listOfIdsFromHistory.add(Integer.parseInt(elem));
            }
        }
        return listOfIdsFromHistory;
    }

    /**
     * Метод автосохранения менеджера задач
     */
    private void save() {
        String firstColumn = "id";
        String secondColumn = "type";
        String thirdColumn = "name";
        String fourthColumn = "status";
        String fifthColumn = "description";
        String sixthColumn = "epic";
        String tableHeader = String.format("%s, %s, %s, %s , %s, %s,\n", firstColumn, secondColumn
                , thirdColumn, fourthColumn, fifthColumn, sixthColumn);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(autosaveFile, StandardCharsets.UTF_8
                , false))) {

            bufferedWriter.write(tableHeader);

            for (Task task : getListOfTasks()) {
                bufferedWriter.write(taskToString(task) + "\n");
            }

            for (EpicTask epicTask : getListOfEpicTasks()) {
                bufferedWriter.write(epicTaskToString(epicTask) + "\n");
            }

            for (EpicTask.SubTask subTask : getListOfSubTasks()) {
                bufferedWriter.write(subTaskToString(subTask) + "\n");
            }

            bufferedWriter.write("\n");

            bufferedWriter.write(historyManagerToString(inMemoryHistoryManager));
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
     * Метод для сохранения задач
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
     * Получение списка всех задач
     */
    @Override
    public List<Task> getListOfTasks() {
        return super.getListOfTasks();
    }

    @Override
    public List<EpicTask> getListOfEpicTasks() {
        return super.getListOfEpicTasks();
    }

    @Override
    public List<EpicTask.SubTask> getListOfSubTasks() {
        return super.getListOfSubTasks();
    }

    /**
     * Удаление всех задач
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
     * Получение задачи по идентификатору
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
     * Создание задачи
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
     * Обновление задачи
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
     * Удаление задачи
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
     * Получение списка всех подзадач определённого эпика
     */
    @Override
    public List<EpicTask.SubTask> getListOfSubTaskByEpicTaskId(int id) {
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
     * Метод для управления статусом для EpicTask задач
     */
    @Override
    public Task.Status getterEpicTaskStatus(List<Integer> listOfSubTaskId) {
        return super.getterEpicTaskStatus(listOfSubTaskId);
    }
}