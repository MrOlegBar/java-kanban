package manager;

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
        Task firstTask = manager.createTask(new Task("Поесть", "Принять пищу", NEW, LocalDateTime.now().minusMinutes(30L)
                , 30L));

        manager.saveTask(firstTask);

        Task secondTask = manager.createTask(new Task("Поспать", "Хорошенько выспаться", DONE, LocalDateTime.now().plusMinutes(30L)
                , 600L));
        manager.saveTask(secondTask);
        System.out.println("    Создали 2е Task задачи:");
        System.out.println(manager.getListOfTasks());

        /**
         * Создали 1у EpicTask задачу с 3мя SubTask подзадачами
         */
        List<Integer> listOfSubtaskIdOfTheFirstEpicTask = new ArrayList<>();
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
        System.out.println(manager.getListOfEpicTasks());

        /**
         * Создали 2ю EpicTask задачу без SubTask подзадач
         */
        List<Integer> listOfSubtaskIdOfTheSecondEpicTask = new ArrayList<>();
        Task.Status statusOfTheSecondEpicTask = manager.getterEpicTaskStatus(listOfSubtaskIdOfTheSecondEpicTask);
        LocalDateTime startTimeOfTheSecondEpicTask = manager.getterEpicTaskStartTime(listOfSubtaskIdOfTheSecondEpicTask);
        long durationOfTheSecondEpicTask = manager.getterEpicTaskDuration(listOfSubtaskIdOfTheSecondEpicTask);
        EpicTask secondEpicTask = manager.createTask(new EpicTask("Сменить работу", "Начать работать Java разработчиком"
                , listOfSubtaskIdOfTheSecondEpicTask, statusOfTheSecondEpicTask, startTimeOfTheSecondEpicTask
        , durationOfTheSecondEpicTask));
        manager.saveEpicTask(secondEpicTask);

        System.out.println("    Создали 2ю EpicTask задачу без SubTask подзадач:");
        System.out.println(manager.getListOfEpicTasks());

        System.out.println("\n    -Получение по идентификатору:");
        try {
            System.out.println(manager.getTaskById(1));
            System.out.println(manager.getTaskById(2));

            System.out.println(manager.getEpicTaskById(3));
            System.out.println(manager.getSubTaskById(4));
            System.out.println(manager.getSubTaskById(5));
            System.out.println(manager.getSubTaskById(6));
            System.out.println(manager.getEpicTaskById(7));
            System.out.println("    -История просмотров задач:");
            System.out.println(manager.getTaskHistory());

            FileBackedTasksManager recoveryManager = FileBackedTasksManager.loadFromFile(new File("Autosave.csv"));

            System.out.println("    Восстановленный список всех задач:");
            System.out.println(recoveryManager.getListOfTasks());
            System.out.println(recoveryManager.getListOfEpicTasks());
            System.out.println(recoveryManager.getListOfSubTasks());
            System.out.println("    Восстановленная История просмотров задач:");
            System.out.println(recoveryManager.getTaskHistory());

            System.out.println("    Метод для возвращения списка задач и подзадач в заданном порядке:");
            System.out.println(manager.getterPrioritizedTasks());
        } catch (NullPointerException | ClassCastException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для восстановления данных менеджера из файла
     */
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager localManager = new FileBackedTasksManager(file);
        List<Integer> taskHistoryIds = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String autosaveFileLine;
            while ((autosaveFileLine = bufferedReader.readLine()) != null) {

                if ((autosaveFileLine.matches(".* Task.*"))) {
                    Task restoredTask = fromString(autosaveFileLine);
                    localManager.updateTask(restoredTask);
                    localManager.setId(restoredTask.getId() + 1);
                }

                if ((autosaveFileLine.matches(".* EpicTask.*"))) {
                    EpicTask restoredEpicTask = (EpicTask) fromString(autosaveFileLine);
                    localManager.updateEpicTask(restoredEpicTask);
                    localManager.setId(restoredEpicTask.getId() + 1);
                }

                if ((autosaveFileLine.matches(".* SubTask.*"))) {
                    EpicTask.SubTask restoredSubTask = (EpicTask.SubTask) fromString(autosaveFileLine);
                    EpicTask epicTaskForSubTask = localManager.getEpicTaskById(restoredSubTask.getEpicTaskId());
                    List<Integer> listOfSubTaskId = epicTaskForSubTask.getListOfSubTaskId();
                    listOfSubTaskId.add(restoredSubTask.getId());

                    localManager.updateSubTask(restoredSubTask);
                    localManager.updateEpicTask(epicTaskForSubTask);
                    localManager.setId(restoredSubTask.getId() + 1);

                }

                if (autosaveFileLine.matches("^\\d, \\d.*")) {
                    taskHistoryIds.addAll(fromStringToHistoryManager(autosaveFileLine));
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        for (Integer id : taskHistoryIds) {
            Task task = localManager.getTaskById(id);
            EpicTask epicTask = localManager.getEpicTaskById(id);
            EpicTask.SubTask subTask = localManager.getSubTaskById(id);

            if (task != null) {
                inMemoryHistoryManager.addTaskToTaskHistory(task);
            }

            if (epicTask != null) {
                inMemoryHistoryManager.addTaskToTaskHistory(epicTask);
            }

            if (subTask != null) {
                inMemoryHistoryManager.addTaskToTaskHistory(subTask);
            }
        }
        return localManager;
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
        String startTime = task.getStartTime().format(formatter);
        String endTime = task.getEndTime().format(formatter);
        return String.format("%s, %s, %s, %s, %s, -, %s, %s, %s", task.getId(), type, task.getName(), task.getStatus()
                , task.getDescription(), startTime, task.getDuration(), endTime);
    }

    private String epicTaskToString(EpicTask epicTask) {
        String type = String.valueOf(epicTask.getClass()).replace("class task.", "");
        String startTime = null;
        String endTime = null;
        if (epicTask.getStartTime() != null) {
            startTime = epicTask.getStartTime().format(formatter);
            endTime = getterEpicTaskEndTime(epicTask.getListOfSubTaskId()).format(formatter);
        }
        return String.format("%s, %s, %s, %s, %s, -, %s, %s, %s", epicTask.getId(), type, epicTask.getName()
                , epicTask.getStatus(), epicTask.getDescription(), startTime, epicTask.getDuration(), endTime);
    }

    private String subTaskToString(EpicTask.SubTask subTask) {
        String type = String.valueOf(subTask.getClass()).replace("class task.EpicTask$", "");
        String startTime = subTask.getStartTime().format(formatter);
        String endTime = subTask.getEndTime().format(formatter);
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s", subTask.getId(), type, subTask.getName()
                , subTask.getStatus(), subTask.getDescription(), subTask.getEpicTaskId(), startTime
                , subTask.getDuration(), endTime);
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

    /**
     * Метод для сохранения менеджера истории в CSV
     */
    private static String historyManagerToString(HistoryManager manager) {
        List<Integer> listOfIdsFromHistory = new ArrayList<>();
        List<Task> list = manager.getTaskHistory();
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
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public EpicTask createTask(EpicTask epicTask) {
        EpicTask newEpicTask = super.createTask(epicTask);
        save();
        return newEpicTask;
    }

    @Override
    public EpicTask.SubTask createTask(EpicTask.SubTask subTask) {
        EpicTask.SubTask newSubTask = super.createTask(subTask);
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
    public List<Task> getTaskHistory() {
        return super.getTaskHistory();
    }

    @Override
    public void removeTaskFromTaskHistory(int id) {
        super.removeTaskFromTaskHistory(id);
        save();
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
}