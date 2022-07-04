package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Класс для создания Task задач
 */
public class Task {
    private final String name;
    private final String description;
    private int id;
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

    /**
     * Метод для получения времени завершения задачи
     */
    public LocalDateTime getEndTime() {
        if (this.startTime != null) {
            return this.startTime.plusMinutes(this.duration);
        } else {
            return null;
        }
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

    public String getDescription() {
        return description;
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

    public long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", duration=" + duration +
                ", endTime=" + endTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && name.equals(task.name) && description.equals(task.description) && status == task.status && Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, startTime, duration, endTime);
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