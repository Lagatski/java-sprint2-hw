import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager InMemoryTask = Manager.getDefault();
        ArrayList<Integer> idList;
        ArrayList<Task> tasks;
        ArrayList<Epic> epicTasks;
        ArrayList<Subtask> subTasks;
        Integer id;
        Integer epicId = 0;

        // Первый эпик
        Epic epic = new Epic("movie", "now");
        InMemoryTask.createNewEpicTask(epic);

        epicId = InMemoryTask.getEpicTask(0).id;
        Subtask task1 = new Subtask("collect boxes", "What", epicId);
        InMemoryTask.createNewSubTask(task1);

        epicId = InMemoryTask.getEpicTask(0).id;
        Subtask task2 = new Subtask("pack the cat", "Where", epicId);
        InMemoryTask.createNewSubTask(task2);

        // Второй эпик
        Epic epic2 = new Epic("buy food", "Where");
        InMemoryTask.createNewEpicTask(epic2);

        epicId = InMemoryTask.getEpicTask(3).id;
        Subtask task3 = new Subtask("get money", "When", epicId);
        InMemoryTask.createNewSubTask(task3);


        // 1.Выводим на печать весь список:
        System.out.println("1:");
        printAll(InMemoryTask);
        System.out.println("\n");


        // HISTORY.Вывожу историю просмотров
        System.out.println("HISTORY:");
        ArrayList<Task> history = InMemoryTask.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
        System.out.println("\n");


        // 2.Пробую вывести на печать по id;
        System.out.println("2:");
        System.out.println(InMemoryTask.getEpicTask(0));
        System.out.println(InMemoryTask.getSubTask(1));
        System.out.println("\n");


        // 3.Пробую вывести на печать все подзадачи для эпика по id;
        System.out.println("3:");
        System.out.println(InMemoryTask.getSubTasksFromEpic(0));
        System.out.println("\n");


        // 4.Пробую обновить 1 задачу и снова распечатать полный список
        System.out.println("4:");
        Epic tmpEpic = InMemoryTask.getEpicTask(0);
        epicId = tmpEpic.getId();
        idList = tmpEpic.idSubtasks;
        epic2 = new Epic("buy FOOOOD", "Where");
        epic2.id = epicId;
        epic2.idSubtasks = idList;
        epic2.status = Statuses.DONE;
        InMemoryTask.updateEpicTask(epic2);
        printAll(InMemoryTask);
        System.out.println("\n");


        // 4.1. Пробую обновить 1 задачу и снова распечатать полный список
        System.out.println("4.1:");
        tmpEpic = InMemoryTask.getEpicTask(0);
        epic2 = new Epic("buy FOOOOD", "Where");
        epic2.id = tmpEpic.getId();
        epic2.idSubtasks = tmpEpic.idSubtasks;
        epic2.status = Statuses.NEW;
        InMemoryTask.updateEpicTask(epic2);
        printAll(InMemoryTask);
        System.out.println("\n");


        // 4.2. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
        System.out.println("4.2:");
        Subtask tmpSub = InMemoryTask.getSubTask(1);
        task1 = new Subtask("COLLECT BOXES", "Where", epicId);
        task1.epicId = tmpSub.epicId;
        task1.id = tmpSub.getId();
        task1.status = Statuses.DONE;
        InMemoryTask.updateSubTask(task1);
        printAll(InMemoryTask);
        System.out.println("\n");


        // 4.3. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
        System.out.println("4.3:");
        tmpSub = InMemoryTask.getSubTask(2);
        task2 = new Subtask("COLLECT ITEMS", "Where", epicId);
        task2.id = tmpSub.getId();
        task2.epicId = tmpSub.epicId;
        task2.status = Statuses.DONE;
        InMemoryTask.updateSubTask(task2);
        printAll(InMemoryTask);
        System.out.println("\n");


        // 4.4. Пробую обновить 1 ПОДзадачу и снова распечатать полный список
        System.out.println("4.4:");
        tmpSub = InMemoryTask.getSubTask(2);
        task2 = new Subtask("COLLECT ITEMS", "Where", epicId);
        task2.id = tmpSub.getId();
        task2.epicId = tmpSub.epicId;
        task2.status = Statuses.IN_PROGRESS;
        InMemoryTask.updateSubTask(task2);
        printAll(InMemoryTask);
        System.out.println("\n");


        // HISTORY.Вывожу историю просмотров
        System.out.println("HISTORY:");
        history = InMemoryTask.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
        System.out.println("\n");


        // 5.Пробую удалить 1 задачу и 1 эпик и вывести что получилось на печать
        System.out.println("5:");
        InMemoryTask.deleteEpicTask(3); // Эпик
        InMemoryTask.deleteSubTask(2); // Подзадачу 0-ого эпика
        printAll(InMemoryTask);


        // 6.Пробую удалить всё
        System.out.println("7:");
        InMemoryTask.deleteTasks();
        InMemoryTask.deleteEpicTasks();
        InMemoryTask.deleteSubTasks();
        printAll(InMemoryTask);

    }

     private static void printAll(TaskManager InMemoryTask) {
        ArrayList<Task> tasks;
        ArrayList<Epic> epicTasks;
        ArrayList<Subtask> subTasks;

        tasks = InMemoryTask.getTasks();
        for (Task task : tasks) {
            System.out.println(task);
        }
        epicTasks = InMemoryTask.getEpicTasks();
        for (Epic task : epicTasks) {
            System.out.println(task);
        }
        subTasks = InMemoryTask.getSubTasks();
        for (Subtask task : subTasks) {
            System.out.println(task);
        }
    }
}