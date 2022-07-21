package manager;

import exception.ManagerCreateException;
import task.EpicTask;
import task.Task;
import exception.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static task.Task.Status.DONE;
import static task.Task.Status.NEW;
import static task.Task.Type.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File autosaveFile;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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
        /*Task firstTask = manager.createTask(new Task("Поесть", "Принять пищу", NEW, LocalDateTime.now().minusMinutes(30L)
                , 30L));

        manager.saveTask(firstTask);

        Task secondTask = manager.createTask(new Task("Поспать", "Хорошенько выспаться", DONE, LocalDateTime.now().plusMinutes(30L)
                , 600L));
        manager.saveTask(secondTask);
        System.out.println("    Создали 2е Task задачи:");
        System.out.println(manager.getListOfTasks());*/

        /**
         * Создали 1у EpicTask задачу с 3мя SubTask подзадачами
         */
        /*List<Integer> listOfSubtaskIdOfTheFirstEpicTask = new ArrayList<>();
        Task.Status statusOfTheFirstEpicTask = manager.getterEpicTaskStatus(listOfSubtaskIdOfTheFirstEpicTask);
        LocalDateTime startTimeOfTheFirstEpicTask = manager.getterEpicTaskStartTime(listOfSubtaskIdOfTheFirstEpicTask);
        long durationOfTheFirstEpicTask = manager.getterEpicTaskDuration(listOfSubtaskIdOfTheFirstEpicTask);

        EpicTask firstEpicTask = manager.createTask(new EpicTask(
                "Закончить учебу"
                , "Получить сертификат обучения"
                , listOfSubtaskIdOfTheFirstEpicTask
                , statusOfTheFirstEpicTask
                , startTimeOfTheFirstEpicTask
                , durationOfTheFirstEpicTask
        ));

        manager.saveEpicTask(firstEpicTask);

        EpicTask.SubTask firstSubtaskOfTheFirstEpicTask = manager.createTask(new EpicTask.SubTask(
                firstEpicTask.getId()
                , "Сдать все спринты"
                , "Вовремя выполнить ТЗ"
                , NEW
                , LocalDateTime.now().plusMinutes(630L)
                , 150_000L
        ));

        manager.saveSubTask(firstSubtaskOfTheFirstEpicTask);

        EpicTask.SubTask secondSubtaskOfTheFirstEpicTask = manager.createTask(new EpicTask.SubTask(
                firstEpicTask.getId()
                , "Сдать дипломный проект"
                , "Сделать дипломный проект"
                , DONE
                , LocalDateTime.now().plusMinutes(150_630L)
                , 250_000L
        ));

        manager.saveSubTask(secondSubtaskOfTheFirstEpicTask);

        EpicTask.SubTask thirdSubtaskOfTheFirstEpicTask = manager.createTask(new EpicTask.SubTask(
                firstEpicTask.getId()
                , "Сдать 5й спринт"
                , "Сделать ТЗ"
                , NEW
                , LocalDateTime.now().plusMinutes(400_630L)
                , 4_320L
        ));

        manager.saveSubTask(thirdSubtaskOfTheFirstEpicTask);

        manager.addSubtaskToEpicTask(firstSubtaskOfTheFirstEpicTask, firstEpicTask);
        manager.addSubtaskToEpicTask(secondSubtaskOfTheFirstEpicTask, firstEpicTask);
        manager.addSubtaskToEpicTask(thirdSubtaskOfTheFirstEpicTask, firstEpicTask);

        manager.updateEpicTask(firstEpicTask);

        System.out.println("    Создали 1у EpicTask задачу с 3мя SubTask подзадачами:");
        System.out.println(manager.getListOfEpicTasks());*/

        /**
         * Создали 2ю EpicTask задачу без SubTask подзадач
         */
        /*List<Integer> listOfSubtaskIdOfTheSecondEpicTask = new ArrayList<>();
        Task.Status statusOfTheSecondEpicTask = manager.getterEpicTaskStatus(listOfSubtaskIdOfTheSecondEpicTask);
        LocalDateTime startTimeOfTheSecondEpicTask = manager.getterEpicTaskStartTime(listOfSubtaskIdOfTheSecondEpicTask);
        long durationOfTheSecondEpicTask = manager.getterEpicTaskDuration(listOfSubtaskIdOfTheSecondEpicTask);
        EpicTask secondEpicTask = manager.createTask(new EpicTask("Сменить работу", "Начать работать Java разработчиком"
                , listOfSubtaskIdOfTheSecondEpicTask, statusOfTheSecondEpicTask, startTimeOfTheSecondEpicTask
        , durationOfTheSecondEpicTask));
        manager.saveEpicTask(secondEpicTask);

        System.out.println("    Создали 2ю EpicTask задачу без SubTask подзадач:");
        System.out.println(manager.getListOfEpicTasks());*/

        /**
         * Получение по идентификатору
         * Восстановленный список всех задач
         * Восстановленная История просмотров задач
         * Метод для возвращения списка задач и подзадач в заданном порядке
         */
        /*System.out.println("\n    -Получение по идентификатору:");
        try {
            System.out.println(manager.getTaskById(1));
            System.out.println(manager.getTaskById(2));

            System.out.println(manager.getEpicTaskById(3));
            System.out.println(manager.getSubTaskById(4));
            System.out.println(manager.getSubTaskById(5));
            System.out.println(manager.getSubTaskById(6));
            System.out.println(manager.getEpicTaskById(7));
            System.out.println("    -История просмотров задач:");
            System.out.println(manager.getListOfTaskHistory());

            FileBackedTasksManager recoveryManager = FileBackedTasksManager.loadFromFile(new File("Autosave.csv"));

            System.out.println("    Восстановленный список всех задач:");
            System.out.println(recoveryManager.getListOfTasks());
            System.out.println(recoveryManager.getListOfEpicTasks());
            System.out.println(recoveryManager.getListOfSubTasks());
            System.out.println("    Восстановленная История просмотров задач:");
            System.out.println(recoveryManager.getListOfTaskHistory());

            System.out.println("    Метод для возвращения списка задач и подзадач в заданном порядке:");
            System.out.println(manager.getterPrioritizedTasks());
        } catch (NullPointerException | ClassCastException e) {
            System.out.println(e.getMessage());
        }*/
    }

    /**
     * Восстановливает данные менеджера из файла
     */
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager localManager = new FileBackedTasksManager(file);
        List<Integer> taskHistoryIds = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String autosaveFileLine;
            while ((autosaveFileLine = bufferedReader.readLine()) != null) {

                if ((autosaveFileLine.matches(".* Task.*"))) {
                    Task restoredTask = fromString(autosaveFileLine);
                    localManager.updateTaskFromString(restoredTask);
                    localManager.setId(restoredTask.getId());
                }

                if ((autosaveFileLine.matches(".* EpicTask.*"))) {
                    EpicTask restoredEpicTask = (EpicTask) fromString(autosaveFileLine);

                    localManager.updateEpicTaskFromString(restoredEpicTask);
                    localManager.setId(restoredEpicTask.getId());
                }

                if ((autosaveFileLine.matches(".* SubTask.*"))) {
                    EpicTask.SubTask restoredSubTask = (EpicTask.SubTask) fromString(autosaveFileLine);

                    List<Integer> listOfSubTaskId;
                    for (EpicTask epicTask : localManager.getListOfEpicTasks()) {
                        if (epicTask.getId() == restoredSubTask.getEpicTaskId()) {
                            listOfSubTaskId = epicTask.getListOfSubTaskId();
                            listOfSubTaskId.add(restoredSubTask.getId());
                            localManager.updateSubTaskFromString(restoredSubTask);
                            localManager.setId(restoredSubTask.getId());
                        }
                    }
                }

                if (autosaveFileLine.matches("^\\d+$") || autosaveFileLine.matches("^\\d+, \\d+.*")) {
                    taskHistoryIds.addAll(fromStringToHistoryManager(autosaveFileLine));
                    for (Integer id : taskHistoryIds) {
                        localManager.getTaskByIdFromString(id);
                        localManager.getEpicTaskByIdFromString(id);
                        localManager.getSubTaskByIdFromString(id);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return localManager;
    }

    /**
     * Проверяет доступность файла для автосохранения
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
     * Сохраняет задачу в строку
     */
    private String taskToString(Task task) {
        String startTime = "null";
        String endTime = "null";
        String type = String.valueOf(task.getClass()).replace("class task.", "");

        if (task.getStartTime() != null) {
            startTime = task.getStartTime().format(formatter);
            endTime = task.getEndTime().format(formatter);
        }
        return String.format("%s, %s, %s, %s, %s, -, %s, %s, %s", task.getId(), type, task.getName(), task.getStatus()
                , task.getDescription(), startTime, task.getDuration(), endTime);
    }

    private String epicTaskToString(EpicTask epicTask) {
        String startTime = "null";
        String endTime = "null";
        long duration = 0;
        String type = String.valueOf(epicTask.getClass()).replace("class task.", "");

        if (epicTask.getListOfSubTaskId().size() != 0) {
            startTime = getterEpicTaskStartTime(epicTask.getListOfSubTaskId()).format(formatter);
            endTime = getterEpicTaskEndTime(epicTask.getListOfSubTaskId()).format(formatter);
            duration = getterEpicTaskDuration(epicTask.getListOfSubTaskId());
            super.saveEpicTask(epicTask);
        }

        return String.format("%s, %s, %s, %s, %s, -, %s, %s, %s", epicTask.getId(), type, epicTask.getName()
                , epicTask.getStatus(), epicTask.getDescription(), startTime, duration, endTime);
    }

    private String subTaskToString(EpicTask.SubTask subTask) {
        String startTime = "null";
        String endTime = "null";
        String type = String.valueOf(subTask.getClass()).replace("class task.EpicTask$", "");
        if (subTask.getStartTime() != null) {
            startTime = subTask.getStartTime().format(formatter);
            endTime = subTask.getEndTime().format(formatter);
        }
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s", subTask.getId(), type, subTask.getName()
                , subTask.getStatus(), subTask.getDescription(), subTask.getEpicTaskId(), startTime
                , subTask.getDuration(), endTime);
    }

    /**
     * Создает задачу из строки
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
            LocalDateTime startTime = LocalDateTime.parse(arrayThisTask[6].trim(), formatter);
            long duration = Long.parseLong(arrayThisTask[7].trim());
            result = new Task(taskId, taskName, taskDescription, taskStatus, startTime, duration);
        }

        if (type.equalsIgnoreCase(epictask)) {

            int epicTaskId = Integer.parseInt(arrayThisTask[0].trim());
            String epicTaskName = arrayThisTask[2].trim();
            String epicTaskDescription = arrayThisTask[4].trim();

            List<Integer> listOfSubTaskIdOfTheEpicTask = new ArrayList<>();

            Task.Status epicTaskStatus = Task.Status.valueOf(arrayThisTask[3].trim());
            LocalDateTime epicTaskStartTime = null;
            if (!arrayThisTask[6].trim().matches(".*null.*")) {
                String startTimeTrim = arrayThisTask[6].trim();
                epicTaskStartTime = LocalDateTime.parse(startTimeTrim, formatter);
            }
            long epicTaskDuration = Long.parseLong(arrayThisTask[7].trim());
            result = new EpicTask(epicTaskId, epicTaskName, epicTaskDescription, listOfSubTaskIdOfTheEpicTask
                    , epicTaskStatus, epicTaskStartTime, epicTaskDuration);
        }

        if (type.equalsIgnoreCase(subtask)) {

            int subTaskId = Integer.parseInt(arrayThisTask[0].trim());
            int epicTaskId = Integer.parseInt(arrayThisTask[5].trim());
            String subTaskName = arrayThisTask[2].trim();
            String subTaskDescription = arrayThisTask[4].trim();
            Task.Status subTaskStatus = Task.Status.valueOf(arrayThisTask[3].trim());
            LocalDateTime startTime = LocalDateTime.parse(arrayThisTask[6].trim(), formatter);
            long duration = Long.parseLong(arrayThisTask[7].trim());
            result = new EpicTask.SubTask(subTaskId, epicTaskId, subTaskName, subTaskDescription, subTaskStatus
            ,startTime, duration);
        }
        return result;
    }

    private Task taskFromRequest(String body) {

        String[] keyValueArray;
        String name = null;
        String description = null;
        Task.Status status = null;
        LocalDateTime startTime = null;
        long duration = 0;

        String[] stringArray = body
                .replaceFirst("\\{\\s*", "")
                .replaceAll("\"", "")
                .replaceFirst("\\s*}", "")
                .split(",\\s*");

        for (String string : stringArray) {
            keyValueArray = string.split(": ");
            String key = keyValueArray[0];
            String value = keyValueArray[1];

            switch (key) {
                case "name":
                    name = value;
                    continue;
                case "description":
                    description = value;
                    continue;
                case "status":
                    status = Task.Status.valueOf(value);
                    continue;
                case "startTime":
                    if (!value.equals("null")) {
                        startTime = LocalDateTime.parse(value, formatter);
                    }
                    continue;
                case "duration":
                    duration = Long.parseLong(value);
                    continue;
                default:
                    break;
            }
        }
        return new Task(name, description, status, startTime, duration);
    }

    private EpicTask epictaskFromRequest(String body) {
        List<Integer> listOfSubTaskId = new ArrayList<>();
        String[] keyValueArray;
        String name = null;
        String description = null;
        Task.Status status = null;
        LocalDateTime startTime = null;
        long duration = 0;
        String[] stringArrayBody = body
                .replaceFirst("\\{\\s*\"", "")
                .replaceFirst("\"\\s*}", "")
                .split(",\\s*\"");

        for (String string : stringArrayBody) {
            keyValueArray = string.split(": ");
            String key = keyValueArray[0]
                    .replaceAll("\"", "");
            String value = keyValueArray[1]
                    .trim()
                    .replaceAll("\"", "");

            switch (key) {
                case "listOfSubTaskId":
                    if (!value.equals("[]")) {
                        String[] stringArrayValue = value
                                .replaceFirst("\\[", "")
                                .replaceAll("\\s", "")
                                .replaceFirst("]", "")
                                .split(",");
                        for (String id : stringArrayValue) {
                            listOfSubTaskId.add(Integer.valueOf(id));
                        }
                    }
                    continue;
                case "name":
                    name = value;
                    continue;
                case "description":
                    description = value;
                    continue;
                case "status":
                    if (listOfSubTaskId.size() != 0) {
                        status = getterEpicTaskStatus(listOfSubTaskId);
                    }
                    continue;
                case "startTime":
                    if (listOfSubTaskId.size() != 0) {
                        startTime = getterEpicTaskStartTime(listOfSubTaskId);
                    }
                    continue;
                case "duration":
                    if (listOfSubTaskId.size() != 0) {
                        duration = getterEpicTaskDuration(listOfSubTaskId);
                    }
                    continue;
                default:
                    break;
            }
        }
        return new EpicTask(name, description, listOfSubTaskId, status, startTime, duration);
    }

    private EpicTask.SubTask subtaskFromRequest(String body) {
        int epicTaskId = 0;
        String[] keyValueArray;
        String name = null;
        String description = null;
        Task.Status status = null;
        LocalDateTime startTime = null;
        long duration = 0;

        String[] stringArray = body
                .replaceFirst("\\{\\s*\"", "")
                .replaceAll("\"", "")
                .replaceFirst("\\s*}", "")
                .split(",\\s*");

        for (String string : stringArray) {
            keyValueArray = string.split(": ");
            String key = keyValueArray[0];
            String value = keyValueArray[1];

            switch (key) {
                case "epicTaskId":
                    epicTaskId = Integer.parseInt(value);
                    continue;
                case "name":
                    name = value;
                    continue;
                case "description":
                    description = value;
                    continue;
                case "status":
                    status = Task.Status.valueOf(value);
                    continue;
                case "startTime":
                    if (!value.equals("null")) {
                        startTime = LocalDateTime.parse(value, formatter);
                    }
                    continue;
                case "duration":
                    duration = Long.parseLong(value);
                    continue;
                default:
                    break;
            }
        }
        return new EpicTask.SubTask(epicTaskId, name, description, status, startTime, duration);
    }

    /**
     * Сохраняет менеджер истории в CSV
     */
    private static String historyManagerToString(HistoryManager manager) {
        List<Integer> listOfIdsFromHistory = new ArrayList<>();
        List<Task> list = manager.getTaskHistory();
        for (Task task : list) {
            listOfIdsFromHistory.add(task.getId());
        }

        return listOfIdsFromHistory
                .toString()
                .replaceFirst("\\[", "")
                .replaceFirst("]", "");
    }

    /**
     * Метод для восстановления истории задач из строки CSV
     */
    private static List<Integer> fromStringToHistoryManager(String value) {
        List<Integer> listOfIdsFromHistory = new ArrayList<>();
        if (value.matches("^\\d+$")) {
            listOfIdsFromHistory.add(Integer.parseInt(value));
        } else if (value.matches("^\\d+, \\d+.*")) {
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
    private void saveToCSV() {
        String firstColumn = "id";
        String secondColumn = "type";
        String thirdColumn = "name";
        String fourthColumn = "status";
        String fifthColumn = "description";
        String sixthColumn = "epic";
        String seventhColumn = "startTime";
        String eighthColumn = "duration";
        String ninthColumn = "endTime";
        String tableHeader = String.format("%s, %s, %s, %s , %s, %s, %s, %s, %s,\n", firstColumn, secondColumn
                , thirdColumn, fourthColumn, fifthColumn, sixthColumn, seventhColumn, eighthColumn, ninthColumn);

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
     * Добавляет id подзадачи в список id подзадач Epic задачи, а id Epic задачи в подзадачу
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
        saveToCSV();
    }

    @Override
    public void saveEpicTask(EpicTask epicTask) {
        super.saveEpicTask(epicTask);
        saveToCSV();
    }

    @Override
    public void saveSubTask(EpicTask.SubTask subTask) {
        super.saveSubTask(subTask);
        saveToCSV();
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
        saveToCSV();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        saveToCSV();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        saveToCSV();
    }

    /**
     * Получение задачи по идентификатору
     */
    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        saveToCSV();
        return task;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask epicTask = super.getEpicTaskById(id);
        saveToCSV();
        return epicTask;
    }

    @Override
    public EpicTask.SubTask getSubTaskById(int id) {
        EpicTask.SubTask subTask = super.getSubTaskById(id);
        saveToCSV();
        return subTask;
    }

    public Task getTaskByIdFromString(int id) {
        return super.getTaskById(id);
    }

    public EpicTask getEpicTaskByIdFromString(int id) {
        return super.getEpicTaskById(id);
    }

    public EpicTask.SubTask getSubTaskByIdFromString(int id) {
        return super.getSubTaskById(id);
    }

    /**
     * Создание задачи
     */
    @Override
    public Task createTask(Task task) throws ManagerCreateException {
        Task newTask = super.createTask(task);
        saveTask(newTask);
        return newTask;
    }

    @Override
    public EpicTask createTask(EpicTask epicTask) {
        EpicTask newEpicTask = super.createTask(epicTask);
        saveEpicTask(newEpicTask);
        return newEpicTask;
    }

    @Override
    public EpicTask.SubTask createTask(EpicTask.SubTask subTask) throws ManagerCreateException {
        EpicTask.SubTask newSubTask = super.createTask(subTask);
        saveSubTask(newSubTask);
        return newSubTask;
    }

    /**
     * Обновление задачи
     */
    @Override
    public void updateTask(Task task) throws ManagerCreateException {
        super.updateTask(task);
        saveToCSV();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws ManagerCreateException {
        super.updateEpicTask(epicTask);
        saveToCSV();
    }

    @Override
    public void updateSubTask(EpicTask.SubTask subTask) throws ManagerCreateException {
        super.updateSubTask(subTask);
        saveToCSV();
    }

    public void updateTaskFromString(Task task) throws ManagerCreateException {
        super.updateTask(task);
    }

    public void updateEpicTaskFromString(EpicTask epicTask) throws ManagerCreateException {
        super.updateEpicTask(epicTask);
    }

    public void updateSubTaskFromString(EpicTask.SubTask subTask) throws ManagerCreateException {
        super.updateSubTask(subTask);
    }

    /**
     * Удаление задачи
     */
    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        saveToCSV();
    }

    @Override
    public void removeEpicTaskById(int id) {
        super.removeEpicTaskById(id);
        saveToCSV();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        saveToCSV();
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
    public List<Task> getListOfTaskHistory() {
        return super.getListOfTaskHistory();
    }

    @Override
    public void removeTaskFromTaskHistory(int id) {
        super.removeTaskFromTaskHistory(id);
        saveToCSV();
    }

    /**
     * Метод для управления статусом для EpicTask задач
     */
    @Override
    public Task.Status getterEpicTaskStatus(List<Integer> listOfSubTaskId) {
        return super.getterEpicTaskStatus(listOfSubTaskId);
    }

    /**
     * Метод для управления датой, когда предполагается приступить к выполнению задачи
     */
    @Override
    public LocalDateTime getterEpicTaskStartTime(List<Integer> listOfSubTaskId) {
        return super.getterEpicTaskStartTime(listOfSubTaskId);
    }

    /**
     * Метод для управления продолжительностью(в минутах) задачи для EpicTask задач
     */
    @Override
    public long getterEpicTaskDuration(List<Integer> listOfSubTaskId) {
        return super.getterEpicTaskDuration(listOfSubTaskId);
    }

    /**
     * Метод для управления датой, когда предполагается закончить выполнение задачи
     */
    @Override
    public LocalDateTime getterEpicTaskEndTime(List<Integer> listOfSubTaskId) {
        return super.getterEpicTaskEndTime(listOfSubTaskId);
    }

    /**
     * Метод для возвращения списка задач и подзадач в заданном порядке
     */
    @Override
    public Set<Task> getterPrioritizedTasks() {
        return super.getterPrioritizedTasks();
    }

    public Task getterTaskFromRequest(String body) {
        return taskFromRequest(body);
    }

    public EpicTask getterEpicTaskFromRequest(String body) {
        return epictaskFromRequest(body);
    }

    public EpicTask.SubTask getterSubTaskFromRequest(String body) {
        return subtaskFromRequest(body);
    }
}