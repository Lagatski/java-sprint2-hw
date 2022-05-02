import java.util.List;

public interface TaskManager {

    /*
     *   Описание:
     *   getTasks - возвращает все объекты типа Task;
     *   getSubTasks - возвращает все объекты типа Subtask;
     *   getEpicTasks - возвращает все объекты типа Epic;
     *   getTask(Integer id) - Получение задачи типа Task, @Integer id - идентификатор Task задачи;
     *   getEpicTask(Integer id) - Получение задачи типа Epic, @Integer id - идентификатор Epic задачи;
     *   getSubTask(Integer id) - Получение задачи типа Subtask, @Integer id - идентификатор Subtask задачи;
     *   getEpicTaskList(Integer id) - Получение списка всех подзадач определённого эпика,
     *                                 @Integer id - идентификатор Эпика;
     *   public ArrayList<Task> getHistory() - Получение списка истории просмотренных задач у объекта HistoryManager;
     *   deleteTasks() - Удаляет все объекты класса Task;
     *   deleteEpicTasks() - Удаляет все объекты класса Epic;
     *   deleteSubTasks() - Удаляет все объекты класса Subtask;
     *   deleteTask(Integer id) - Удаляет одну задачу из списка Task задач. @Integer id - идентификатор задачи;
     *   deleteEpicTask(Integer id) - Удаляет одну задачу из списка Epic задач.
     *                                А также все связанные с ней подзадачи. @Integer id - идентификатор задачи;
     *   deleteSubTask(Integer id) - Удаляет одну задачу из списка Subtask задач.
     *                               А также удаляет идентификатор из списка подзадач своего Эпика.
     *                               @Integer id - идентификатор задачи;
     *   createNewTask(Task task) - Сохраняем новый объект Task;
     *   createNewEpicTask(Epic task) - Сохраняем новый объект Epic;
     *   createNewSubTask(Subtask task) - Сохраняем новый объект Subtask;
     */

    public List<Task> getTasks();

    public List<Subtask> getSubTasks();

    public List<Epic> getEpicTasks();

    public Task getTask(Integer id);

    public Epic getEpic(Integer id);

    public Subtask getSub(Integer id);

    public List<Subtask> getSubTasksFromEpic(Integer id);

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

    public List<Task> getHistory();
}
