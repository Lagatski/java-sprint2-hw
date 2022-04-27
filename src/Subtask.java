import java.util.Objects;

public class Subtask extends Task {
    protected Integer epicId;

    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask)) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId.equals(subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
