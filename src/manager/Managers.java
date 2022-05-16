package manager;

import task.EpicTask;
import task.Task;

import java.util.ArrayList;

public class Managers {
    public static TaskManager getDefault() {
        return new TaskManager() {
            @Override
            public void saveToTaskStorage(Task task) {

            }

            @Override
            public void saveToTaskStorage(EpicTask epicTask) {

            }

            @Override
            public void saveToTaskStorage(EpicTask.SubTask subTask) {

            }

            @Override
            public ArrayList<Task> getListOfTasks() {
                return null;
            }

            @Override
            public ArrayList<EpicTask> getListOfEpicTasks() {
                return null;
            }

            @Override
            public ArrayList<EpicTask.SubTask> getListOfSubTasks() {
                return null;
            }

            @Override
            public void deleteAllTasks() {

            }

            @Override
            public void deleteAllEpicTasks() {

            }

            @Override
            public Task getTaskById(int id) {
                return null;
            }

            @Override
            public EpicTask getEpicTaskById(int id) {
                return null;
            }

            @Override
            public EpicTask.SubTask getSubTaskById(int id) {
                return null;
            }

            @Override
            public Task createCopyOfTask(Task task) {
                return null;
            }

            @Override
            public EpicTask createCopyOfTask(EpicTask epicTask) {
                return null;
            }

            @Override
            public EpicTask.SubTask createCopyOfTask(EpicTask.SubTask subTask) {
                return null;
            }

            @Override
            public void updateTask(Task task) {

            }

            @Override
            public void updateTask(EpicTask epicTask) {

            }

            @Override
            public void updateTask(EpicTask.SubTask subTask) {

            }

            @Override
            public void removeTaskById(int id) {

            }

            @Override
            public void removeEpicTaskById(int id) {

            }

            @Override
            public void removeSubTaskById(int id) {

            }

            @Override
            public void addSubtaskToEpicTask(EpicTask.SubTask subTask, EpicTask epicTask) {

            }

            @Override
            public Task.Status getEpicTaskStatus(ArrayList<Integer> listOfSubTaskId) {
                return null;
            }
        };

    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
