import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        ArrayList<Integer> idList;
        ArrayList<Task> tasks;
        ArrayList<Epic> epicTasks;
        ArrayList<Subtask> subTasks;
        Integer id;
        Integer epicId = 0;

        // Первый эпик
        Epic epic = new Epic("movie", "now");
        manager.createNewEpicTask(epic);

        epicId = manager.getEpicTask(0).id;
        Subtask task1 = new Subtask("collect boxes", "What", epicId);
        manager.createNewSubTask(task1);

        epicId = manager.getEpicTask(0).id;
        Subtask task2 = new Subtask("pack the cat", "Where", epicId);
        manager.createNewSubTask(task2);

        // Второй эпик
        Epic epic2 = new Epic("buy food", "Where");
        manager.createNewEpicTask(epic2);

        epicId = manager.getEpicTask(3).id;
        Subtask task3 = new Subtask("get money", "When", epicId);
        manager.createNewSubTask(task3);


        // 1.Выводим на печать весь список:
        System.out.println("1:");
        printAll(manager);
        System.out.println("\n");


        // 2.Пробую вывести на печать по id;
        System.out.println("2:");
        System.out.println(manager.getEpicTask(0));
        System.out.println(manager.getSubTask(1));
        System.out.println("\n");


        // 3.Пробую вывести на печать все подзадачи для эпика по id;
        System.out.println("3:");
        System.out.println(manager.getSubTasksFromEpic(0));
        System.out.println("\n");


        // 4.Пробую обновить 1 задачу и снова распечатать полный список
        System.out.println("4:");
        Epic tmpEpic = manager.getEpicTask(0);
        epicId = tmpEpic.getId();
        idList = tmpEpic.idSubtasks;
        epic2 = new Epic("buy FOOOOD", "Where");
        epic2.id = epicId;
        epic2.idSubtasks = idList;
        epic2.status = "DONE";
        manager.updateEpicTask(epic2);
        printAll(manager);
        System.out.println("\n");


        // 4.1. Пробую обновить 1 задачу и снова распечатать полный список
        System.out.println("4.1:");
        tmpEpic = manager.getEpicTask(0);
        epic2 = new Epic("buy FOOOOD", "Where");
        epic2.id = tmpEpic.getId();
        epic2.idSubtasks = tmpEpic.idSubtasks;
        epic2.status = "NEW";
        manager.updateEpicTask(epic2);
        printAll(manager);
        System.out.println("\n");


        // 4.2. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
        System.out.println("4.2:");
        Subtask tmpSub = manager.getSubTask(1);
        task1 = new Subtask("COLLECT BOXES", "Where", epicId);
        task1.epicId = tmpSub.epicId;
        task1.id = tmpSub.getId();
        task1.status = "DONE";
        manager.updateSubTask(task1);
        printAll(manager);
        System.out.println("\n");


        // 4.3. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
        System.out.println("4.3:");
        tmpSub = manager.getSubTask(2);
        task2 = new Subtask("COLLECT ITEMS", "Where", epicId);
        task2.id = tmpSub.getId();
        task2.epicId = tmpSub.epicId;
        task2.status = "DONE";
        manager.updateSubTask(task2);
        printAll(manager);
        System.out.println("\n");


        // 4.4. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
        System.out.println("4.4:");
        tmpSub = manager.getSubTask(2);
        task2 = new Subtask("COLLECT ITEMS", "Where", epicId);
        task2.id = tmpSub.getId();
        task2.epicId = tmpSub.epicId;
        task2.status = "IN_PROGRESS";
        manager.updateSubTask(task2);
        printAll(manager);
        System.out.println("\n");


        // 5.Пробую удалить 1 задачу и 1 эпик и вывести что получилось на печать
        System.out.println("5:");
        manager.deleteEpicTask(3); // Эпик
        manager.deleteSubTask(2); // Подзадачу 0-ого эпика
        printAll(manager);


        // 6.Пробую удалить всё
        System.out.println("6:");
        manager.deleteTasks();
        manager.deleteEpicTasks();
        manager.deleteSubTasks();
        printAll(manager);

    }

     private static void printAll(Manager manager) {
        ArrayList<Task> tasks;
        ArrayList<Epic> epicTasks;
        ArrayList<Subtask> subTasks;

        tasks = manager.getTasks();
        for (Task task : tasks) {
            System.out.println(task);
        }
        epicTasks = manager.getEpicTasks();
        for (Epic task : epicTasks) {
            System.out.println(task);
        }
        subTasks = manager.getSubTasks();
        for (Subtask task : subTasks) {
            System.out.println(task);
        }
    }
}