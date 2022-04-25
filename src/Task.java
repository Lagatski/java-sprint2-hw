import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Integer id;
    protected String status;

    public Task(String name, String description, Integer id) {
        this.name = name;
        this.description = description;
        this.id = id;
        status = "NEW";
    }

    public Integer getId() {
        return id;
    }

    // Переопределяем на всякий случай + для тестов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(getId(), task.getId()) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, getId(), status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
