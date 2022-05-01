import java.util.ArrayList;

public interface TaskManager {

    public ArrayList<Task> getTasks();
    public ArrayList<Subtask> getSubTasks();
    public ArrayList<Epic> getEpicTasks();
    public Task getTask(Integer id);
    public Epic getEpicTask(Integer id);
    public Subtask getSubTask(Integer id);
    public ArrayList<Subtask> getSubTasksFromEpic(Integer id);


    public void deleteTasks();
    public void deleteEpicTasks();
    public void deleteSubTasks();
    public void deleteTask(Integer id);
    public void deleteEpicTask(Integer id);
    public void deleteSubTask(Integer id);

    public void createNewTask(Task task);
    public void createNewEpicTask(Epic task);
    public void createNewSubTask(Subtask task);

    public void updateTask(Task task);
    public void updateEpicTask(Epic epic);
    public void updateSubTask(Subtask subTask);

    public ArrayList<Task> getHistory();
}
