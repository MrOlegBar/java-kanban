package epictask;

import manager.Manager;
import task.Task;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Класс для создания Epic задач
 */
public class EpicTask extends Task {
    private ArrayList<SubTask> subTasks;

    /**
     * Конструктор для создания Epic задач
     */
    public EpicTask(String nameEpicTask, String descriptionEpicTask, ArrayList<SubTask> subTasks) {
        super(nameEpicTask, descriptionEpicTask);
        this.subTasks = subTasks;
        this.setStatus(getEpicTaskStatus()); // 4. Метод для управления статусом для эпик задач.
    }

    /**
     * Конструктор для копирования Epic задач
     */
    public EpicTask(EpicTask epicTask) {
        this(epicTask.getName(), epicTask.getDescription(), epicTask.subTasks);
    }
    /**
     * get метод
     */
    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    /**
     * 4. Метод для управления статусом для эпик задач.
     */
    public Manager.Status getEpicTaskStatus() {
        Manager.Status statusEpicTask;
        int countNew = 0;
        int countDone = 0;

        for (EpicTask.SubTask subTask : this.subTasks) {
            if (subTask.getStatus().equals(Manager.Status.NEW)) {
                countNew++;
            }
            if (!subTask.getStatus().equals(Manager.Status.DONE)) {
                countDone++;
            }
        }

        if ((subTasks.isEmpty()) || (countNew == subTasks.size())) {
            statusEpicTask = Manager.Status.NEW;
        } else if (countDone == subTasks.size()) {
            statusEpicTask = Manager.Status.DONE;
        } else {
            statusEpicTask = Manager.Status.IN_PROGRESS;
        }
        return statusEpicTask;
    }

    @Override
    public String toString() {
        return "ID задачи Epic=\"" + getId() + "\", Название Epic задачи=\"" + getName() + "\", Описание=\"" + getDescription() + "\""
                + ", " + Arrays.toString(subTasks.toArray()) + ", Статус=\"" + getStatus() + "\"";
    }
    /**
     * Внутренний класс для создания SubTask подзадач Epic задач
     */
    public static class SubTask extends Task {
        private final String nameEpicTask;
        /**
         * Конструктор внутреннего класса для создания SubTask подзадач Epic задач
         */
        public SubTask(String nameEpicTask, String nameSubTask, String descriptionSubTask, Manager.Status statusSubTask) {
            super(nameSubTask, descriptionSubTask, statusSubTask);
            this.nameEpicTask = nameEpicTask;
        }
        /**
         * Конструктор для копирования SubTask подзадач Epic задач
         */
        public SubTask(SubTask subtask) {
            this(subtask.nameEpicTask, subtask.getName(), subtask.getDescription(), subtask.getStatus());
        }
        /**
         * get метод
         */

        @Override
        public String toString() {
            return "ID подзадачи SubTask=\"" + getId() + "\", Название Epic задачи=\"" + nameEpicTask
                    + "\", Название подзадачи=\"" + getName() + "\", Описание=\"" + getDescription() + "\", Статус=\"" + getStatus()
                    + "\"";
        }
    }
}