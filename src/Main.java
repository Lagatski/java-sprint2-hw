import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        ArrayList<Integer> idList;
        ArrayList<Object> test;
        Integer id;
        Integer epicId;

        // Первый эпик
        idList = new ArrayList<>();
        epicId = manager.getId();
        Epic epic = new Epic("movie", "now", epicId, idList);
        manager.crateNewTask(epic);

        id = manager.getId();
        Subtask task1 = new Subtask("collect boxes", "What", id, epicId);
        manager.crateNewTask(task1);

        id = manager.getId();
        Subtask task2 = new Subtask("pack the cat", "Where", id, epicId);
        manager.crateNewTask(task2);

        // Второй эпик
        idList = new ArrayList<>();
        epicId = manager.getId();
        Epic epic2 = new Epic("buy food", "Where", epicId, idList);
        manager.crateNewTask(epic2);

        id = manager.getId();
        Subtask task3 = new Subtask("get money", "When", id, epicId);
        manager.crateNewTask(task3);


        // 1.Выводим на печать весь список:
        //System.out.println(manager.getTaskList());
        System.out.println("1:");
        test = manager.getTaskList();
        for (Object s : test) {
            System.out.println(s);
        }
        System.out.println("\n");


        // 2.Пробую вывести на печать по id;
        System.out.println("2:");
        System.out.println(manager.getTask(0) + "\n");

        // 3.Пробую вывести на печать все подзадачи для эпика по id;
        System.out.println("3:");
        System.out.println(manager.getEpicTaskList(0) + "\n");

        // 4.Пробую обновить 1 задачу и снова распечатать полный список
        System.out.println("4:");
        Epic tmpEpic = (Epic)manager.getTask(0);
        epicId = tmpEpic.getId();
        idList = tmpEpic.idSubtasks;
        epic2 = new Epic("buy FOOOOD", "Where", epicId, idList);
        epic2.status = "DONE";
        manager.updateTask(epic2);

        test = manager.getTaskList();
        for (Object s : test) {
            System.out.println(s);
        }
        System.out.println("\n");

        // 4.1. Пробую обновить 1 задачу и снова распечатать полный список
        System.out.println("4.1:");
        tmpEpic = (Epic)manager.getTask(0);
        epicId = tmpEpic.getId();
        idList = tmpEpic.idSubtasks;
        epic2 = new Epic("buy FOOOOD", "Where", epicId, idList);
        epic2.status = "NEW";
        manager.updateTask(epic2);

        test = manager.getTaskList();
        for (Object s : test) {
            System.out.println(s);
        }
        System.out.println("\n");

        // 4.2. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
        System.out.println("4.2:");
        Subtask tmpSub = (Subtask)manager.getTask(1);
        id = tmpSub.getId();
        epicId = tmpSub.epicId;
        task1 = new Subtask("COLLECT BOXES", "Where", id, epicId);
        task1.status = "DONE";
        manager.updateTask(task1);

        test = manager.getTaskList();
        for (Object s : test) {
            System.out.println(s);
        }
        System.out.println("\n");

        // 4.3. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
        System.out.println("4.3:");
        tmpSub = (Subtask)manager.getTask(2);
        id = tmpSub.getId();
        epicId = tmpSub.epicId;
        task2 = new Subtask("COLLECT ITEMS", "Where", id, epicId);
        task2.status = "DONE";
        manager.updateTask(task2);

        test = manager.getTaskList();
        for (Object s : test) {
            System.out.println(s);
        }
        System.out.println("\n");

        // 4.4. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
        System.out.println("4.4:");
        tmpSub = (Subtask)manager.getTask(2);
        id = tmpSub.getId();
        epicId = tmpSub.epicId;
        task2 = new Subtask("COLLECT ITEMS", "Where", id, epicId);
        task2.status = "IN_PROGRESS";
        manager.updateTask(task2);

        test = manager.getTaskList();
        for (Object s : test) {
            System.out.println(s);
        }
        System.out.println("\n");

        // 5.Пробую удалить 1 задачу и 1 эпик и вывести что получилось на печать
        System.out.println("5:");
        manager.deleteTask(3); // Эпик
        manager.deleteTask(2); // Подзадачу 0-ого эпика
        test = manager.getTaskList();
        for (Object s : test) {
            System.out.println(s);
        }

        // 6.Пробую удалить всё
        System.out.println("6:");
        manager.deleteTaskList();
        test = manager.getTaskList();
        for (Object s : test) {
            System.out.println(s);
        }

    }
}