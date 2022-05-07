package task;

import java.util.ArrayList;

/**
 * Класс для создания Epic задач
 */
public class EpicTask extends Task {
    private ArrayList<Integer> listOfSubTaskId;

    public ArrayList<Integer> getListOfSubTaskId() {
        return listOfSubTaskId;
    }
    /**
     * Конструктор для создания Epic задач
     */
    public EpicTask(String epicTaskName, String epicTaskDescription, ArrayList<Integer> listOfSubTaskIdOfTheEpicTask
            , Status epicTaskStatus) {
        super(epicTaskName, epicTaskDescription, epicTaskStatus);
        this.listOfSubTaskId = listOfSubTaskIdOfTheEpicTask;
    }
    /**
     * Конструктор для обновления Epic задач
     */
    public EpicTask(Integer epicTaskId, String epicTaskName, String epicTaskDescription
            , ArrayList<Integer> listOfSubTaskIdOfTheEpicTask, Status epicTaskStatus) {
        super(epicTaskName, epicTaskDescription, epicTaskStatus);
        this.setId(epicTaskId);
        this.listOfSubTaskId = listOfSubTaskIdOfTheEpicTask;
    }
    /**
     * Конструктор для копирования Epic задач
     */
    public EpicTask(EpicTask epicTask) {
        this(epicTask.getName(), epicTask.getDescription(), epicTask.listOfSubTaskId, epicTask.getStatus());
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", listOfSubTaskId='" + listOfSubTaskId + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
    /**
     * Внутренний класс для создания SubTask подзадач
     */
    public static class SubTask extends Task {
        private Integer epicTaskId;

        public Integer getEpicTaskId() {
            return epicTaskId;
        }
        /**
         * Конструктор внутреннего класса для создания SubTask подзадач
         */
        public SubTask(Integer epicTaskId, String subTaskName, String subTaskDescription, Status subTaskStatus) {
            super(subTaskName, subTaskDescription, subTaskStatus);
            this.epicTaskId = epicTaskId;
        }
        /**
         * Конструктор внутреннего класса для обновления SubTask подзадач
         */
        public SubTask(Integer subTaskId, Integer epicTaskId, String subTaskName, String subTaskDescription
                , Status subTaskStatus) {
            super(subTaskName, subTaskDescription, subTaskStatus);
            this.epicTaskId = epicTaskId;
        }
        /**
         * Конструктор для копирования SubTask подзадач
         */
        public SubTask(SubTask subtask) {
            this(subtask.epicTaskId, subtask.getName(), subtask.getDescription(), subtask.getStatus());
        }
        /**
         * Переопределенные методы класса Objects для SubTask подзадач
         */
        @Override
        public String toString() {
            return "SubTask{" +
                    "id='" + getId() + '\'' +
                    ", epicTaskId='" + epicTaskId + '\'' +
                    ", name='" + getName() + '\'' +
                    ", description='" + getDescription() + '\'' +
                    ", status='" + getStatus() + '\'' +
                    '}';
        }
    }
}