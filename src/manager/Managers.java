package manager;

import java.io.File;

public class Managers {
    public static FileBackedTasksManager getDefault(File file) {
        return new FileBackedTasksManager(file, false);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        return new FileBackedTasksManager(file, true);
    }
}