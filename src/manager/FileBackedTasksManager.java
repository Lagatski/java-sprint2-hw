package manager;

import model.*;

import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file, boolean load) {
        this.file = file;
        if (load) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                if (bufferedReader.ready()) {
                    bufferedReader.readLine();
                    while (bufferedReader.ready()) {
                        String line = bufferedReader.readLine();
                        if (line.equals("")) {
                            line = bufferedReader.readLine();
                            String[] idInHistory = line.split(",");
                            recoveryHistory(idInHistory);
                            break;
                        }
                        recoveryTask(line);
                    }
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Can't read from file: " + file.getName());
            }
        }
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpicTasks() {
        super.deleteEpicTasks();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void createNewEpicTask(Epic task) {
        super.createNewEpicTask(task);
        save();
    }

    @Override
    public void createNewSubTask(Subtask task) {
        super.createNewSubTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(Epic epic) {
        super.updateEpicTask(epic);
        save();
    }

    @Override
    public void updateSubTask(Subtask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public Task getTask(Integer id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(Integer id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSub(Integer id) {
        Subtask sub = super.getSub(id);
        save();
        return sub;
    }

    private void recoveryTask(String line) {
        String[] elements = line.split(",");
        if (id <= Integer.parseInt(elements[0])) {
            id = Integer.parseInt(elements[0]) + 1;
        }
        switch (elements[1]) {
            case ("TASK"): {
                Task task = new Task(Integer.parseInt(elements[0]), elements[2],
                        elements[4], Status.valueOf(elements[3]));
                tasks.put(task.getId(), task);
                break;
            }
            case ("EPIC"): {
                Epic epic = new Epic(Integer.parseInt(elements[0]), elements[2],
                        elements[4], Status.valueOf(elements[3]));
                epics.put(epic.getId(), epic);
                break;
            }
            case ("SUBTASK"): {
                Subtask sub = new Subtask(Integer.parseInt(elements[0]), elements[2],
                        elements[4], Status.valueOf(elements[3]), Integer.parseInt(elements[5]));
                subTasks.put(sub.getId(), sub);
                epics.get(sub.getEpicId()).getIdSubtasks().add(sub.getId()); // Добавили id в список подзадач эпика
                break;
            }
            default:
                break;
        }
    }

    private void recoveryHistory(String[] idInHistory) {
        for (String elem : idInHistory) {
            Integer id = Integer.parseInt(elem);
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            } else if (epics.containsKey(id)) {
                historyManager.add(epics.get(id));
            } else {
                historyManager.add(subTasks.get(id));
            }
        }
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter srcFile = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            srcFile.write("id,type,name,status,description,epic\n");
            for (Integer id : tasks.keySet()) {
                srcFile.write(tasks.get(id).toString() + "\n");
            }
            for (Integer id : epics.keySet()) {
                srcFile.write(epics.get(id).toString() + "\n");
            }
            for (Integer id : subTasks.keySet()) {
                srcFile.write(subTasks.get(id).toString() + "\n");
            }
            srcFile.write("\n");
            if (getHistory() != null) {
                List<Task> history = getHistory();
                for (Task task : history) {
                    srcFile.write(task.getId().toString() + ",");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName());
        }
    }
}
