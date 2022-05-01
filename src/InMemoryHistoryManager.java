import java.util.ArrayList;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> historyTasks;

    public InMemoryHistoryManager() {
        historyTasks = new ArrayList<>();
    }


    @Override
    public void add(Task newTask) {
        for (Task task : historyTasks) {
            if (task.getId().equals(newTask.getId())) {
                return;
            }
        }
        historyTasks.add(newTask);
    }

    @Override
    public ArrayList<Task> getHistory() {
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
