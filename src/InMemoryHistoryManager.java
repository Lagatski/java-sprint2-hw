import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> historyTasks;

    public InMemoryHistoryManager() {
        historyTasks = new ArrayList<>();
    }


    @Override
    public void add(Task newTask) {
        if (historyTasks.size() >= 10) {
            historyTasks.remove(0);
        }
        historyTasks.add(newTask);
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InMemoryHistoryManager)) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return historyTasks.equals(that.historyTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyTasks);
    }
}
