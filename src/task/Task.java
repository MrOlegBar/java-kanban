package task;

import manager.Manager;
import java.util.Objects;

public class Task {
    /**
     * Класс для создания Task задач
     */
    private final int id;
    private final String name;
    private final String description;
    private Manager.Status status;
    /**
     * Конструктор для создания Task задач
     * Суперконструктор для SubTask подзадач
     */
    public Task(String nameTask, String descriptionTask, Manager.Status statusTask) {
        this.id = Manager.getId() + 1;
        Manager.setId(this.id);
        this.name = nameTask;
        this.description = descriptionTask;
        this.status = statusTask;
    }
    /**
     * Конструктор для обновления Task задач
     * Суперконструктор для обновления SubTask подзадач
     */
    public Task(int id, String nameTask, String descriptionTask, Manager.Status statusTask) {
        this.id = id;
        this.name = nameTask;
        this.description = descriptionTask;
        this.status = statusTask;
    }
    /**
     * Суперконструктор для создания Epic задач
     */
    public Task(String nameTask, String descriptionTask) {
        this.id = Manager.getId() + 1;
        Manager.setId(this.id);
        this.name = nameTask;
        this.description = descriptionTask;
    }
    /**
     * Суперконструктор для обновления EpicTask задач
     */
    public Task(int id, String nameTask, String descriptionTask) {
        this.id = id;
        this.name = nameTask;
        this.description = descriptionTask;
    }
    /**
     * Конструктор для копирования Task.Task задач
     */
    public Task(Task task) {
        this(task.name, task.description, task.status);
    }
    /**
     * get и set методы
     */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Manager.Status getStatus() {
        return status;
    }

    public void setStatus(Manager.Status status) {
        this.status = status;
    }
    /**
     * Переопределенные методы класса Objects для Task задач
     */
    @Override
    public String toString() {
        return "ID задачи Task=\"" + id + "\", Название задачи=\"" + name + "\", Описание=\"" + description
                + "\", Статус=\"" + status + "\"";
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
}