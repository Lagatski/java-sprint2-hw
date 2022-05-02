import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    // Коллекции для сохранения задач:
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subTasks;
    private Integer id;
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        tasks = new HashMap<>();
        id = 0;
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();

        for (Integer taskId : tasks.keySet()) {
            taskList.add(tasks.get(taskId));
        }

        return taskList;
    }

    @Override
    public List<Subtask> getSubTasks() {
        List<Subtask> subTaskList = new ArrayList<>();

        for (Integer taskId : subTasks.keySet()) {
            subTaskList.add(subTasks.get(taskId));
        }

        return subTaskList;
    }

    @Override
    public List<Epic> getEpicTasks() {
        List<Epic> epicList = new ArrayList<>();

        for (Integer taskId : epics.keySet()) {
            epicList.add(epics.get(taskId));
        }
        return epicList;
    }

    @Override
    public Task getTask(Integer id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(Integer id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSub(Integer id) {
        if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        }
        return null;
    }

    @Override
    public List<Subtask> getSubTasksFromEpic(Integer id) {
        ArrayList<Subtask> subTask = new ArrayList<>();
        if (epics.containsKey(id)) {
            Epic tempEpic = epics.get(id);
            ArrayList<Integer> subTaskId = tempEpic.idSubtasks; // Нашли номера id в подзачах
            for (Integer subId : subTaskId) {
                subTask.add(subTasks.get(subId));
            }
            return subTask;
        }
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void deleteTasks() { // Удаление всех задач
        tasks.clear();
    }

    @Override
    public void deleteEpicTasks() {
        epics.clear();
    }

    @Override
    public void deleteSubTasks() {
        subTasks.clear();
    }

    @Override
    public void deleteTask(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicTask(Integer id) {
        if (epics.containsKey(id)) { // Если удаляемый id есть в Эпике, то нужно удалить все подзадачи
            Epic tmpEpic = epics.get(id);
            ArrayList<Integer> tmpSubId = tmpEpic.idSubtasks;
            for (Integer subId : tmpSubId) {
                subTasks.remove(subId);
            }
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubTask(Integer id) {
        if (subTasks.containsKey(id)) {
            Subtask tmpSub = subTasks.get(id);
            Integer tmpEpicId = tmpSub.epicId;
            Epic tmpEpic = epics.get(tmpEpicId);
            ArrayList<Integer> tmpSubTasks = tmpEpic.idSubtasks;
            for (Integer tmpSubTask : tmpSubTasks) {
                if (tmpSubTask.equals(id)) {
                    tmpSubTasks.remove(tmpSubTask);
                    break;
                }
            }
            subTasks.remove(id);
        }
    }

    @Override
    public void createNewTask(Task task) {
        Integer id = generateId(); // Генерируем новый Id
        task.id = id;
        tasks.put(id, task);
    }

    @Override
    public void createNewEpicTask(Epic task) {
        Integer id = generateId(); // Генерируем новый Id
        task.id = id;
        epics.put(id, task);
    }

    @Override
    public void createNewSubTask(Subtask task) {
        if (epics.containsKey(task.epicId)) { // Проверили что наша коллекция Эпиков содержит данный id;
            Integer id = generateId();                        // Генерируем новый Id
            task.id = id;
            Epic tmpEpic = epics.get(task.epicId);            // Получаем объект класса Epic из коллекции
            ArrayList<Integer> tmpSubId = tmpEpic.idSubtasks; // Получаем список подзадач из текущего эпика
            tmpSubId.add(task.id);
            tmpEpic.idSubtasks = tmpSubId;      // Добавили и сохранили список Эпика id новой подзадачи
            subTasks.put(id, task);             // Добавили в подзадачи
        }
    }

    @Override
    public void updateTask(Task task) {
        Statuses newStatus = checkTaskStatus(task);
        if (newStatus == Statuses.ERROR) {
            return;
        }

        if (newStatus != null) { // Если поменялся статус, то меняем на новый и обновляем:
            task.status = newStatus; // Обновили на новый
            tasks.put(task.getId(), task);
        } else {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpicTask(Epic epic) {
        Epic tmpEpic;
        ArrayList<Integer> tmpSubId;

        Statuses newStatus = checkEpicStatus(epic);
        if (newStatus == Statuses.ERROR) {
            return;
        }

        if (newStatus != null) { // Если поменялся статус, то проверим на что:
            /* Если придёт статус DONE или NEW, то поменяем у всех подзадач: */
            if (newStatus == Statuses.DONE || newStatus == Statuses.NEW) {
                id = epic.getId();
                tmpEpic = epics.get(id);
                tmpSubId = tmpEpic.idSubtasks;     // Список подзадач данного эпика
                for (Integer subId : tmpSubId) { // Проходим по всем id из списка подзадач данного Эпика
                    Subtask tmpSub = subTasks.get(subId);
                    tmpSub.status = newStatus;
                }
                epics.put(epic.getId(), epic); // Обновляем caм эпик
            } // Если останется IN_PROGRESS, то ничего не меняем так как это должно меняться от подзадач
        } else {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(Subtask subTask) {
        Integer id;
        Integer tmpEpicId;
        Epic tmpEpic;
        ArrayList<Integer> tmpSubId;
        Integer count;

        Statuses newStatus = checkSubStatus(subTask);
        if (newStatus == Statuses.ERROR) {
            return;
        }
        System.out.println(newStatus);

        id = subTask.getId();              // ID подзадачи
        Subtask tmpSub = subTasks.get(id); // Объект Подзадачи, который будем замещаться
        tmpEpicId = tmpSub.epicId;         // ID эпика
        tmpEpic = epics.get(tmpEpicId);    // Объект Эпика
        tmpSubId = tmpEpic.idSubtasks;     // Список подзадач в этом Эпике

        if (newStatus != null) {
            if (newStatus == Statuses.IN_PROGRESS) { // проверить и поменять у Эпика статус на IN_PROGRESS;
                if (tmpEpic.status.equals(newStatus)) {
                    subTasks.put(subTask.getId(), subTask);
                } else {
                    tmpEpic.status = newStatus;
                    subTasks.put(subTask.getId(), subTask);
                }
            } else if (newStatus == Statuses.NEW) {
                /* проверить если все подзадачи стали NEW, то установить Эпик в NEW.
                   А если статус Эпика сейчас DONE, то поменять его на IN_PROGRESS*/
                subTasks.put(subTask.getId(), subTask); // Сохраним сразу новую подзадачу, чтобы проверять с её учётом
                count = 0;
                for (Integer subId : tmpSubId) { // Если не выйдем досрочно, то выполниться следующее условие:
                    tmpSub = subTasks.get(subId);
                    if (tmpSub.status != Statuses.NEW) {
                        break;
                    }
                    count++;
                }
                if (count == tmpSubId.size()) {
                    tmpEpic.status = newStatus;
                } else if (tmpEpic.status == Statuses.DONE) {
                    tmpEpic.status = Statuses.IN_PROGRESS;
                }
            } else if (newStatus == Statuses.DONE) {
                /*  проверить статусы других подзадач этого эпика и если все они DONE,
                    то сменить на DONE. Nначе сменить на IN_PROGRESS; */
                subTasks.put(subTask.getId(), subTask); // Сохраним сразу новую подзадачу, чтобы проверять с её учётом
                count = 0;
                for (Integer subId : tmpSubId) { // Если не выйдем досрочно, то выполниться следующее условие:
                    tmpSub = subTasks.get(subId);
                    if (tmpSub.status != Statuses.DONE) {
                        break;
                    }
                    count++;
                }
                if (count == tmpSubId.size()) {
                    tmpEpic.status = newStatus;
                } else if (tmpEpic.status == Statuses.NEW) {
                    tmpEpic.status = Statuses.IN_PROGRESS;
                }
            } else {
                subTasks.put(subTask.getId(), subTask);
            }
        } else {
            subTasks.put(subTask.getId(), subTask);
        }
    }

    /*
     *   checkTaskStatus(Task task);
     *   checkEpicStatus(Epic epic);
     *   checkSubStatus(Subtask subTask);
     *
     *   Описание:
     *
     *   Приватныe методы, вызываемый из методов обновления.
     *   В случае изменения статуса вернется его новое значение, иначе "".
     *   Если объект, который отсутствует в списке или не верно задан статус -> ERROR;
     */

    private Statuses checkTaskStatus(Task task) {
        Statuses newStatus;
        Integer id;

        newStatus = task.status;
        id = task.getId(); // Получаем id объекта
        if (tasks.containsKey(id)) {
            Task oldTask = tasks.get(id); // Получаем старый объект для сравнения статусов
            if (oldTask.status == newStatus) {
                return null;
            } else {
                return newStatus;
            }
        }
        return Statuses.ERROR; // В случае если id по какой-то причине отсутствует в списке
    }

    private Statuses checkEpicStatus(Epic epic) {
        Statuses newStatus;
        Integer id;

        newStatus = epic.status;
        System.out.println(newStatus);
        id = epic.getId(); // Получаем id объекта
        if (epics.containsKey(id)) {
            Task oldTask = epics.get(id); // Получаем старый объект для сравнения статусов
            if (oldTask.status == newStatus) {
                return null;
            } else {
                return newStatus;
            }
        }
        return Statuses.ERROR; // В случае если id по какой то причине отсутствует в списке
    }

    private Statuses checkSubStatus(Subtask subTask) {
        Statuses newStatus;
        Integer id;

        newStatus = subTask.status;
        id = subTask.getId(); // Получаем id объекта
        if (subTasks.containsKey(id)) {
            Task oldTask = subTasks.get(id); // Получаем старый объект для сравнения статусов
            if (oldTask.status == newStatus) {
                return null;
            } else {
                return newStatus;
            }
        }
        return Statuses.ERROR; // В случае если id по какой-то причине отсутствует в списке
    }

    /*
     *   generateId() - приватный метод для возвращения id для новой задачи, после увеличивает её на 1;
     */
    private Integer generateId() {
        return id++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InMemoryTaskManager)) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return getTasks().equals(that.getTasks()) && epics.equals(that.epics) &&
                getSubTasks().equals(that.getSubTasks()) && id.equals(that.id) &&
                historyManager.equals(that.historyManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTasks(), epics, getSubTasks(), id, historyManager);
    }
}
