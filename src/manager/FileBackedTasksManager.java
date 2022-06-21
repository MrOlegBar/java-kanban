package manager;

import task.EpicTask;
import task.Task;
import exception.FileException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static task.Task.Status.DONE;
import static task.Task.Status.NEW;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    public static void main(String[] args) throws FileException {

        /**
         * Тестирование
         */
        FileBackedTasksManager recoveryManager = FileBackedTasksManager.loadFromFile(new File("Autosave.csv"));

        System.out.println("    Восстановленный список всех задач:");
        System.out.println(recoveryManager.getListOfTasks());
        System.out.println(recoveryManager.getListOfEpicTasks());
        System.out.println(recoveryManager.getListOfSubTasks());
        System.out.println("    Восстановленная История просмотров задач:");
        System.out.println(recoveryManager.getHistory());

        /**
         * Создали 2е Task задачи
         */
        Task firstTask = new Task("Поесть", "Принять пищу", NEW);
        recoveryManager.saveTask(firstTask);

        Task secondTask = new Task("Поспать", "Хорошенько выспаться", DONE);
        recoveryManager.saveTask(secondTask);
        System.out.println("    Создали 2е Task задачи:");
        System.out.println(recoveryManager.getListOfTasks());

        /**
         * Создали 1у EpicTask задачу с 3мя SubTask подзадачами
         */
        List<Integer> listOfSubtaskIdOfTheFirstEpicTask = new ArrayList<>();
        Task.Status statusOfTheFirstEpicTask = recoveryManager.getterEpicTaskStatus(listOfSubtaskIdOfTheFirstEpicTask);
        EpicTask firstEpicTask = new EpicTask("Закончить учебу",
                "Получить сертификат обучения", listOfSubtaskIdOfTheFirstEpicTask
                , statusOfTheFirstEpicTask);
        recoveryManager.saveEpicTask(firstEpicTask);

        EpicTask.SubTask firstSubtaskOfTheFirstEpicTask = new EpicTask.SubTask(firstEpicTask.getId()
                , "Сдать все спринты"
                , "Вовремя выполнить ТЗ", NEW);
        recoveryManager.saveSubTask(firstSubtaskOfTheFirstEpicTask);

        EpicTask.SubTask secondSubtaskOfTheFirstEpicTask = new EpicTask.SubTask(firstEpicTask.getId()
                , "Сдать дипломный проект"
                , "Сделать дипломный проект", DONE);
        recoveryManager.saveSubTask(secondSubtaskOfTheFirstEpicTask);

        EpicTask.SubTask thirdSubtaskOfTheFirstEpicTask = new EpicTask.SubTask(firstEpicTask.getId()
                , "Сдать 5й спринт"
                , "Сделать ТЗ", NEW);
        recoveryManager.saveSubTask(thirdSubtaskOfTheFirstEpicTask);

        recoveryManager.addSubtaskToEpicTask(firstSubtaskOfTheFirstEpicTask, firstEpicTask);
        recoveryManager.addSubtaskToEpicTask(secondSubtaskOfTheFirstEpicTask, firstEpicTask);
        recoveryManager.addSubtaskToEpicTask(thirdSubtaskOfTheFirstEpicTask, firstEpicTask);
        recoveryManager.updateEpicTask(firstEpicTask);

        System.out.println("    Создали 1у EpicTask задачу с 3мя SubTask подзадачами:");
        System.out.println(recoveryManager.getListOfEpicTasks());
        /**
         * Создали 2ю EpicTask задачу без SubTask подзадач
         */
        List<Integer> listOfSubtaskIdOfTheSecondEpicTask = new ArrayList<>();
        Task.Status statusOfTheSecondEpicTask = recoveryManager.getterEpicTaskStatus(listOfSubtaskIdOfTheSecondEpicTask);
        EpicTask secondEpicTask = new EpicTask("Сменить работу"
                , "Начать работать Java разработчиком", listOfSubtaskIdOfTheSecondEpicTask
                , statusOfTheSecondEpicTask);
        recoveryManager.saveEpicTask(secondEpicTask);

        System.out.println("    Создали 2ю EpicTask задачу без SubTask подзадач:");
        System.out.println(recoveryManager.getListOfEpicTasks());

        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(recoveryManager.getTaskById(8));
        System.out.println("    -История просмотров задач:");
        System.out.println(recoveryManager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(recoveryManager.getTaskById(9));
        System.out.println("    -История просмотров задач:");
        System.out.println(recoveryManager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(recoveryManager.getEpicTaskById(10));
        System.out.println("    -История просмотров задач:");
        System.out.println(recoveryManager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(recoveryManager.getSubTaskById(11));
        System.out.println("    -История просмотров задач:");
        System.out.println(recoveryManager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(recoveryManager.getSubTaskById(12));
        System.out.println("    -История просмотров задач:");
        System.out.println(recoveryManager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(recoveryManager.getSubTaskById(13));
        System.out.println("    -История просмотров задач:");
        System.out.println(recoveryManager.getHistory());
        System.out.println("\n    -Получение по идентификатору:");
        System.out.println(recoveryManager.getEpicTaskById(14));
        System.out.println("    -История просмотров задач:");
        System.out.println(recoveryManager.getHistory());
    }

    File autosaveFile;

    public FileBackedTasksManager(File file) throws FileException {
        if (isFileExists(file)) {
            this.autosaveFile = file;
        }
    }

    /**
     * Метод для восстановления данных менеджера из файла
     */
    public static FileBackedTasksManager loadFromFile(File file) throws FileException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        List<Integer> taskHistoryIds = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String autosaveFileLine;
            while ((autosaveFileLine = bufferedReader.readLine()) != null) {

                if ((autosaveFileLine.matches(".*, Task .*"))) {
                    Task restoredTask = taskFromString(autosaveFileLine);
                    manager.updateTask(restoredTask);
                    manager.setId(restoredTask.getId() + 1);
                }

                if ((autosaveFileLine.matches(".*, EpicTask .*"))) {
                    EpicTask restoredEpicTask = epicTaskFromString(autosaveFileLine);
                    manager.updateEpicTask(restoredEpicTask);
                    manager.setId(restoredEpicTask.getId() + 1);
                }

                if ((autosaveFileLine.matches(".*, SubTask .*"))) {
                    EpicTask.SubTask restoredSubTask = subTaskFromString(autosaveFileLine);
                    manager.updateSubTask(restoredSubTask);
                    manager.setId(restoredSubTask.getId() + 1);

                }

                if (autosaveFileLine.matches(".*\\{.*}.*")) {
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
                manager.getInMemoryHistoryManager().add(task);
            }

            if (epicTask != null) {
                manager.getInMemoryHistoryManager().add(epicTask);
            }

            if (subTask != null) {
                manager.getInMemoryHistoryManager().add(subTask);
            }
        }
        return manager;
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
    private String taskToString(Task task) {
        String type = String.valueOf(task.getClass()).replace("class task.", "");
        return String.format("%-26s, %-9s, %-25s, %-11s , %-35s,", task.getId(), type, task.getName(), task.getStatus()
                , task.getDescription());
    }

    private String epicTaskToString(EpicTask epicTask) {
        StringBuilder stringBuilderOfSubTaskId = new StringBuilder("[");
        for (Integer id : epicTask.getListOfSubTaskId()) {
            stringBuilderOfSubTaskId.append(id);
            stringBuilderOfSubTaskId.append(" ");
        }
        stringBuilderOfSubTaskId.append("]");
        String stringOfSubTaskId = stringBuilderOfSubTaskId.toString();
        stringOfSubTaskId = stringOfSubTaskId.replaceFirst(" ]", "]");
        String type = String.valueOf(epicTask.getClass()).replace("class task.", "");
        return String.format("%-26s, %-9s, %-25s, %-11s , %-35s, %s", epicTask.getId(), type, epicTask.getName()
                , epicTask.getStatus(), epicTask.getDescription(), stringOfSubTaskId);
    }

    private String subTaskToString(EpicTask.SubTask subTask) {
        String type = String.valueOf(subTask.getClass()).replace("class task.EpicTask$", "");
        return String.format("%-26s, %-9s, %-25s, %-11s , %-35s, %s", subTask.getId(), type, subTask.getName()
                , subTask.getStatus(), subTask.getDescription(), subTask.getEpicTaskId());
    }

    /**
     * Метод создания задачи из строки
     */
    private static Task taskFromString(String value) {
        String[] arrayThisTask = value.split(",");
        int taskId = Integer.parseInt(arrayThisTask[0].trim());
        String taskName = arrayThisTask[2].trim();
        String taskDescription = arrayThisTask[4].trim();
        Task.Status taskStatus = Task.Status.valueOf(arrayThisTask[3].trim());
        return new Task(taskId, taskName, taskDescription, taskStatus);
    }

    private static EpicTask epicTaskFromString(String value) {
        String[] arrayThisEpicTask = value.split(",");
        int epicTaskId = Integer.parseInt(arrayThisEpicTask[0].trim());
        String epicTaskName = arrayThisEpicTask[2].trim();
        String epicTaskDescription = arrayThisEpicTask[4].trim();

        List<Integer> listOfSubTaskIdOfTheEpicTask = new ArrayList<>();
        if (!arrayThisEpicTask[5].matches(" \\[]")) {
            String lineForSplit = arrayThisEpicTask[5].replaceFirst(" \\[", "").replaceFirst("]"
                    , "");
            String[] arrayOfSubTaskIdOfTheEpicTask = lineForSplit.split(" ");
            for (String elem : arrayOfSubTaskIdOfTheEpicTask) {
                listOfSubTaskIdOfTheEpicTask.add(Integer.parseInt(elem));
            }
        }

        Task.Status epicTaskStatus = Task.Status.valueOf(arrayThisEpicTask[3].trim());
        return new EpicTask(epicTaskId, epicTaskName, epicTaskDescription, listOfSubTaskIdOfTheEpicTask
                , epicTaskStatus);
    }

    private static EpicTask.SubTask subTaskFromString(String value) {
        String[] arrayThisSubTask = value.split(",");
        int subTaskId = Integer.parseInt(arrayThisSubTask[0].trim());
        int epicTaskId = Integer.parseInt(arrayThisSubTask[5].trim());
        String subTaskName = arrayThisSubTask[2].trim();
        String subTaskDescription = arrayThisSubTask[4].trim();
        Task.Status subTaskStatus = Task.Status.valueOf(arrayThisSubTask[3].trim());
        return new EpicTask.SubTask(subTaskId, epicTaskId, subTaskName, subTaskDescription, subTaskStatus);
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

        StringBuilder stringBuilderOfIdsFromTaskHistory = new StringBuilder("{");
        for (Integer id : listOfIdsFromHistory) {
            stringBuilderOfIdsFromTaskHistory.append(id);
            stringBuilderOfIdsFromTaskHistory.append(" ");
        }
        stringBuilderOfIdsFromTaskHistory.append("}");
        String stringOfIdsFromTaskHistory = stringBuilderOfIdsFromTaskHistory.toString();
        stringOfIdsFromTaskHistory = stringOfIdsFromTaskHistory.replaceFirst(" }", "}");
        return stringOfIdsFromTaskHistory;
    }

    /**
     * Метод для восстановления истории задач из строки CSV
     */
    private static List<Integer> fromStringToHistoryManager(String value) {
        List<Integer> listOfIdsFromHistory = new ArrayList<>();
        if (value.matches("\\{.*}")) {
            String[] taskHistoryArray = value.replaceFirst("\\{", "").replaceFirst("}", "")
                    .split(" ");
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
        String firstColumn = "| id | / { historyTasks }";
        String secondColumn = "| type |";
        String thirdColumn = "| name |";
        String fourthColumn = "| status |";
        String fifthColumn = "| description |";
        String sixthColumn = "| epic | / [ subTasks ]";
        String tableHeader = String.format("%-26s, %-9s, %-25s, %-11s , %-35s, %s,\n", firstColumn, secondColumn
                , thirdColumn, fourthColumn, fifthColumn, sixthColumn);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(autosaveFile, StandardCharsets.UTF_8
                , false))) {
            if(autosaveFile.delete()){
                autosaveFile.createNewFile();
            }

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

            bufferedWriter.write(historyManagerToString(getInMemoryHistoryManager()));
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