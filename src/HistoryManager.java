import java.util.List;

public interface HistoryManager {

    /*
    *   void add(Task newTask) - Должен реализовывать добавление в истории просмотров задач в кэш;
    *   List<Task> getHistory() - Метод осуществляет возврат списка истории просторов;
     */

    public void add(Task newTask);

    public List<Task> getHistory();
}
