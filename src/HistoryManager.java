import java.util.ArrayList;

public interface HistoryManager {

    public void add(Task newTask);
    public ArrayList<Task> getHistory();
}
