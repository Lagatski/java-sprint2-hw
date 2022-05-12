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

    public FileBackedTasksManager(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            if (bufferedReader.ready()) {
                Integer id = 0;
                Map<Integer, String> lines = new LinkedHashMap<>();
                bufferedReader.readLine();
                while (bufferedReader.ready()) {
                    lines.put(++id, bufferedReader.readLine());
                }
                recoveryTaskAndHistory(lines, id);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.file = file;
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

    private void recoveryTaskAndHistory(Map<Integer, String> lines, Integer mapSize) {
        String[] idInHistory = lines.get(mapSize).split(",");

        for (Integer numOfString = 1; numOfString < mapSize-1; numOfString++) {
            String[] elements = lines.get(numOfString).split(",");

            if (elements[1].equals("TASK")) {
                Task task = new Task(elements[2], elements[4]);
                task.id = Integer.parseInt(elements[0]);
                if (elements[3].equals("NEW")) {
                    task.status = Status.NEW;
                } else if (elements[3].equals("DONE")) {
                    task.status = Status.DONE;
                } else if (elements[3].equals("IN_PROGRESS")) {
                    task.status = Status.IN_PROGRESS;
                }
                tasks.put(Integer.parseInt(elements[0]), task);
                for (String s : idInHistory) {
                    if (task.id.equals(Integer.parseInt(s))) {
                        historyManager.add(task);
                    }
                }
            } else if (elements[1].equals("EPIC")) {
                Epic epic = new Epic(elements[2], elements[4]);
                epic.id = Integer.parseInt(elements[0]);
                if (elements[3].equals("NEW")) {
                    epic.status = Status.NEW;
                } else if (elements[3].equals("DONE")) {
                    epic.status = Status.DONE;
                } else if (elements[3].equals("IN_PROGRESS")) {
                    epic.status = Status.IN_PROGRESS;
                }
                epics.put(Integer.parseInt(elements[0]), epic);
                for (String s : idInHistory) {
                    if (epic.id.equals(Integer.parseInt(s))) {
                        historyManager.add(epic);
                    }
                }
            } else if (elements[1].equals("SUBTASK")) {
                Subtask sub = new Subtask(elements[2], elements[4], Integer.parseInt(elements[5]));
                sub.id = Integer.parseInt(elements[0]);
                if (elements[3].equals("NEW")) {
                    sub.status = Status.NEW;
                } else if (elements[3].equals("DONE")) {
                    sub.status = Status.DONE;
                } else if (elements[3].equals("IN_PROGRESS")) {
                    sub.status = Status.IN_PROGRESS;
                }
                subTasks.put(Integer.parseInt(elements[0]), sub);
                for (String s : idInHistory) {
                    if (sub.id.equals(Integer.parseInt(s))) {
                        historyManager.add(sub);
                    }
                }
            }
        }
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter srcFile = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            srcFile.write("ID,TYPE,NAME,STATUS,DESCRIPTION,EPIC\n");
            for (Integer id : tasks.keySet()) {
                srcFile.write(tasks.get(id).toString() + "\n");
            }
            for (Integer id : epics.keySet()) {
                srcFile.write(epics.get(id).toString() + "\n");
            }
            for (Integer id : subTasks.keySet()) {
                srcFile.write(subTasks.get(id).toString() + "\n");
            }
            srcFile.write("HISTORY:\n");
            if (getHistory() != null) {
                List<Task> history = getHistory();
                for (Task task : history) {
                    srcFile.write(task.getId().toString() + ",");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ManagerSaveException();
        }
    }
}
