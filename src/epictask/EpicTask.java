package epictask;

import manager.Manager;
import task.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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
        Manager.Status statusEpicTask = getEpicTaskStatus(); // 4. Метод для управления статусом для эпик задач.
        this.setStatus(statusEpicTask);
    }
    /**
     * Конструктор для обновления Epic задач
     */
    public EpicTask(int id, String nameEpicTask, String descriptionEpicTask, ArrayList<SubTask> subTasks) {
        super(id, nameEpicTask, descriptionEpicTask);
        this.subTasks = subTasks;
        Manager.Status statusEpicTask = getEpicTaskStatus(); // 4. Метод для управления статусом для эпик задач.
        this.setStatus(statusEpicTask);
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
     * 4. Метод для управления статусом для EpicTask задач
     */
    private Manager.Status getEpicTaskStatus() {
        Manager.Status statusEpicTask;
        int countNew = 0;
        int countDone = 0;

        for (EpicTask.SubTask subTask : this.subTasks) {
            if (subTask.getStatus().equals(Manager.Status.NEW)) {
                countNew++;
            }
            if (subTask.getStatus().equals(Manager.Status.DONE)) {
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
    /**
     * Переопределенные методы класса Objects для EpicTask задач
     */
    @Override
    public String toString() {
        return "ID задачи Epic=\"" + getId() + "\", Название Epic задачи=\"" + getName() + "\", Описание=\"" + getDescription() + "\""
                + ", " + Arrays.toString(subTasks.toArray()) + ", Статус=\"" + getStatus() + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return subTasks.equals(epicTask.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }

    /**
     * Внутренний класс для создания SubTask подзадач
     */
    public static class SubTask extends Task {

        private final String nameEpicTask;
        /**
         * Конструктор внутреннего класса для создания SubTask подзадач
         */
        public SubTask(String nameEpicTask, String nameSubTask, String descriptionSubTask, Manager.Status statusSubTask) {
            super(nameSubTask, descriptionSubTask, statusSubTask);
            this.nameEpicTask = nameEpicTask;
        }
        /**
         * Конструктор для обновления SubTask подзадач
         */
        public SubTask(int id, String nameEpicTask, String nameSubTask, String descriptionSubTask, Manager.Status statusSubTask) {
            super(id, nameSubTask, descriptionSubTask, statusSubTask);
            this.nameEpicTask = nameEpicTask;
        }
        /**
         * Конструктор для копирования SubTask подзадач
         */
        public SubTask(SubTask subtask) {
            this(subtask.nameEpicTask, subtask.getName(), subtask.getDescription(), subtask.getStatus());
        }
        /**
         * get метод
         */
        public String getNameEpicTask() {
            return nameEpicTask;
        }
        /**
         * Переопределенные методы класса Objects для SubTask подзадач
         */
        @Override
        public String toString() {
            return "ID подзадачи SubTask=\"" + getId() + "\", Название Epic задачи=\"" + nameEpicTask
                    + "\", Название подзадачи=\"" + getName() + "\", Описание=\"" + getDescription() + "\", Статус=\"" + getStatus()
                    + "\"";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            SubTask subTask = (SubTask) o;
            return nameEpicTask.equals(subTask.nameEpicTask);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), nameEpicTask);
        }
    }
}