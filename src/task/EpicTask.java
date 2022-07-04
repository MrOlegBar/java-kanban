package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс для создания Epic задач
 */
public class EpicTask extends Task {
    private final List<Integer> listOfSubTaskId;

    public EpicTask(String epicTaskName, String epicTaskDescription, List<Integer> listOfSubTaskIdOfTheEpicTask
            , Status epicTaskStatus, LocalDateTime startTime, long duration) {
        super(epicTaskName, epicTaskDescription, epicTaskStatus, startTime, duration);
        this.listOfSubTaskId = new ArrayList<>(listOfSubTaskIdOfTheEpicTask);
    }

    /**
     * Конструктор для обновления Epic задач
     */
    public EpicTask(int epicTaskId, String epicTaskName, String epicTaskDescription
            , List<Integer> listOfSubTaskIdOfTheEpicTask, Status epicTaskStatus, LocalDateTime startTime
            , long duration) {
        super(epicTaskName, epicTaskDescription, epicTaskStatus, startTime, duration);
        this.setId(epicTaskId);
        this.listOfSubTaskId = listOfSubTaskIdOfTheEpicTask;
    }

    public List<Integer> getListOfSubTaskId() {
        return listOfSubTaskId;
    }

    @Override
    public String toString() {
        String startTime = null;
        String endTime = null;
        if (getStartTime() != null) {
            startTime = getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
        if (getEndTime() != null) {
            endTime = getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
        return "EpicTask{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", listOfSubTaskId='" + listOfSubTaskId + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + startTime +
                ", duration=" + getDuration() +
                ", endTime=" + endTime +
                "}\n";
    }

    /**
     * Внутренний класс для создания SubTask подзадач
     */
    public static class SubTask extends Task {
        private int epicTaskId;

        public SubTask(int epicTaskId, String subTaskName, String subTaskDescription, Status subTaskStatus
                , LocalDateTime startTime, long duration) {
            super(subTaskName, subTaskDescription, subTaskStatus, startTime, duration);
            this.epicTaskId = epicTaskId;
        }

        /**
         * Конструктор внутреннего класса для обновления SubTask подзадач
         */
        public SubTask(int subTaskId, int epicTaskId, String subTaskName, String subTaskDescription
                , Status subTaskStatus, LocalDateTime startTime, long duration) {
            super(subTaskName, subTaskDescription, subTaskStatus, startTime, duration);
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
                    ", status=" + getStatus() +
                    ", startTime=" + getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                    ", duration=" + getDuration() +
                    ", endTime=" + getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                    "}\n";
        }
    }
}