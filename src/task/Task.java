package task;

import java.util.Objects;

/**
 * Класс для создания Task задач
 */
public class Task {
    private final String name;
    private final String description;
    private int id;
    private Status status;

    public Task(String taskName, String taskDescription, Status taskStatus) {
        this.name = taskName;
        this.description = taskDescription;
        this.status = taskStatus;
    }

    /**
     * Конструктор для обновления Task задач
     */
    public Task(int taskId, String taskName, String taskDescription, Status taskStatus) {
        this.id = taskId;
        this.name = taskName;
        this.description = taskDescription;
        this.status = taskStatus;
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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && description.equals(task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "}\n";
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