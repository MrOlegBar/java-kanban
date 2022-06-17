package task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс для создания Epic задач
 */
public class EpicTask extends Task {
    private final ArrayList<Integer> listOfSubTaskId;

    public EpicTask(String epicTaskName, String epicTaskDescription, ArrayList<Integer> listOfSubTaskIdOfTheEpicTask
            , Status epicTaskStatus) {
        super(epicTaskName, epicTaskDescription, epicTaskStatus);
        this.listOfSubTaskId = new ArrayList<>(listOfSubTaskIdOfTheEpicTask);
    }

    /**
     * Конструктор для обновления Epic задач
     */
    public EpicTask(int epicTaskId, String epicTaskName, String epicTaskDescription
            , ArrayList<Integer> listOfSubTaskIdOfTheEpicTask, Status epicTaskStatus) {
        super(epicTaskName, epicTaskDescription, epicTaskStatus);
        this.setId(epicTaskId);
        this.listOfSubTaskId = listOfSubTaskIdOfTheEpicTask;
    }

    public ArrayList<Integer> getListOfSubTaskId() {
        return listOfSubTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return listOfSubTaskId.equals(epicTask.listOfSubTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listOfSubTaskId);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", listOfSubTaskId='" + listOfSubTaskId + '\'' +
                ", status='" + getStatus() + '\'' +
                "}\n";
    }

    /**
     * Внутренний класс для создания SubTask подзадач
     */
    public static class SubTask extends Task {
        private int epicTaskId;

        public SubTask(int epicTaskId, String subTaskName, String subTaskDescription, Status subTaskStatus) {
            super(subTaskName, subTaskDescription, subTaskStatus);
            this.epicTaskId = epicTaskId;
        }

        /**
         * Конструктор внутреннего класса для обновления SubTask подзадач
         */
        public SubTask(int subTaskId, int epicTaskId, String subTaskName, String subTaskDescription
                , Status subTaskStatus) {
            super(subTaskName, subTaskDescription, subTaskStatus);
            this.setId(subTaskId);
            this.epicTaskId = epicTaskId;
        }

        public int getEpicTaskId() {
            return epicTaskId;
        }

        public void setEpicTaskId(int epicTaskId) {
            this.epicTaskId = epicTaskId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            SubTask subTask = (SubTask) o;
            return epicTaskId == subTask.epicTaskId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), epicTaskId);
        }

        @Override
        public String toString() {
            return "SubTask{" +
                    "id='" + getId() + '\'' +
                    ", epicTaskId='" + epicTaskId + '\'' +
                    ", name='" + getName() + '\'' +
                    ", description='" + getDescription() + '\'' +
                    ", status='" + getStatus() + '\'' +
                    "}\n";
        }
    }
}