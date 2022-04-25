import java.util.ArrayList;

public class Epic extends Task {
    ArrayList idSubtasks;

    public Epic(String name, String description, Integer id, ArrayList idSubtasks) {
        super(name, description, id);
        this.idSubtasks = idSubtasks;
    }

    // Переопределяем на всякий случай + для тестов
    @Override
    public String toString() {
        return "Epic{" +
                "name='" + super.name + '\'' +
                ", description='" + super.description + '\'' +
                ", id=" + super.id +
                ", status='" + super.status + '\'' +
                ", idSubtasks=" + idSubtasks +
                '}';
    }
}
