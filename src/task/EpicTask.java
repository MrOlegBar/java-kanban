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
    private List<Integer> listOfSubTaskId;

    public EpicTask(String epicTaskName
            , String epicTaskDescription
            , List<Integer> listOfSubTaskIdOfTheEpicTask
            , Status epicTaskStatus
            , LocalDateTime startTime
            , long duration) {
        super(epicTaskName, epicTaskDescription, epicTaskStatus, startTime, duration);
        this.listOfSubTaskId = new ArrayList<>(listOfSubTaskIdOfTheEpicTask);
    }

    /**
     * Конструктор для обновления Epic задач
     */
    public EpicTask(int epicTaskId
            , String epicTaskName
            , String epicTaskDescription
            , List<Integer> listOfSubTaskIdOfTheEpicTask
            , Status epicTaskStatus
            , LocalDateTime startTime
            , long duration) {
        super(epicTaskName, epicTaskDescription, epicTaskStatus, startTime, duration);
        this.setId(epicTaskId);
        this.listOfSubTaskId = listOfSubTaskIdOfTheEpicTask;
    }

    public List<Integer> getListOfSubTaskId() {
        return listOfSubTaskId;
    }

    public void setListOfSubTaskId(List<Integer> listOfSubTaskId) {
        this.listOfSubTaskId = listOfSubTaskId;
    }

    @Override
    public String toString() {
        String returnString = null;
        try {
            if (getStartTime() != null) {
                returnString = "EpicTask{" +
                        "id='" + getId() + '\'' +
                        ", name='" + getName() + '\'' +
                        ", description='" + getDescription() + '\'' +
                        ", listOfSubTaskId='" + listOfSubTaskId + '\'' +
                        ", status=" + getStatus() +
                        ", startTime=" + getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                        ", duration=" + getDuration() +
                        ", endTime=" + getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                        "}\n";
            } else {
                returnString = "EpicTask{" +
                        "id='" + getId() + '\'' +
                        ", name='" + getName() + '\'' +
                        ", description='" + getDescription() + '\'' +
                        ", listOfSubTaskId='" + listOfSubTaskId + '\'' +
                        ", status=" + getStatus() +
                        ", startTime=" + null +
                        ", duration=" + getDuration() +
                        ", endTime=" + null +
                        "}\n";
            }
        } catch (NullPointerException e) {
            System.out.println("Ошибка формата объектов LocalDateTime: " + e.getMessage());
        }
        return returnString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(listOfSubTaskId, epicTask.listOfSubTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listOfSubTaskId);
    }

    /**
     * Внутренний класс для создания SubTask подзадач
     */
    public static class SubTask extends Task {
        private int epicTaskId;

        public SubTask(int epicTaskId
                , String subTaskName
                , String subTaskDescription
                , Status subTaskStatus
                , LocalDateTime startTime
                , long duration) {
            super(subTaskName, subTaskDescription, subTaskStatus, startTime, duration);
            this.epicTaskId = epicTaskId;
        }

        /**
         * Конструктор внутреннего класса для обновления SubTask подзадач
         */
        public SubTask(int subTaskId
                , int epicTaskId
                , String subTaskName
                , String subTaskDescription
                , Status subTaskStatus
                , LocalDateTime startTime
                , long duration) {
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
            String returnString = null;
            try {
                if (getStartTime() != null) {
                    returnString = "SubTask{" +
                            "id='" + getId() + '\'' +
                            ", epicTaskId='" + epicTaskId + '\'' +
                            ", name='" + getName() + '\'' +
                            ", description='" + getDescription() + '\'' +
                            ", status=" + getStatus() +
                            ", startTime=" + getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                            ", duration=" + getDuration() +
                            ", endTime=" + getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                            "}\n";
                } else {
                    returnString = "SubTask{" +
                            "id='" + getId() + '\'' +
                            ", epicTaskId='" + epicTaskId + '\'' +
                            ", name='" + getName() + '\'' +
                            ", description='" + getDescription() + '\'' +
                            ", status=" + getStatus() +
                            ", startTime=" + null +
                            ", duration=" + getDuration() +
                            ", endTime=" + null +
                            "}\n";
                }
            } catch (NullPointerException e) {
                System.out.println("Ошибка формата объектов LocalDateTime: " + e.getMessage());
            }
            return returnString;
        }
    }
}