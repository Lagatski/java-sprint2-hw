import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    // Коллекции для сохранения задач:
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subTasks;
    private Integer id;
    HistoryManager historyManager;

    public InMemoryTaskManager() {
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        tasks = new HashMap<>();
        id = 0;
        historyManager = Manager.getDefaultHistory();
    }

    /*
     *   Ниже группа методов состоящая из Геттеров
     *
     *   Описание:
     *   getTasks - возвращает все объекты типа Task;
     *   getSubTasks - возвращает все объекты типа Subtask;
     *   getEpicTasks - возвращает все объекты типа Epic;
     *   generateId() - приватный метод для возвращения id для новой задачи, после увеличивает её на 1;
     *   getTask(Integer id) - Получение задачи типа Task, @Integer id - идентификатор Task задачи;
     *   getEpicTask(Integer id) - Получение задачи типа Epic, @Integer id - идентификатор Epic задачи;
     *   getSubTask(Integer id) - Получение задачи типа Subtask, @Integer id - идентификатор Subtask задачи;
     *   getEpicTaskList(Integer id) - Получение списка всех подзадач определённого эпика,
     *                                 @Integer id - идентификатор Эпика;
     *   public ArrayList<Task> getHistory() - Получение списка истории просмотренных задач
     *   private void add(Integer id) - Добавляет задачу в историю просмотров без дублирования уже просмотренных
     */

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList <Task> taskList = new ArrayList<>();

        for (Integer taskId : tasks.keySet()) {
            taskList.add(tasks.get(taskId));
        }

        return (taskList);
    }

    @Override
    public ArrayList<Subtask> getSubTasks() {
        ArrayList <Subtask> subTaskList = new ArrayList<>();

        for (Integer taskId : subTasks.keySet()) {
            subTaskList.add(subTasks.get(taskId));
        }

        return (subTaskList);
    }

    @Override
    public ArrayList<Epic> getEpicTasks() {
        ArrayList <Epic> epicList = new ArrayList<>();

        for (Integer taskId : epics.keySet()) {
            epicList.add(epics.get(taskId));
        }
        return (epicList);
    }

    private Integer generateId() {
        return id++;
    }

    @Override
    public Task getTask(Integer id) {
        for (Integer taskId : tasks.keySet()) {
            if (taskId.equals(id)) {
                historyManager.add(tasks.get(id));
                return (tasks.get(id));
            }
        }
        return null;
    }

    @Override
    public Epic getEpicTask(Integer id) {
        for (Integer taskId : epics.keySet()) {
            if (taskId.equals(id)) {
                historyManager.add(epics.get(id));
                return (epics.get(id));
            }
        }
        return null;
    }

    @Override
    public Subtask getSubTask(Integer id) {
        for (Integer taskId : subTasks.keySet()) {
            if (taskId.equals(id)) {
                historyManager.add(subTasks.get(id));
                return (subTasks.get(id));
            }
        }
        return null;
    }

    @Override
    public ArrayList<Subtask> getSubTasksFromEpic(Integer id) {
        ArrayList<Subtask> subTask = new ArrayList<>();
        for (Integer taskId : epics.keySet()) {
            if (taskId.equals(id)) {
                Epic tempEpic = epics.get(id);
                ArrayList<Integer> subTaskId = tempEpic.idSubtasks; // Нашли номера id в подзачах
                for (Integer integer1 : subTaskId) {
                    subTask.add(subTasks.get(integer1));
                }
                return subTask;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }


    /*
     *   Ниже группа методов для удаления задач
     *
     *   Описание:
     *   deleteTasks() - Удаляет все объекты класса Task;
     *   deleteEpicTasks() - Удаляет все объекты класса Epic;
     *   deleteSubTasks() - Удаляет все объекты класса Subtask;
     *   deleteTask(Integer id) - Удаляет одну задачу из списка Task задач. @Integer id - идентификатор задачи;
     *   deleteEpicTask(Integer id) - Удаляет одну задачу из списка Epic задач.
     *                                А также все связанные с ней подзадачи. @Integer id - идентификатор задачи;
     *   deleteSubTask(Integer id) - Удаляет одну задачу из списка Subtask задач.
     *                               А также удаляет идентификатор из списка подзадач своего Эпика.
     *                               @Integer id - идентификатор задачи;
     */

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
        for (Integer integer : tasks.keySet()) {
            if (integer.equals(id)) {
                tasks.remove(id);
                return;
            }
        }
    }

    @Override
    public void deleteEpicTask(Integer id) {
        for (Integer integer : epics.keySet()) {
            if (integer.equals(id)) { // Если удаляемый id есть в Эпике, то нужно удалить все подзадачи
                Epic tmpEpic = epics.get(id);
                ArrayList<Integer> tmpSubId = tmpEpic.idSubtasks;
                for (Integer integer1 : tmpSubId) {
                    subTasks.remove(integer1);
                }
                epics.remove(id);
                return;
            }
        }
    }

    @Override
    public void deleteSubTask(Integer id) {
        for (Integer integer : subTasks.keySet()) { // Если удаляемый id это Подзадача, нужно удалить id в списке Эпика
            if (integer.equals(id)) {
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
                tmpEpic.idSubtasks = tmpSubTasks;
                epics.put(tmpEpic.getId(), tmpEpic);
                subTasks.remove(id);
                return;
            }
        }
    }

    /*
     *   Ниже методы для создания задач
     *
     *   Описание:
     *   createNewTask(Task task) - Сохраняем новый объект Task;
     *   createNewEpicTask(Epic task) - Сохраняем новый объект Epic;
     *   createNewSubTask(Subtask task) - Сохраняем новый объект Subtask;
     */

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

    /*
     *   Ниже методы для обновления задач
     *
     *   Описание:
     *   updateTask(Task task) - Получает на вход новый объект и добавляет такой в текущий список;
     *   updateTask(Epic epic) - Получает на вход новый объект и добавляет такой в текущий список.
     *                           Включая актуальность статусов подзадач;
     *   updateTask(Subtask subTask) - Получает на вход новый объект и добавляет такой в текущий список.
     *                                 Включая проверку актуальности статуса своего эпика;
     */

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
                for (Integer integer : tmpSubId) { // Проходим по всем id из списка подзадач данного Эпика
                    Subtask tmpSub = subTasks.get(integer);
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
                for (Integer integer : tmpSubId) { // Если не выйдем досрочно, то выполниться следующее условие:
                    tmpSub = subTasks.get(integer);
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
            } else if (newStatus.equals("DONE")) {
                /*  проверить статусы других подзадач этого эпика и если все они DONE,
                    то сменить на DONE. Nначе сменить на IN_PROGRESS; */
                subTasks.put(subTask.getId(), subTask); // Сохраним сразу новую подзадачу, чтобы проверять с её учётом
                count = 0;
                for (Integer integer : tmpSubId) { // Если не выйдем досрочно, то выполниться следующее условие:
                    tmpSub = subTasks.get(integer);
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
        for (Integer integer : tasks.keySet()) {  // Nщем по id старый объект
            if (integer.equals(id)) {
                Task oldTask = tasks.get(integer); // Получаем старый объект для сравнения статусов
                if (oldTask.status == newStatus) {
                    return null;
                } else {
                    return newStatus;
                }
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
        for (Integer integer : epics.keySet()) { // Nщем по этому ID "старый" объект
            if (integer.equals(id)) {
                Task oldTask = epics.get(integer); // Получаем старый объект для сравнения статусов
                if (oldTask.status == newStatus) {
                    return null;
                } else {
                    return newStatus;
                }
            }
        }
        return Statuses.ERROR; // В случае если id по какой то причине отсутствует в списке
    }

    private Statuses checkSubStatus(Subtask subTask) {
        Statuses newStatus;
        Integer id;

        newStatus = subTask.status;
        id = subTask.getId(); // Получаем id объекта
        for (Integer integer : subTasks.keySet()) { // Nщем по этому ID "старый" объект
            if (integer.equals(id)) {
                Task oldTask = subTasks.get(integer); // Получаем старый объект для сравнения статусов
                if (oldTask.status == newStatus) {
                    return null;
                } else {
                    return newStatus;
                }
            }
        }
        return Statuses.ERROR; // В случае если id по какой то причине отсутствует в списке
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
