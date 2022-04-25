import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    // Коллекции для сохранения задач:
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epicTasks;
    private HashMap<Integer, Subtask> subTasks;
    private Integer id;

    // Для сравнений в методах:
    ArrayList<Integer> tmp;
    Task tempTask;
    Epic tempEpic;
    Subtask tempSubtask;
    public Manager() {
        epicTasks = new HashMap<>();
        subTasks = new HashMap<>();
        tasks = new HashMap<>();
        id = 0;
        tmp = new ArrayList<>();
        tempTask = new Task("","", 0);
        tempEpic = new Epic("","", 0, tmp);
        tempSubtask = new Subtask("","", 0, 0);
    }

    public ArrayList<Object> getTaskList() { // Получение списка всех задач (как ссылки на объекты)
        ArrayList<Object> taskList = new ArrayList<>();
        for (Integer integer : tasks.keySet()) {
            taskList.add(tasks.get(integer));
        }
        for (Integer integer : epicTasks.keySet()) {
            taskList.add(epicTasks.get(integer));
        }
        for (Integer integer : subTasks.keySet()) {
            taskList.add(subTasks.get(integer));
        }
        return (taskList);
    }

    public void deleteTaskList() { // Удаление всех задач
        tasks.clear();
        epicTasks.clear();
        subTasks.clear();
    }

    public void deleteTask(Integer id) { // Удаление задачи по идентификатору
        for (Integer integer : tasks.keySet()) {
            if (integer.equals(id)) {
                tasks.remove(id);
                return;
            }
        }
        for (Integer integer : epicTasks.keySet()) {
            if (integer.equals(id)) { // Если удаляемый id есть в Эпике, то нужно удалить все подзадачи
                Epic tmpEpic = epicTasks.get(id);
                ArrayList<Integer> tmpSubId = tmpEpic.idSubtasks;
                for (Integer integer1 : tmpSubId) {
                    subTasks.remove(integer1);
                }
                epicTasks.remove(id);
                return;
            }
        }
        for (Integer integer : subTasks.keySet()) { // Если удаляемый id это Подзадача, нужно удалить id в списке Эпика
            if (integer.equals(id)) {
                Subtask tmpSub = subTasks.get(id);
                Integer tmpEpicId = tmpSub.epicId;
                Epic tmpEpic = epicTasks.get(tmpEpicId);
                ArrayList<Integer> tmpSubTasks = tmpEpic.idSubtasks;
                for (Integer tmpSubTask : tmpSubTasks) {
                    if (tmpSubTask.equals(id)) {
                        tmpSubTasks.remove(tmpSubTask);
                        break;
                    }
                }
                tmpEpic.idSubtasks = tmpSubTasks;
                epicTasks.put(tmpEpic.getId(), tmpEpic);
                subTasks.remove(id);
                return;
            }
        }
        return;
    }

    public Object getTask(Integer id) { // Получение задачи по идентификатору
        for (Integer integer : tasks.keySet()) {
            if (integer.equals(id)) {
                return (tasks.get(id));
            }
        }
        for (Integer integer : epicTasks.keySet()) {
            if (integer.equals(id)) {
                return (epicTasks.get(id));
            }
        }
        for (Integer integer : subTasks.keySet()) {
            if (integer.equals(id)) {
                return (subTasks.get(id));
            }
        }
        return null;
    }

    public void crateNewTask(Object o) { // Создание новой задачи(в зависимости от класса сохраняем в нужную коллекцию)
        if (o.getClass() == tempTask.getClass()) {
            Task task = (Task)o;
            tasks.put(task.getId(), task);
        } else if (o.getClass() == tempEpic.getClass()) {
            Epic task = (Epic)o;
            epicTasks.put(task.getId(), task);
        } else if (o.getClass() == tempSubtask.getClass()) {
            Subtask task = (Subtask)o;
            if (epicTasks.containsKey(task.epicId)) { // Проверили что наша коллекция Эпиков содержит данный id;
                Epic tmpEpic = epicTasks.get(task.epicId); // Получаем объект класса Epic из коллекции
                ArrayList<Integer> tmpSubId = tmpEpic.idSubtasks; // Получаем список подзадач из текущего эпика
                tmpSubId.add(task.id);
                tmpEpic.idSubtasks = tmpSubId; // Добавили в список id новой подзадачи
                epicTasks.put(tmpEpic.getId(), tmpEpic); // Обновили эпик
                subTasks.put(task.getId(), task); // Добавили в подзадачи
            } else {
                return;
            }
        } else {
            return;
        }
    }

    public void updateTask(Object o) { // Метод для обновления задачи
        Integer id;
        Integer tmpEpicId;
        Epic tmpEpic;
        ArrayList<Integer> tmpSubId;

        String newStatus = checkStatus(o);
        if (newStatus.equals("ERROR")) {
            return;
        }
        if (o.getClass() == tempTask.getClass()) {
            Task task = (Task)o;
            if (!newStatus.equals("")) { // Если поменялся статус, то меняем на новый и обновляем:
                task.status = newStatus; // Обновили на новый
                tasks.put(task.getId(), task);
            } else {
                tasks.put(task.getId(), task);
            }
        } else if (o.getClass() == tempEpic.getClass()) {
            Epic task = (Epic)o;
            if (!newStatus.equals("")) { // Если поменялся статус, то проверим на что:
                // Если придёт статус DONE или NEW, то поменяем у всех подзадач
                if (newStatus.equals("DONE") || newStatus.equals("NEW")) {
                    id = task.getId();
                    tmpEpic = epicTasks.get(id);
                    tmpSubId = tmpEpic.idSubtasks;  // Список подзадач данного эпика
                    for (Integer integer : tmpSubId) { // Проходим по всем id из списка подзадач данного Эпика
                        Subtask tmpSub = subTasks.get(integer);
                        tmpSub.status = newStatus;
                    }
                    epicTasks.put(task.getId(), task); // Обновляем caм эпик
                } else { // Если останется IN_PROGRESS, то ничего не меняем так как это должно меняться от подзадач
                    return;
                }
            } else {
                epicTasks.put(task.getId(), task);
            }
        } else if (o.getClass() == tempSubtask.getClass()) {
            Subtask task = (Subtask)o;
            if (!newStatus.equals("")) {
                if (newStatus.equals("IN_PROGRESS")) {
                    // TODO проверить и поменять у Эпика статус на IN_PROGRESS;
                    id = task.getId();
                    Subtask tmpSub = subTasks.get(id);
                    tmpEpicId = tmpSub.epicId;
                    tmpEpic = epicTasks.get(tmpEpicId);
                    if (tmpEpic.status.equals(newStatus)) {
                        subTasks.put(task.getId(), task);
                    } else {
                        tmpEpic.status = newStatus;
                        subTasks.put(task.getId(), task);
                    }
                } else if (newStatus.equals("NEW")) {
                    // TODO проверить если все подзадачи стали NEW, то установить Эпик в NEW.
                    // TODO А если статус Эпика сейчас DONE, то поменять его на IN_PROGRESS
                    id = task.getId();
                    Subtask tmpSub = subTasks.get(id); // Объект Подзадачи, который будем замещаться
                    tmpEpicId = tmpSub.epicId; // ID эпика
                    tmpEpic = epicTasks.get(tmpEpicId); // Объект Эпика
                    tmpSubId = tmpEpic.idSubtasks; // Список подзадач в этом Эпике
                    subTasks.put(task.getId(), task); // Сохраним сразу новую подзадачу, чтобы проверять с её учётом
                    Integer count = 0;
                    for (Integer integer : tmpSubId) { // Если не выйдем досрочно, то выполниться следующее условие
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
                    // TODO проверить статусы других подзадач этого эпика и если все они DONE,
                    //  то сменить на DONE. Nначе сменить на IN_PROGRESS;
                    id = task.getId();
                    Subtask tmpSub = subTasks.get(id); // Объект Подзадачи, который будем замещаться
                    tmpEpicId = tmpSub.epicId; // ID эпика
                    tmpEpic = epicTasks.get(tmpEpicId); // Объект Эпика
                    tmpSubId = tmpEpic.idSubtasks; // Список подзадач в этом Эпике
                    subTasks.put(task.getId(), task); // Сохраним сразу новую подзадачу, чтобы проверять с её учётом
                    Integer count = 0;
                    for (Integer integer : tmpSubId) { // Если не выйдем досрочно, то выполниться следующее условие
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
                    subTasks.put(task.getId(), task);
                }
            } else {
                subTasks.put(task.getId(), task);
            }
        } else { // На всякий случай
            return;
        }
    }

    public ArrayList<Object> getEpicTaskList(Integer id) { // Получение списка всех подзадач определённого эпика
        ArrayList<Object> subTask = new ArrayList<>();
        for (Integer integer : epicTasks.keySet()) {
            if (integer.equals(id)) {
                Epic tempEpic = epicTasks.get(id);
                ArrayList<Integer> subTaskId = tempEpic.idSubtasks; // Нашли номера id в подзачах
                for (Integer integer1 : subTaskId) {
                    subTask.add(subTasks.get(integer1));
                }
                return subTask;
            }
        }
        return null;
    }

    public Integer getId() {
        return id++;
    }

    // В случае изменения статуса вернется его новое значение, иначе "". Если пришёл не верный объект -> ERROR;
    private String checkStatus(Object o) {
        String newStatus;
        Integer id;

        if (o.getClass() == tempTask.getClass()) {
            Task task = (Task)o;
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
                        System.out.println("ERROR");
                        return "ERROR"; // Если вдруг по ошибке подаётся другой статус, который мы не обрабатываем
                    }
                }
            }
            System.out.println("ERROR");
            return "ERROR"; // В случае если id по какой то причине отсутствует в списке
        } else if (o.getClass() == tempEpic.getClass()) {
            Epic task = (Epic)o;
            newStatus = task.status;
            id = task.getId(); // Получаем id объекта
            for (Integer integer : epicTasks.keySet()) { // Nщем по этому ID "старый" объект
                if (integer.equals(id)) {
                    Task oldTask = epicTasks.get(integer); // Получаем старый объект для сравнения статусов
                    if (oldTask.status.equals(newStatus)) {
                        return "";
                    } else if (newStatus.equals("DONE")
                            || newStatus.equals("NEW") || newStatus.equals("IN_PROGRESS")) {
                        return newStatus;
                    } else {
                        System.out.println("ERROR");
                        return "ERROR"; // Если вдруг по ошибке подаётся другой статус, который мы не обрабатываем
                    }
                }
            }
            System.out.println("ERROR");
            return "ERROR"; // В случае если id по какой то причине отсутствует в списке
        } else if (o.getClass() == tempSubtask.getClass()) {
            Subtask task = (Subtask)o;
            newStatus = task.status;
            id = task.getId(); // Получаем id объекта
            for (Integer integer : subTasks.keySet()) { // Nщем по этому ID "старый" объект
                if (integer.equals(id)) {
                    Task oldTask = subTasks.get(integer); // Получаем старый объект для сравнения статусов
                    if (oldTask.status.equals(newStatus)) {
                        return "";
                    } else if (newStatus.equals("DONE")
                            || newStatus.equals("NEW") || newStatus.equals("IN_PROGRESS")) {
                        return newStatus;
                    } else {
                        System.out.println("ERROR");
                        return "ERROR"; // Если вдруг по ошибке подаётся другой статус, который мы не обрабатываем
                    }
                }
            }
            System.out.println("ERROR");
            return "ERROR"; // В случае если id по какой то причине отсутствует в списке
        } else {
            return "ERROR"; // Объект не известного класса
        }
    }

}
