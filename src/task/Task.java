package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Класс для создания Task задач
 */
public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime startTime;
    private long duration;

    private LocalDateTime endTime;

    public Task(String taskName, String taskDescription, Status taskStatus, LocalDateTime startTime, long duration) {
        this.name = taskName;
        this.description = taskDescription;
        this.status = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    /**
     * Конструктор для обновления Task задач
     */
    public Task(int taskId, String taskName, String taskDescription, Status taskStatus, LocalDateTime startTime
            , long duration) {
        this.id = taskId;
        this.name = taskName;
        this.description = taskDescription;
        this.status = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (this.startTime != null) {
            return this.startTime.plusMinutes(this.duration);
        } else {
            return null;
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && name.equals(task.name)
                && description.equals(task.description) && status == task.status
                && Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime);
    }

    /**
     * Constructs a new object.
     */
    public Task() {
        super();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, startTime, duration, endTime);
    }

    @Override
    public String toString() {
        String returnString = null;
        try {
            if (startTime != null) {
                 returnString = "Task{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", description='" + description + '\'' +
                        ", status=" + status +
                        ", startTime=" + startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                        ", duration=" + duration +
                        ", endTime=" + endTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                        "}\n";
            } else {
                returnString = "Task{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", description='" + description + '\'' +
                        ", status=" + status +
                        ", startTime=" + null +
                        ", duration=" + duration +
                        ", endTime=" + null +
                        "}\n";
            }
        } catch (NullPointerException e) {
            System.out.println("Ошибка формата объектов LocalDateTime: " + e.getMessage());
        }
        return returnString;
    }

    /**
     * Перечисление статусов задач
     */
    public enum Status {
        NEW,
        DONE,
        IN_PROGRESS
    }

    /**
     * Перечисление типов задач
     */
    public enum Type {
        TASK,
        EPICTASK,
        SUBTASK
    }
}