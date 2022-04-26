import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    // Коллекции для сохранения задач:
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subTasks;
    private Integer id;

    public Manager() {
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        tasks = new HashMap<>();
        id = 0;
    }

    /*
     *   Ниже группа методов состоящая из Геттеров
     *
     *   Описание:
     *   getTasks - возвращает все объекты типа Task;
     *   getSubTasks - возвращает все объекты типа Subtask;
     *   getEpicTasks - возвращает все объекты типа Epic;
     *   getId() - возвращает id для новой задачи, после увеличивает её на 1;
     *   getTask(Integer id) - Получение задачи типа Task, @Integer id - идентификатор Task задачи;
     *   getEpicTask(Integer id) - Получение задачи типа Epic, @Integer id - идентификатор Epic задачи;
     *   getSubTask(Integer id) - Получение задачи типа Subtask, @Integer id - идентификатор Subtask задачи;
     *   getEpicTaskList(Integer id) - Получение списка всех подзадач определённого эпика,
     *                                 @Integer id - идентификатор Эпика;
     */

    public ArrayList<Task> getTasks() {
        ArrayList <Task> taskList = new ArrayList<>();

        for (Integer integer : tasks.keySet()) {
            taskList.add(tasks.get(integer));
        }

        return (taskList);
    }

    public ArrayList<Subtask> getSubTasks() {
        ArrayList <Subtask> subTaskList = new ArrayList<>();

        for (Integer integer : subTasks.keySet()) {
            subTaskList.add(subTasks.get(integer));
        }

        return (subTaskList);
    }

    public ArrayList<Epic> getEpicTasks() {
        ArrayList <Epic> epicList = new ArrayList<>();

        for (Integer integer : epics.keySet()) {
            epicList.add(epics.get(integer));
        }
        return (epicList);
    }

    public Integer getId() {
        return id++;
    }

    public Task getTask(Integer id) { // Получение задачи по идентификатору
        for (Integer integer : tasks.keySet()) {
            if (integer.equals(id)) {
                return (tasks.get(id));
            }
        }
        return null;
    }

    public Epic getEpicTask(Integer id) {
        for (Integer integer : epics.keySet()) {
            if (integer.equals(id)) {
                return (epics.get(id));
            }
        }
        return null;
    }

    public Subtask getSubTask(Integer id) {
        for (Integer integer : subTasks.keySet()) {
            if (integer.equals(id)) {
                return (subTasks.get(id));
            }
        }
        return null;
    }

    public ArrayList<Subtask> getSubTasksFromEpic(Integer id) {
        ArrayList<Subtask> subTask = new ArrayList<>();
        for (Integer integer : epics.keySet()) {
            if (integer.equals(id)) {
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

    public void deleteTasks() { // Удаление всех задач
        tasks.clear();
    }

    public void deleteEpicTasks() {
        epics.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
    }

    public void deleteTask(Integer id) {
        for (Integer integer : tasks.keySet()) {
            if (integer.equals(id)) {
                tasks.remove(id);
                return;
            }
        }
    }

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

    public void createNewTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void createNewEpicTask(Epic task) {
        epics.put(task.getId(), task);
    }

    public void createNewSubTask(Subtask task) {
        if (epics.containsKey(task.epicId)) { // Проверили что наша коллекция Эпиков содержит данный id;
            Epic tmpEpic = epics.get(task.epicId); // Получаем объект класса Epic из коллекции
            ArrayList<Integer> tmpSubId = tmpEpic.idSubtasks; // Получаем список подзадач из текущего эпика
            tmpSubId.add(task.id);
            tmpEpic.idSubtasks = tmpSubId; // Добавили в список id новой подзадачи
            epics.put(tmpEpic.getId(), tmpEpic); // Обновили эпик
            subTasks.put(task.getId(), task); // Добавили в подзадачи
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

    public void updateTask(Task task) {
        String newStatus = checkTaskStatus(task);
        if (newStatus.equals("ERROR")) {
            return;
        }

        if (!newStatus.equals("")) { // Если поменялся статус, то меняем на новый и обновляем:
            task.status = newStatus; // Обновили на новый
            tasks.put(task.getId(), task);
        } else {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpicTask(Epic epic) {
        Epic tmpEpic;
        ArrayList<Integer> tmpSubId;

        String newStatus = checkEpicStatus(epic);
        if (newStatus.equals("ERROR")) {
            return;
        }

        if (!newStatus.equals("")) { // Если поменялся статус, то проверим на что:
            /* Если придёт статус DONE или NEW, то поменяем у всех подзадач: */
            if (newStatus.equals("DONE") || newStatus.equals("NEW")) {
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

    public void updateSubTask(Subtask subTask) {
        Integer id;
        Integer tmpEpicId;
        Epic tmpEpic;
        ArrayList<Integer> tmpSubId;
        Integer count;

        String newStatus = checkSubStatus(subTask);
        if (newStatus.equals("ERROR")) {
            return;
        }

        id = subTask.getId();              // ID подзадачи
        Subtask tmpSub = subTasks.get(id); // Объект Подзадачи, который будем замещаться
        tmpEpicId = tmpSub.epicId;         // ID эпика
        tmpEpic = epics.get(tmpEpicId);    // Объект Эпика
        tmpSubId = tmpEpic.idSubtasks;     // Список подзадач в этом Эпике

        if (!newStatus.equals("")) {
            if (newStatus.equals("IN_PROGRESS")) { // проверить и поменять у Эпика статус на IN_PROGRESS;
                if (tmpEpic.status.equals(newStatus)) {
                    subTasks.put(subTask.getId(), subTask);
                } else {
                    tmpEpic.status = newStatus;
                    subTasks.put(subTask.getId(), subTask);
                }
            } else if (newStatus.equals("NEW")) {
                /* проверить если все подзадачи стали NEW, то установить Эпик в NEW.
                   А если статус Эпика сейчас DONE, то поменять его на IN_PROGRESS*/
                subTasks.put(subTask.getId(), subTask); // Сохраним сразу новую подзадачу, чтобы проверять с её учётом
                count = 0;
                for (Integer integer : tmpSubId) { // Если не выйдем досрочно, то выполниться следующее условие:
                    tmpSub = subTasks.get(integer);
                    if (!tmpSub.status.equals("NEW")) {
                        break;
                    }
                    count++;
                }
                if (count == tmpSubId.size()) {
                    tmpEpic.status = newStatus;
                } else if (tmpEpic.status.equals("DONE")) {
                    tmpEpic.status = "IN_PROGRESS";
                }
            } else if (newStatus.equals("DONE")) {
                /*  проверить статусы других подзадач этого эпика и если все они DONE,
                    то сменить на DONE. Nначе сменить на IN_PROGRESS; */
                subTasks.put(subTask.getId(), subTask); // Сохраним сразу новую подзадачу, чтобы проверять с её учётом
                count = 0;
                for (Integer integer : tmpSubId) { // Если не выйдем досрочно, то выполниться следующее условие:
                    tmpSub = subTasks.get(integer);
                    if (!tmpSub.status.equals("DONE")) {
                        break;
                    }
                    count++;
                }
                if (count == tmpSubId.size()) {
                    tmpEpic.status = newStatus;
                } else if (tmpEpic.status.equals("NEW")) {
                    tmpEpic.status = "IN_PROGRESS";
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

    private String checkTaskStatus(Task task) {
        String newStatus;
        Integer id;

        newStatus = task.status;
        id = task.getId(); // Получаем id объекта
        for (Integer integer : tasks.keySet()) {  // Nщем по id старый объект
            if (integer.equals(id)) {
                Task oldTask = tasks.get(integer); // Получаем старый объект для сравнения статусов
                if (oldTask.status.equals(newStatus)) {
                    return "";
                } else if (newStatus.equals("DONE")
                        || newStatus.equals("NEW") || newStatus.equals("IN_PROGRESS")) {
                    return newStatus;
                } else {
                    return "ERROR"; // Если вдруг по ошибке подаётся другой статус, который мы не обрабатываем
                }
            }
        }
        return "ERROR"; // В случае если id по какой-то причине отсутствует в списке
    }

    private String checkEpicStatus(Epic epic) {
        String newStatus;
        Integer id;

        newStatus = epic.status;
        id = epic.getId(); // Получаем id объекта
        for (Integer integer : epics.keySet()) { // Nщем по этому ID "старый" объект
            if (integer.equals(id)) {
                Task oldTask = epics.get(integer); // Получаем старый объект для сравнения статусов
                if (oldTask.status.equals(newStatus)) {
                    return "";
                } else if (newStatus.equals("DONE")
                        || newStatus.equals("NEW") || newStatus.equals("IN_PROGRESS")) {
                    return newStatus;
                } else {
                    return "ERROR"; // Если вдруг по ошибке подаётся другой статус, который мы не обрабатываем
                }
            }
        }
        return "ERROR"; // В случае если id по какой то причине отсутствует в списке
    }

    private String checkSubStatus(Subtask subTask) {
        String newStatus;
        Integer id;

        newStatus = subTask.status;
        id = subTask.getId(); // Получаем id объекта
        for (Integer integer : subTasks.keySet()) { // Nщем по этому ID "старый" объект
            if (integer.equals(id)) {
                Task oldTask = subTasks.get(integer); // Получаем старый объект для сравнения статусов
                if (oldTask.status.equals(newStatus)) {
                    return "";
                } else if (newStatus.equals("DONE")
                        || newStatus.equals("NEW") || newStatus.equals("IN_PROGRESS")) {
                    return newStatus;
                } else {
                    return "ERROR"; // Если вдруг по ошибке подаётся другой статус, который мы не обрабатываем
                }
            }
        }
        return "ERROR"; // В случае если id по какой то причине отсутствует в списке
    }

}
