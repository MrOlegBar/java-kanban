package task;

import manager.Manager;

public class Task {
    /**
     * Класс для создания Task.Task задач
     */
    private final int id;
    private final String name;
    private final String description;
    private Manager.Status status;
    /**
     * Конструктор для создания Task.Task задач
     */
    public Task(String nameTask, String descriptionTask, Manager.Status statusTask) {
        this.id = Manager.getId() + 1;
        Manager.setId(this.id);
        this.name = nameTask;
        this.description = descriptionTask;
        this.status = statusTask;
    }
    /**
     * Конструктор для создания задач наследников Epic задач и SubTask подзадач
     */
    public Task(String nameTask, String descriptionTask) {
        this.id = Manager.getId() + 1;
        Manager.setId(this.id);
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

    @Override
    public String toString() {
        return "ID задачи Task.Task=\"" + id + "\", Название задачи=\"" + name + "\", Описание=\"" + description
                + "\", Статус=\"" + status + "\"";
    }
}