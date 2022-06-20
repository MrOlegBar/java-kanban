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
        TaskManager manager = Managers.getDefault();

        /**
         * Создали 2е Task задачи
         */
        Task firstTask = new Task("Поесть", "Принять пищу", NEW);
        manager.saveTask(firstTask);

        Task secondTask = new Task("Поспать", "Хорошенько выспаться", DONE);
        manager.saveTask(secondTask);

        /**
         * Создали 1у EpicTask задачу с 3мя SubTask подзадачами
         */
        ArrayList<Integer> listOfSubtaskIdOfTheFirstEpicTask = new ArrayList<>();
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

        /**
         * Создали 2ю EpicTask задачу без SubTask подзадач
         */
        ArrayList<Integer> listOfSubtaskIdOfTheSecondEpicTask = new ArrayList<>();
        Task.Status statusOfTheSecondEpicTask = manager.getterEpicTaskStatus(listOfSubtaskIdOfTheSecondEpicTask);
        EpicTask secondEpicTask = new EpicTask("Сменить работу"
                , "Начать работать Java разработчиком", listOfSubtaskIdOfTheSecondEpicTask
                , statusOfTheSecondEpicTask);
        manager.saveEpicTask(secondEpicTask);

        System.out.println("\n    -Получение по идентификатору:");
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
        System.out.println(manager.getHistory());

        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("Autosave.csv"));

    }

    static File autosaveFile;

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
    public static Task taskFromString(String value) {
        String[] arrayThisTask = value.split(",");
        int taskId = Integer.parseInt(arrayThisTask[0]);
        String taskName = arrayThisTask[1];
        String taskDescription = arrayThisTask[2];
        Task.Status taskStatus = Task.Status.valueOf(arrayThisTask[3]);
        Task task = new Task(taskId, taskName, taskDescription, taskStatus);
        return task;
    }

    public static EpicTask epicTaskFromString(String value) {
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

    public static EpicTask.SubTask subTaskFromString(String value) {
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
     * Метод для сохранения менеджера истории в CSV
     */
    private static String historyManagerToString(HistoryManager manager) {
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
     * Метод для восстановления менеджера истории из CSV
     */
    public static List<Integer> fromStringToHistoryManager(String value) {
        String[] taskHistoryArray = null;
        if (value.matches("\\{.*\\}")) {
            value.replaceFirst("\\{", "");
            value.replaceFirst("\\}", "");
            taskHistoryArray = value.split(" ");
        }
        return new ArrayList(List.of(taskHistoryArray));
    }

    /**
     * Метод для восстановления данных менеджера из файла
     */
    public static FileBackedTasksManager loadFromFile(File file) throws FileException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String s;
            List<Integer> taskHistoryList = null;
            while ((s = br.readLine()) != null) {

                if ((s = br.readLine()).matches(".* Task.*")) {
                    Task restoredTask = taskFromString(s);
                    fileBackedTasksManager.updateTask(restoredTask);

                }

                if ((s = br.readLine()).matches(".* EpicTask.*")) {
                    EpicTask restoredEpicTask = epicTaskFromString(s);
                    fileBackedTasksManager.updateEpicTask(restoredEpicTask);
                }

                if ((s = br.readLine()).matches(".* SubTask.*")) {
                    EpicTask.SubTask restoredSubTask = subTaskFromString(s);
                    fileBackedTasksManager.updateSubTask(restoredSubTask);
                }

                if ((s = br.readLine()).matches("\\{.*\\}")) {
                    taskHistoryList = fromStringToHistoryManager(s);
                }
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return fileBackedTasksManager;
    }

    /**
     * Метод автосохранения менеджера задач
     */
    public void save() {
        String firstColumn = "| id | / { historyTasks }";
        String secondColumn = "| type |";
        String thirdColumn = "| name |";
        String fourthColumn = "| status |";
        String fifthColumn = "| description |";
        String sixthColumn = "| epic | / [ subTasks ]";
        String tableHeader = String.format("%-26s, %-9s, %-25s, %-11s , %-35s, %s,\n", firstColumn, secondColumn
                , thirdColumn, fourthColumn, fifthColumn, sixthColumn);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(autosaveFile, StandardCharsets.UTF_8
                , false))) {
            if(autosaveFile.delete()){
                autosaveFile.createNewFile();
            }

            bw.write(tableHeader);

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

            bw.write(historyManagerToString(getInMemoryHistoryManager()));
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
     * Метод для управления статусом для EpicTask задач
     */
    @Override
    public Task.Status getterEpicTaskStatus(ArrayList<Integer> listOfSubTaskId) {
        return super.getterEpicTaskStatus(listOfSubTaskId);
    }
}