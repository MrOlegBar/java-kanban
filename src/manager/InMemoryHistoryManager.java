package manager;

import task.Task;

import java.util.*;

/**
 * Класс для истории просмотра задач
 */
public class InMemoryHistoryManager implements HistoryManager<Task> {
    private final Map<Integer, Node> historyManagerMap = new HashMap<>();
    private Node first;
    private Node last;

    /**
     * Класс для узла двусвязного списка
     */
    private class Node {
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Task element, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * Помечает задачи как просмотренные
     */
    @Override
    public void add(Task task) {
        int taskId = task.getId();
        boolean isContainsNode = historyManagerMap.containsKey(taskId);
        if (isContainsNode) {
            remove(taskId);
        }
        linkLast(task);
        historyManagerMap.put(taskId, last);
    }

    /**
     * История просмотров задач
     */
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    /**
     * Удаляет задачи из просмотра
     */
    @Override
    public void remove(int id) {
        Node node = historyManagerMap.get(id);
        if (node != null) {
            removeNode(node);
            historyManagerMap.remove(id);
        }
    }

    /**
     * Добавляет задачу в конец двусвязного списка
     */
    public void linkLast(Task task) {
        final Node saveLast = last;
        final Node newNode = new Node(saveLast, task, null);
        last = newNode;
        if (saveLast == null)
            first = newNode;
        else
            saveLast.next = newNode;
    }

    /**
     * Собирает все задачи двусвязного списка в обычный ArrayList
     */
    private List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        int i = 0;
        for (Node x = first; x != null; x = x.next)
            result.add(i++, x.item);
        return result;
    }

    /**
     * Вырезает узел связного списка
     */
    private void removeNode(Node node) {
        final Node next = node.next;
        final Node prev = node.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.item = null;
    }
}