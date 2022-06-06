package manager;

import task.Task;

import java.util.*;

/**
 * Класс для истории просмотра задач
 */
public class InMemoryHistoryManager<E> implements HistoryManager<Task> {
    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    private Map<Integer, InMemoryHistoryManager.Node> historyManagerMap = new HashMap<Integer, InMemoryHistoryManager.Node>();
    private static InMemoryHistoryManager customLinkedList= new InMemoryHistoryManager<>();

    /**
     * Класс для узла двусвязного списка
     */
    private static class Node<E> {
        Task item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, Task element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * Добавляет задачу в конец двусвязного списка
     */
    void linkLast(Task e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<E>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    /**
     * Собирает все задачи двусвязного списка в обычный ArrayList
     */
    private List<Task> getTasks() {
        List<Task> result = new ArrayList();
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next)
            result.add(i++, x.item);
        return result;
    }

    /**
     * Вырезает узел связного списка
     */
    private void removeNode(Node<E> x) {
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
    }

    /**
     * Удаляет задачи из просмотра
     */
    public void remove(int id) {
        removeNode(historyManagerMap.get(id));
        historyManagerMap.remove(id);
    }

    /**
     * Помечает задачи как просмотренные
     */
    public void add(Task e) {
        if (historyManagerMap.containsKey(e.getId())) {
            remove(e.getId());
        }
        customLinkedList.linkLast(e);
        historyManagerMap.put(e.getId(), last);
    }

    /**
     * История просмотров задач
     */
    public List<Task> getHistory() {
        return getTasks();
    }
}