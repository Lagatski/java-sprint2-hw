public class Subtask extends Task {
    Integer epicId;

    public Subtask(String name, String description, Integer id, Integer epicId) {
        super(name, description, id);
        this.epicId = epicId;
    }

    // Переопределяем на всякий случай + для тестов
    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + super.name + '\'' +
                ", description='" + super.description + '\'' +
                ", id=" + super.id +
                ", status='" + super.status + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
