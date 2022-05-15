package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.List;
import model.*;


public class InMemoryTaskManager implements TaskManager {
    // Коллекции для сохранения задач:
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subTasks;
    protected Integer id;
    protected HistoryManager historyManager;

    public InMemoryTaskManager() {
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        tasks = new HashMap<>();
        id = 0;
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public List<Subtask> getSubTasks() {
        return new ArrayList<>(this.subTasks.values());
    }

    @Override
    public List<Epic> getEpicTasks() {
        return new ArrayList<>(this.epics.values());
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
            ArrayList<Integer> subTaskId = tempEpic.getIdSubtasks();
            for (Integer subId : subTaskId) {
                subTask.add(subTasks.get(subId));
                historyManager.add(subTasks.get(subId));
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
    public void deleteTasks() {
        for (Integer task : tasks.keySet()) {
            historyManager.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteEpicTasks() {
        for (Integer task : epics.keySet()) {
            historyManager.remove(task);
        }
        for (Integer task : subTasks.keySet()) {
            historyManager.remove(task);
        }

        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        for (Integer task : subTasks.keySet()) {
            historyManager.remove(task);
        }

        subTasks.clear();

        for (Integer epicId : epics.keySet()) {
            epics.get(epicId).setStatus(Status.NEW);
        }
    }

    @Override
    public void deleteTask(Integer id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicTask(Integer id) {
        if (epics.containsKey(id)) { // Если удаляемый id есть в Эпике, то нужно удалить все подзадачи
            Epic tmpEpic = epics.get(id);
            ArrayList<Integer> tmpSubId = tmpEpic.getIdSubtasks();
            for (Integer subId : tmpSubId) {
                historyManager.remove(subId);
                subTasks.remove(subId);
            }
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubTask(Integer id) {
        if (subTasks.containsKey(id)) {
            Subtask tmpSub = subTasks.get(id);
            Integer tmpEpicId = tmpSub.getEpicId();
            Epic tmpEpic = epics.get(tmpEpicId);
            ArrayList<Integer> tmpSubTasks = tmpEpic.getIdSubtasks();
            for (Integer tmpSubTask : tmpSubTasks) {
                if (tmpSubTask.equals(id)) {
                    tmpSubTasks.remove(tmpSubTask);
                    break;
                }
            }
            historyManager.remove(id);
            subTasks.remove(id);
            checkEpicStatus(tmpEpicId);
        }
    }

    @Override
    public void createNewTask(Task task) {
        Integer id = generateId(); // Генерируем новый Id
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void createNewEpicTask(Epic task) {
        Integer id = generateId(); // Генерируем новый Id
        task.setId(id);
        epics.put(id, task);
    }

    @Override
    public void createNewSubTask(Subtask task) {
        if (epics.containsKey(task.getEpicId())) { // Проверили что наша коллекция Эпиков содержит данный id;
            Integer id = generateId();                        // Генерируем новый Id
            task.setId(id);
            Epic tmpEpic = epics.get(task.getEpicId());            // Получаем объект класса Epic из коллекции
            ArrayList<Integer> tmpSubId = tmpEpic.getIdSubtasks(); // Получаем список подзадач из текущего эпика
            tmpSubId.add(task.getId());
            subTasks.put(id, task);                           // Добавили в подзадачи
            checkEpicStatus(task.getEpicId());
        }
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpicTask(Epic epic) {
        Epic tmpEpic;
        ArrayList<Integer> tmpSubId;

        if (!epics.containsKey(epic.getId())) {
            return;
        }

        if (statusIsChange(epic)) { // Если придёт статус DONE или NEW, то поменяем у всех подзадач:
            if (epic.getStatus() == Status.DONE || epic.getStatus() == Status.NEW) {
                tmpEpic = epics.get(epic.getId());
                tmpSubId = tmpEpic.getIdSubtasks();     // Список подзадач данного эпика
                for (Integer subId : tmpSubId) {   // Проходим по всем id из списка подзадач данного Эпика
                    Subtask tmpSub = subTasks.get(subId);
                    tmpSub.setStatus(epic.getStatus());
                }
                epics.put(epic.getId(), epic); // Обновляем caм эпик
            }
        } else {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(Subtask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            return;
        }

        if (statusIsChange(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            checkEpicStatus(subTask.getEpicId());
        } else {
            subTasks.put(subTask.getId(), subTask);
        }
    }

    /*
     *   Описание:
     *
     *   statusIsChange(Task task) - Вернет true, если статус пришёл отличный от старого, иначе false;
     *   checkEpicStatus(Integer epicId) - Проверяет актуальность статуса у эпика. @Integer epicId - идентификатор;
     *   generateId() - приватный метод для возвращения id для новой задачи, после увеличивает её на 1;
     */

    private boolean statusIsChange(Task task) {
        Task oldTask;

        if (tasks.containsKey(task.getId())) { // Получаем старый объект для сравнения статусов
            oldTask = tasks.get(task.getId());
        } else if (epics.containsKey(task.getId())) {
            oldTask = epics.get(task.getId());
        } else {
            oldTask = subTasks.get(task.getId());
        }
        return oldTask.getStatus() == task.getStatus() ? false : true;
    }

    private void checkEpicStatus(Integer epicId) {
        Epic epic;
        ArrayList<Integer> tmpSubId;
        Integer countNew = 0;
        Integer countDone = 0;

        epic = epics.get(epicId);
        tmpSubId = epic.getIdSubtasks();

        for (Integer subId : tmpSubId) {
            if (subTasks.get(subId).getStatus() == Status.NEW) {
                countNew++;
            } else if (subTasks.get(subId).getStatus() == Status.DONE) {
                countDone++;
            }
        }

        if (countNew.equals(tmpSubId.size())) {
            epic.setStatus(Status.NEW);
        } else if (countDone.equals(tmpSubId.size())) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

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
