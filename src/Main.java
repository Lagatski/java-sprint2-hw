import java.util.ArrayList;
import java.util.List;
import model.*;
import manager.*;
import java.io.File;

public class Main {

    /*
    *   Закоментированный код проверок, чтобы можно было быстро проверить добавление в пустой файл новых задач.
     */

    public static void main(String[] args) {
        TaskManager InFileTask = Managers.loadFromFile(new File("../resources/saveTasks.csv"));

        ArrayList<Integer> idList;
        ArrayList<Task> tasks;
        ArrayList<Epic> epicTasks;
        ArrayList<Subtask> subTasks;
        Integer id;
        Integer epicId = 0;

//        // Первый эпик
//        Epic epic = new Epic("movie", "now");
//        InFileTask.createNewEpicTask(epic);
//
//        epicId = InFileTask.getEpic(0).id;
//        Subtask task1 = new Subtask("collect boxes", "What", epicId);
//        InFileTask.createNewSubTask(task1);
//
//        epicId = InFileTask.getEpic(0).id;
//        Subtask task2 = new Subtask("pack the cat", "Where", epicId);
//        InFileTask.createNewSubTask(task2);
//
//        // Второй эпик
//        Epic epic2 = new Epic("buy food", "Where");
//        InFileTask.createNewEpicTask(epic2);
//
//        epicId = InFileTask.getEpic(3).id;
//        Subtask task3 = new Subtask("get money", "When", epicId);
//        InFileTask.createNewSubTask(task3);
//
//
//        // 1.Выводим на печать весь список:
//        System.out.println("1:");
//        printAll(InFileTask);
//        System.out.println("\n");
//
//
//        // HISTORY.Вывожу историю просмотров
//        System.out.println("HISTORY:");
//        List<Task> history = InFileTask.getHistory();
//        if (history != null) {
//            for (Task task : history) {
//                System.out.println(task);
//            }
//        }
//        System.out.println("\n");
//
//
//        // 2.Пробую вывести на печать по id;
//        System.out.println("2:");
//        System.out.println(InFileTask.getEpic(0));
//        System.out.println(InFileTask.getSub(1));
//        System.out.println("\n");
//
//
//        // 3.Пробую вывести на печать все подзадачи для эпика по id = 0;
//        System.out.println("3:");
//        System.out.println(InFileTask.getSubTasksFromEpic(0));
//        System.out.println("\n");
//
//
//        // 4.Пробую обновить 1 задачу и снова распечатать полный список
//        System.out.println("4:");
//        Epic tmpEpic = InFileTask.getEpic(0);
//        epicId = tmpEpic.getId();
//        idList = tmpEpic.idSubtasks;
//        epic2 = new Epic("buy FOOOOD", "Where");
//        epic2.id = epicId;
//        epic2.idSubtasks = idList;
//        epic2.status = Status.DONE;
//        InFileTask.updateEpicTask(epic2);
//        printAll(InFileTask);
//        System.out.println("\n");
//
//
//        // 4.1. Пробую обновить 1 задачу и снова распечатать полный список
//        System.out.println("4.1:");
//        tmpEpic = InFileTask.getEpic(0);
//        epic2 = new Epic("buy FOOOOD", "Where");
//        epic2.id = tmpEpic.getId();
//        epic2.idSubtasks = tmpEpic.idSubtasks;
//        epic2.status = Status.NEW;
//        InFileTask.updateEpicTask(epic2);
//        printAll(InFileTask);
//        System.out.println("\n");
//
//
//        // 4.2. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
//        System.out.println("4.2:");
//        Subtask tmpSub = InFileTask.getSub(1);
//        task1 = new Subtask("COLLECT BOXES", "Where", epicId);
//        task1.epicId = tmpSub.epicId;
//        task1.id = tmpSub.getId();
//        task1.status = Status.DONE;
//        InFileTask.updateSubTask(task1);
//        printAll(InFileTask);
//        System.out.println("\n");
//
//
//        // 4.3. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
//        System.out.println("4.3:");
//        tmpSub = InFileTask.getSub(2);
//        task2 = new Subtask("COLLECT ITEMS", "Where", epicId);
//        task2.id = tmpSub.getId();
//        task2.epicId = tmpSub.epicId;
//        task2.status = Status.DONE;
//        InFileTask.updateSubTask(task2);
//        printAll(InFileTask);
//        System.out.println("\n");
//
//
//        // 4.4. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
//        System.out.println("4.4:");
//        tmpSub = InFileTask.getSub(2);
//        task2 = new Subtask("COLLECT ITEMS", "Where", epicId);
//        task2.id = tmpSub.getId();
//        task2.epicId = tmpSub.epicId;
//        task2.status = Status.IN_PROGRESS;
//        InFileTask.updateSubTask(task2);
//        printAll(InFileTask);
//        System.out.println("\n");


        // HISTORY.Вывожу историю просмотров
        System.out.println("HISTORY:");
        Subtask tmpSub = InFileTask.getSub(1); // Добиваю историю просмотров для теста
        tmpSub = InFileTask.getSub(2);
        tmpSub = InFileTask.getSub(4);
        List<Task> history = InFileTask.getHistory();
        if (history != null) {
            for (Task task : history) {
                System.out.println(task);
            }
        }
        System.out.println("\n");

    }

     private static void printAll(TaskManager InFileTask) {
        List<Task> tasks;
        List<Epic> epicTasks;
        List<Subtask> subTasks;

        tasks = InFileTask.getTasks();
        for (Task task : tasks) {
            System.out.println(task);
        }
        epicTasks = InFileTask.getEpicTasks();
        for (Epic task : epicTasks) {
            System.out.println(task);
        }
        subTasks = InFileTask.getSubTasks();
        for (Subtask task : subTasks) {
            System.out.println(task);
        }
    }
}