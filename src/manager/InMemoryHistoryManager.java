package manager;

import java.util.HashMap;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import model.*;

public class InMemoryHistoryManager implements HistoryManager {
    public CustomLinkedList<Task> tasks;
    private HashMap<Integer, Node<Task>> historyTasks;

    public InMemoryHistoryManager() {
        tasks = new CustomLinkedList<>();
        historyTasks = new HashMap<>();
    }

    @Override
    public void remove(int id) {
        tasks.removeNode(historyTasks.get(id));
        historyTasks.remove(id);
    }

    @Override
    public void add(Task newTask) {
        if (historyTasks.containsKey(newTask.getId())) {
            remove(newTask.getId());
            historyTasks.put(newTask.getId(), tasks.linkLast(newTask));
        } else {
            historyTasks.put(newTask.getId(), tasks.linkLast(newTask));
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasks.getTasks();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InMemoryHistoryManager)) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return tasks.equals(that.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks);
    }

    private static class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public Node<T> linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            this.size++;
            return newNode;
        }

        public List<T> getTasks() {
            List<T> content = new ArrayList<>();
            if (head == null) {
                return null;
            } else {
                for (Node<T> curHead = head; curHead != null; curHead = curHead.next) {
                    content.add(curHead.data);
                }
                return content;
            }
        }

        public void removeNode(Node<T> node) {
            Node<T> prevElem = node.prev;
            Node<T> nextElem = node.next;

            if (prevElem == null && nextElem == null) {
                tail = null;
                head = null;
            } else if (prevElem == null) {
                head = nextElem;
                nextElem.prev = null;
            } else if (nextElem == null) {
                tail = prevElem;
                prevElem.next = null;
            } else {
                prevElem.next = node.next;
                nextElem.prev = node.prev;
            }

            this.size--;
        }

        public int size() {
            return this.size;
        }
    }
}
